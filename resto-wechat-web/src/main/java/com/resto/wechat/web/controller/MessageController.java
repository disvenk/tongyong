package com.resto.wechat.web.controller;

import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.entity.MdlUpload;
import com.resto.brand.core.entity.ResultDto;
import com.resto.brand.core.util.HttpPostUtil;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.core.util.QRCodeUtil;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.OrderState;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.constant.ProductionStatus;
import com.resto.shop.web.model.*;
import com.resto.api.customer.entity.Customer;
import com.resto.shop.web.service.*;
import com.resto.wechat.web.config.SessionKey;
import com.resto.wechat.web.rpcinterceptors.DataSourceTarget;
import com.resto.wechat.web.util.EmojiFilter;
import com.resto.wechat.web.util.ImageUtil;
import com.resto.wechat.web.util.MessageUtil;
import com.resto.wechat.web.util.WeChatCardUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@RestController
@RequestMapping("msg")
public class MessageController extends GenericController {

    @Resource
    BrandSettingService brandSettingService;

    @Resource
    BrandService brandService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    private GetNumberService getNumberService;

    @Resource
    private CustomerService customerService;

    @Resource
    private NewCustomerService newCustomerService;

    @Resource
    private OrderService orderService;

    @Resource
    private WechatConfigService wechatConfigService;
    @Autowired
    WeChatService weChatService;

    @Autowired
    OrderPaymentItemService orderPaymentItemService;

    @Autowired
    WeChatReceiptService weChatReceiptService;

    @RequestMapping(method = RequestMethod.GET)
    public String wechatSign(String echostr, String signature, String timestamp, String nonce, HttpServletRequest request) {
		log.info("开始验签--");
        if (weChatService.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return "fail";
    }


//    @RequestMapping(method = RequestMethod.GET)
//    public String decrypt(String encrypt_type,String msg_signature,String timetamp,String nonce, HttpServletRequest request) throws Exception {
//        String wechatXmlMsg = readRequestString(request);
//        log.info("接收到微信第三方平台发送的加密信息: " + wechatXmlMsg);
//        //String fromXML,String timestamp,String msgSignature,String nonce, String appId,String encodingAesKey
//        log.info("encrypt_type"+encrypt_type);
//        log.info("msg_signature"+msg_signature);
//        String wechatXml = readRequestString(request);
//        log.info("wechatXml"+wechatXml);
//
////        String appId = jsonObject.getString("AppId");
////        String encodingAesKey = jsonObject.getString("Encrypt");
////        weChatService.getWechatmsgDecrypt(fromxml,timetamp,msg_signature,nonce,appId,encodingAesKey);
//        return "success";
//    }




    @RequestMapping(method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String wechatMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURL = request.getRequestURL().toString();
        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
        Brand brand = brandService.selectBySign(brandSign);
        String wechatXmlMsg = readRequestString(request);
        log.info("接收到微信消息请求: " + wechatXmlMsg);
        String reply = "success";
        try {
            JSONObject msg = com.resto.brand.core.util.StringUtils.xml2Json(wechatXmlMsg);
            String msgType = msg.getString("MsgType");
            String fromUser = msg.getString("FromUserName");    //用户的id
            String toUser = msg.getString("ToUserName");        //公众号的id
            if ("event".equals(msgType)) {
                String event = msg.getString("Event");
                //微信会员卡，卡券事件推送
                if("user_get_card".equals(event)){
                    log.info("进入微信卡券事件");
                    String cardId=msg.getString("CardId");
                    String userCardCode=msg.getString("UserCardCode");
                    reply = updateMemberCardMsg(request,fromUser,cardId,userCardCode);
                    return reply;
                }
                if (!msg.has("EventKey")) {//如果不存在 EventKey 则默认回复success
                    return reply;
                }
                String eventKey = msg.getString("EventKey");
                if ("subscribe".equals(event)) {//扫码后-->关注事件
                    if (eventKey != null && msg.has("Ticket")) {//判断是否为扫描带参数二维码事件推送
                        reply = executeEventKeyMsg(fromUser, toUser, eventKey);
                    } else {//默认关注事件
                        reply = sendDefaultMsg(fromUser, toUser);
                    }
                } else if ("SCAN".equals(event) && eventKey != null) {//已关注-->扫码后触发事件
                    reply = executeEventKeyMsg(fromUser, toUser, eventKey);
                } else if ("CLICK".equals(event) && "photoMsg".equals(eventKey)) {
                    reply = executePhotoMsg(toUser, fromUser, request, response);
                    log.info("reply:" + reply);
                }

                if("user_authorize_invoice".equals(event)){//发票授权后推送事件
                    Map<String, String> requestMap = MessageUtil.xmlToMap(request);
                    String fromUserName = requestMap.get("FromUserName");
                    String succOrderId = requestMap.get("SuccOrderId");
                    String failOrderId = requestMap.get("FailOrderId");
                    String authorizeAppId = requestMap.get("AuthorizeAppId");
                    if(failOrderId!=null && !"".equals(failOrderId)){
                        log.error("授权页申请发票失败,订单id:"+failOrderId+",用户id:"+fromUserName);
                        return "";
                    }

                    WechatConfig wechatConfig = wechatConfigService.selectById(brand.getWechatConfigId());
                    request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID,brand.getId() );
                    BigDecimal money = orderPaymentItemService.selectEnableTicketMoney(succOrderId);
                    String resutlt = weChatReceiptService.blueTicket(brand.getId(), succOrderId, money, fromUserName, wechatConfig.getAppid(), wechatConfig.getAppsecret());
                    if(!resutlt.equals("sucesss")){
                        log.error("开票请求失败,流水号:"+failOrderId+",用户id:"+fromUserName);
                    }
                    return "";//可以直接回复空串，服务器不做任何处理也不做重试
                }

                if("cloud_invoice_invoiceresult_event".equals(event)){//开票结果异步通知
                    Map<String, String> requestMap = MessageUtil.xmlToMap(request);
                    String fpqqlsh = requestMap.get("fpqqlsh");//流水号
                    String nsrsbh = requestMap.get("nsrsbh");//识别号
                    String status = requestMap.get("status").toString();
                    if("2".equals(status)){
                        log.error("开票失败,流水号："+fpqqlsh+",纳税人识别号："+nsrsbh);
                        return "";//可以直接回复空串，服务器不做任何处理也不做重试
                    }
                    request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID,brand.getId());
                    int i = weChatReceiptService.updateReceipt(fpqqlsh);

                    if(i<=0){
                        log.error("更新发票状态失败，流水号"+fpqqlsh);
                        return "";
                    }

                    return "";
                }
            } else if ("text".equals(msgType)) {
                Map<String, Object> selectMap = new HashMap<>();
                String content = msg.getString("Content");
                //验证订单流水号
                String serialNumberYZ = "[0-9]{4}[0-1]{1}[0-9]{1}[0-3]{1}[0-9]{1}" +
                        "[0-2]{1}[0-9]{1}[0-5]{1}[0-9]{1}[0-5]{1}[0-9]{1}" +
                        "[0-9]{4}";
                //验证订单编号
                String orderIdYZ = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{32}$";
                Pattern serialNumberP = Pattern.compile(serialNumberYZ);
                Pattern orderIdP = Pattern.compile(orderIdYZ);
                if (serialNumberP.matcher(content).matches()) {
                    selectMap.put("serialNumber", content);
                } else if (orderIdP.matcher(content).matches()) {
                    selectMap.put("orderId", content);
                } else {
                    return reply;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<Order> resultOrder = orderService.selectWXOrderItems(selectMap);
                if (!CollectionUtils.isEmpty(resultOrder)) {
                    StringBuffer sb = new StringBuffer();
                    for (Order order : resultOrder) {
                        sb.append("【订单编号】：\n    " + order.getSerialNumber());
                        if (order.getPushOrderTime() != null) {
                            sb.append("\n【下单时间】：\n    " + dateFormat.format(order.getPushOrderTime()));
                        }
                        if (order.getPrintOrderTime() != null) {
                            sb.append("\n【打印时间】：\n    " + dateFormat.format(order.getPrintOrderTime()));
                        }
                        if (order.getCallNumberTime() != null) {
                            sb.append("\n【叫号时间】：\n    " + dateFormat.format(order.getCallNumberTime()));
                        }
                        sb.append("\n【订单状态】：" + OrderState.getStateName(order.getOrderState()));
                        sb.append("\n【生产状态】：" + ProductionStatus.getStatusName(order.getProductionStatus()));
                        sb.append("\n【订单明细】");
                        Map<Object, Object> maps = new HashMap<>();
                        for (OrderItem item : order.getOrderItems()) {
                            maps.put(item.getArticleName(), item.getCount());
                        }
                        for (Map.Entry<Object, Object> map : maps.entrySet()) {
                            sb.append("\n  |_" + map.getKey().toString()
                                    + "x" + map.getValue().toString());
                        }
                        maps.clear();
                        sb.append("\n【支付方式】");
                        for (OrderPaymentItem item : order.getOrderPaymentItems()) {
                            maps.put(PayMode.getPayModeName((Integer) item.getPaymentModeId()), item.getPayValue());
                        }
                        for (Map.Entry<Object, Object> map : maps.entrySet()) {
                            sb.append("\n  |_" + map.getKey().toString()
                                    + "：" + map.getValue().toString());
                        }
                        sb.append("\n【订单金额】：" + order.getOrderMoney());
                    }
                    reply = weChatService.getXmlText(fromUser, toUser, sb.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信消息处理异常:" + e.getMessage());
            response.sendError(500, e + " message:" + e.getMessage());
        }
        return reply;
    }


    private String readRequestString(HttpServletRequest request) throws IOException {
        StringBuffer sb = new StringBuffer();
        InputStream is;
        is = request.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        return sb.toString();
    }


    /**
     * 根据不同类型处理不同的预定义消息
     *
     * @param toUser   【用户ID】
     * @param fromUser 【公众号ID】
     * @param eventKey 【微信发来的自定义参数】
     * @return
     */
    private String executeEventKeyMsg(String toUser, String fromUser, String eventKey) {
        String reply = "";
        JSONObject obj = getMsgParam(eventKey);
        log.info("\n\n" + obj.toString() + "\n\n");
        log.info("\n\n" + obj.has("QrCodeId") + "\n\n");
//        if (obj.has("QrCodeId")) {
//            QueueQrcode queueQrcode = queueQrcodeService.selectById(obj.get("QrCodeId").toString());
//            if ("quhao".equals(queueQrcode.getType())) {//取号
//                reply = sendQuHaoUrl(toUser, fromUser, queueQrcode);
//            }
//        } else {//默认触发绑定等位红包操作
//            reply = sendQueueMsg(toUser, fromUser, obj);
//        }
        reply = sendQueueMsg(toUser, fromUser, obj);
        log.info("\n【发送给用户】：\n" + reply + "\n\n");
        return reply;
    }

    private String executePhotoMsg(String toUser, String fromUser, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reply = "success";
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0,lastR)+"/";
        systemPath = systemPath + "informationPhoto/";
//        systemPath = systemPath.replace("wechat","shop");

        log.info("当前地址：" + systemPath);

        String requestURL = request.getRequestURL().toString();
        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
        Brand brand = brandService.selectBySign(brandSign);
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
        DataSourceTarget.setDataSourceName(brand.getId());

        WechatConfig wConfig = brand.getWechatConfig();
        log.info("当前品牌：" + brand.getBrandName());
        Customer customer = customerService.login(getCurrentBrandId(),fromUser);
        if(customer == null){
            String accessToken = weChatService.getAccessToken(wConfig.getAppid(), wConfig.getAppsecret());
            String customInfoJson = weChatService.getUserInfoSubscribe(accessToken, fromUser);
            JSONObject cusInfo = new JSONObject(customInfoJson);
            customer = new Customer();
            customer.setWechatId(cusInfo.getString("openid"));
            customer.setNickname(EmojiFilter.filterEmoji(cusInfo.getString("nickname")));
            customer.setSex(cusInfo.getInt("sex"));
            customer.setProvince(cusInfo.getString("province"));
            customer.setCity(cusInfo.getString("city"));
            customer.setCountry(cusInfo.getString("country"));
            customer.setHeadPhoto(cusInfo.getString("headimgurl"));
            customer.setBrandId(brand.getId());
            customer = customerService.register(customer);
            customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),customer.getId());
        }

        log.info("当前用户：" + customer.getNickname());
        //推送文本
        StringBuffer msg = new StringBuffer();
        if(brandSetting.getShareText() != null){
            brandSetting.setShareText(brandSetting.getShareText().replaceAll("<p>","").replaceAll("</p>","\n").replaceAll("<br>","").replaceAll("&nbsp;"," "));
        }
        if(brandSetting.getShareText() != null && !"".equals(brandSetting.getShareText().trim().replace(" ",""))){
            msg.append(brandSetting.getShareText());
        }else{
            msg.append("分享图片，邀请好友领取好礼：\n" +
                    "· 好友扫码领取专享邀请券；\n" +
                    "· 好友到店消费，回馈返利红包；\n" +
                    "还在等什么，赶紧分享好友吧~");
        }
        weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), wConfig.getAppid(), wConfig.getAppsecret());
        //得到个人邀请二维码;
        String brandLogoPath = ImageUtil.download(brandSetting.getRedPackageLogo(), request);
        //, systemPath.replace("informationPhoto", "wechat") + "assets/img/ling.png"    领字的二维码中间 logo图片地址
        if(brandSetting.getOpenHttps() == 1){
            QRCodeUtil.createQRCodeInformation("https://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?notIntoShop=1&df=" + customer.getSerialNumber(), systemPath, customer.getId() + "Infomation.jpg", brandLogoPath);
        }else{
            QRCodeUtil.createQRCodeInformation("http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?notIntoShop=1&df=" + customer.getSerialNumber(), systemPath, customer.getId() + "Infomation.jpg", brandLogoPath);
        }
        ImageUtil.deleteFile(new File(brandLogoPath));
        String text = "邀请你领取「" + brand.getBrandName() + "」红包";
        String one = "";
        String two = "";
        if(text.length() > 13){
            one = text.substring(0,13);
            two = text.substring(13,text.length());
        }

        //水印头像
        ImageUtil.pressImage(customer, "http://pic-article.oss-cn-shanghai.aliyuncs.com/ag4sp1mk2fCAIUz1AA75pyXfwtI697.png", systemPath + customer.getId() + "shuiyin.jpg", request);
        //水印二维码
        ImageUtil.pressQrcode(systemPath + customer.getId() + "Infomation.jpg", customer, systemPath + customer.getId() + "shuiyin2.jpg", request);
        if(text.length() > 13){
            //水印昵称
            ImageUtil.pressText(one, customer, systemPath + customer.getId() + "shuiyin3.jpg", request);
            //水印店铺
            ImageUtil.pressShopName(two, customer, systemPath + customer.getId() + "shuiyin4.jpg", request);
            ResultDto<MdlUpload> result = uploadPhoto(weChatService.getAccessToken(wConfig.getAppid(), wConfig.getAppsecret()), "image", systemPath + customer.getId() + "shuiyin4.jpg");
            JSONObject cusInfo = new JSONObject(result.getObj());
            weChatService.sendCustomerImageMsg(cusInfo.getString("media_id"), fromUser, wConfig.getAppid(), wConfig.getAppsecret());
        }else{
            ImageUtil.pressOneText(text, customer, systemPath + customer.getId() + "shuiyin3.jpg", request);
            ResultDto<MdlUpload> result = uploadPhoto(weChatService.getAccessToken(wConfig.getAppid(), wConfig.getAppsecret()), "image", systemPath + customer.getId() + "shuiyin3.jpg");
            JSONObject cusInfo = new JSONObject(result.getObj());
            weChatService.sendCustomerImageMsg(cusInfo.getString("media_id"), fromUser, wConfig.getAppid(), wConfig.getAppsecret());
        }
        ImageUtil.deleteFile(new File(systemPath + customer.getId() + "headPhoto.jpg"));
        ImageUtil.deleteFile(new File(systemPath + customer.getId() + "headPhoto2.jpg"));
        ImageUtil.deleteFile(new File(systemPath + customer.getId() + "Infomation.jpg"));
        if(text.length() > 13){
            ImageUtil.deleteFile(new File(systemPath + customer.getId() + "shuiyin4.jpg"));
        }
        return reply;
    }

    final String MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESSTOKEN&type=TYPEA";

    public ResultDto<MdlUpload> uploadPhoto(String accessToken, String type, String filePath) {
        ResultDto<MdlUpload> result = new ResultDto<MdlUpload>();
        String url = MEDIA_URL.replace("ACCESSTOKEN", accessToken).replace("TYPEA", type);
        File file = new File(filePath);
        net.sf.json.JSONObject jsonObject;
        try {
            HttpPostUtil post = new HttpPostUtil(url);
            post.addParameter("media", file);
            String s = post.send();
            jsonObject = net.sf.json.JSONObject.fromObject(s);
            if (jsonObject.containsKey("media_id")) {
                MdlUpload upload=new MdlUpload();
                upload.setMedia_id(jsonObject.getString("media_id"));
                upload.setType(jsonObject.getString("type"));
                upload.setCreated_at(jsonObject.getString("created_at"));
                result.setObj(upload);
                result.setErrmsg("success");
                result.setErrcode("0");
            } else {
                result.setErrmsg(jsonObject.getString("errmsg"));
                result.setErrcode(jsonObject.getString("errcode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrmsg("Upload Exception:"+e.toString());
        }
        return result;
    }

    /**
     * 发送默认欢迎消息
     *
     * @param toUser   【用户ID】
     * @param fromUser 【公众号ID】
     * @return
     */
    private String sendDefaultMsg(final String toUser, final String fromUser) {
        BrandSetting setting = brandSettingService.selectByBrandId(DataSourceTarget.getDataSourceName());
        Brand brand = brandService.selectBrandBySetting(setting.getId());

        String img;
        if (setting.getWechatWelcomeImg().indexOf("http") > -1) {
            img = setting.getWechatWelcomeImg();
        } else {
            img = brand.getWechatImgUrl() + setting.getWechatWelcomeImg();
        }
        Customer c=new Customer();
        c.setWechatId(toUser);
        final Customer customer = newCustomerService.dbSelectOne(getCurrentBrandId(),c);
        String reply = "";
        if(customer == null){
            if(setting.getMemberCardUrl()!=null&&!"".equals(setting.getMemberCardUrl())){
                reply = weChatService.getXmlImageUrl(toUser, fromUser,
                        setting.getWechatWelcomeTitle(),
                        setting.getWechatWelcomeContent(),
                        img,setting.getMemberCardUrl());
            }else{
                reply = weChatService.getXmlImageUrl(toUser, fromUser,
                        setting.getWechatWelcomeTitle(),
                        setting.getWechatWelcomeContent(),
                        img,setting.getWechatWelcomeUrl());
            }
            //setting.getWechatWelcomeUrl());
            return reply;
        }else if (!customer.getIsBindPhone() && customer.getIsShare().equals(Common.YES)
                && StringUtils.isNotBlank(customer.getShareLink()) && !"clearShareLink".equalsIgnoreCase(customer.getShareLink())){
            reply = weChatService.getXmlImageUrl(toUser, fromUser,
                    "感谢关注，点击领取邀请红包",
                    "前往领取>",
                    "http://pic-article.oss-cn-shanghai.aliyuncs.com/ag4sp1lklMiAE9CxAAM8bq3bS9g123_big.png",
                    customer.getShareLink());
            log.info("接收到订阅消息，回复欢迎消息cusReply:" + reply);
        }else {
            if(setting.getMemberCardUrl()!=null&&!"".equals(setting.getMemberCardUrl())){
                reply = weChatService.getXmlImageUrl(toUser, fromUser,
                        setting.getWechatWelcomeTitle(),
                        setting.getWechatWelcomeContent(),
                        img,setting.getMemberCardUrl());
            }else{
                reply = weChatService.getXmlImageUrl(toUser, fromUser,
                        setting.getWechatWelcomeTitle(),
                        setting.getWechatWelcomeContent(),
                        img,setting.getWechatWelcomeUrl());
            }
            //setting.getWechatWelcomeUrl());
            log.info("接收到订阅消息，回复欢迎消息:" + reply);
        }
        /**
         * 判断当前用户是不是进行扫码关注进入的，如果是 推送
         * 您的桌号是103号，上餐前请勿换座，
         * 点击进入[鲁肉范云餐厅]点餐   的推送
         */
        if(customer.getLastTableNumber() != null && !"".equals(customer.getLastTableNumber())){
            customer.setSubscribe(1);
            newCustomerService.dbUpdate(getCurrentBrandId(),customer);

            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(customer.getLastOrderShop());
            final WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            Long usefulTime = new Date().getTime();
            final String url = setting.getWechatWelcomeUrl() + "?shopId=" + shopDetail.getId() + "&tableNumber=" + customer.getLastTableNumber() + "&usefulTime=" + usefulTime;
            final StringBuffer msg = new StringBuffer();
            msg.append("<a href='" + url+ "'>您的桌号是" + customer.getLastTableNumber() + "号，上餐前请勿换座，</a>\n");
            msg.append("<a href='" + url+ "'>点击进入[" + shopDetail.getName() + "]点餐</a>");
//            try {
//                Timer timer=new Timer();//实例化Timer类
//                timer.schedule(new TimerTask(){
//                    public void run(){
//                        try {
//                            String cusReply = weChatService.sendCustomerMsg(msg.toString(),customer.getWechatId(), config.getAppid(), config.getAppsecret());
////                            String cusReply = weChatService.getXmlText(toUser, fromUser, msg.toString());
//                            log.info("接收到订阅消息，回复欢迎消息cusReply:" + cusReply);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        this.cancel();
//                    }
//                },3000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        return reply;
    }

    /**
     * 发送 店铺 排队取号页面的消息	【自助取号】
     *
     * @param toUser      【用户ID】
     * @param fromUser    【公众号ID】
     * @param queueQrcode 【二维码信息】
     * @return
     */
    private String sendQuHaoUrl(String toUser, String fromUser, QueueQrcode queueQrcode) {
        //消息链接
        String msgUrl = "http://" + queueQrcode.getSign() + ".restoplus.cn/geekqueuing"
                + "/waitModel/getQueueJump?id=" + queueQrcode.getId() + "&openId=" + toUser;
        String reply = weChatService.getXmlImageUrl(toUser, fromUser,
                "你有一个等位红包未领取！",
                "点击查看详情",
                "http://pic-article.oss-cn-shanghai.aliyuncs.com/hongbao.png",
                msgUrl);
        log.info("\n【推送取号页面成功】\n shopId：" + queueQrcode.getShopId() + "\n brandId：" + queueQrcode.getBrandId() + " \n");
        return reply;
    }

    /**
     * 扫描进入等位叫号页面推送
     *
     * @param toUser   【用户ID】
     * @param fromUser 【公众号ID】
     * @param obj      【微信发来的自定义参数】
     * @return
     */
    private String sendQueueMsg(String toUser, String fromUser, JSONObject obj) {
        String shopId = obj.getString("QrCodeId");
        ShopDetail shopDetail= shopDetailService.selectByPrimaryKey(shopId);
        Brand brand = brandService.selectByPrimaryKey(shopDetail.getBrandId());
//        WechatConfig config = wechatConfigService.selectByBrandId(shopDetail.getBrandId());
//        //消息链接
//        String msgUrl = "http://" + brand.getBrandSign() + ".restoplus.cn/geekqueuing/waitModel/getQRCodeJump?shopId=" + shopId + "&brandId=" + brand.getId();
//        StringBuffer msg = new StringBuffer();
//        msg.append("欢迎光临"+shopDetail.getName()+"，点击前往<a href='" + msgUrl+ "'>(取号)</a>");
//        String cusReply = weChatService.sendCustomerMsg(msg.toString(),toUser, config.getAppid(), config.getAppsecret());


        String msgUrl = "http://" + brand.getBrandSign() + ".restoplus.cn/geekqueuing/waitModel/getQRCodeJump?shopId=" + shopId + "&brandId=" + brand.getId();
        String cusReply = weChatService.getXmlImageUrl(toUser, fromUser,
                "欢迎光临"+shopDetail.getName()+"，点击图片即可取号",
                "还可以在线点单哦",
                "http://pic-article.oss-cn-shanghai.aliyuncs.com/ag4sp1lXD4WASt27AABNbOfgsh0758.png",
                msgUrl);
        return cusReply;
    }


    /**
     * 从微信 发来的消息中，截取包含的自定义参数
     *
     * @param eventKey
     * @return
     */
    private JSONObject getMsgParam(String eventKey) {
        if (eventKey.startsWith("qrscene_")) {//初次关注，微信发送的参数中，前缀为  qrscene_  需手动截取
            eventKey = eventKey.substring("qrscene_".length(), eventKey.length());
        }
        return new JSONObject(eventKey);
    }

    /**
     * 发送消息,调用微信拉取会员信息接口,更新会员(领取会员卡免注册)
     * @param fromUser
     * @param cardId
     * @param userCardCode
     * @return
     */
    private String updateMemberCardMsg(HttpServletRequest request,String fromUser, String cardId, String userCardCode) {
        log.info("进入领取会员卡更新会员信息");
        log.info("card_id:"+cardId+"code:"+userCardCode);
        String reply = "success";
        String requestURL = request.getRequestURL().toString();
        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
        log.info("brandSign===================:"+brandSign);
        Brand brand = brandService.selectBySign(brandSign);
        DataSourceTarget.setDataSourceName(brand.getId());
        WechatConfig wConfig = brand.getWechatConfig();
        log.info("当前品牌：" + brand.getBrandName());
        Customer customer = customerService.login(brand.getId(),fromUser);
        String accessToken = weChatService.getAccessToken(wConfig.getAppid(), wConfig.getAppsecret());
        if(customer == null){
            String customInfoJson = weChatService.getUserInfoSubscribe(accessToken, fromUser);
            JSONObject cusInfo = new JSONObject(customInfoJson);
            String phone= WeChatCardUtils.getPhone(cardId,userCardCode,accessToken);
            customer = new Customer();
            customer.setWechatId(cusInfo.getString("openid"));
            customer.setNickname(EmojiFilter.filterEmoji(cusInfo.getString("nickname")));
            customer.setSex(cusInfo.getInt("sex"));
            customer.setProvince(cusInfo.getString("province"));
            customer.setCity(cusInfo.getString("city"));
            customer.setCountry(cusInfo.getString("country"));
            customer.setHeadPhoto(cusInfo.getString("headimgurl"));
            customer.setBrandId(brand.getId());
            //customer.setTelephone(phone);
            customer.setCardId(cardId);
            customer.setCode(userCardCode);
            //customer.setIsBindPhone(true);
            Map map = new HashMap(4);
            map.put("brandName", brand.getBrandName());
            map.put("fileName", customer.getId());
            map.put("type", "UserAction");
            map.put("content", "系统向用户:" + customer.getNickname() +"记录领取会员卡日志,手机号：" + phone+"card_id:"+cardId+"code:"+userCardCode + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            try {
                customer = customerService.registerCard(customer);
                customerService.bindPhone(phone, customer.getId(), 0, null, null,null);
                log.info("注册会员信息成功,手机号:"+phone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Customer c=new Customer();
            c.setWechatId(fromUser);
            Customer cus=newCustomerService.dbSelectOne(getCurrentBrandId(),c);
            log.info("会员是否绑定:"+cus.getIsBindPhone()+",card_id:"+cardId+",code:"+userCardCode);
            if(cus.getTelephone()==null||cus.getTelephone().equals("")){
                String phone= WeChatCardUtils.getPhone(cardId,userCardCode,accessToken);
                log.info("手机号:"+phone+"会员id"+cus.getId());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:" + customer.getNickname() +"记录领取会员卡日志,手机号：" + phone+"card_id:"+cardId+"code:"+userCardCode + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                if(phone!=null){
                    customer=new Customer();
                    customer.setId(cus.getId());
                    customer.setCardId(cardId);
                    customer.setCode(userCardCode);
                    try {
                       newCustomerService.dbUpdate(getCurrentBrandId(),customer);
                       customerService.bindPhone(phone, cus.getId(), 0, null, null,null);
                       log.info("更新会员信息成功,该用户手机号为："+phone);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                customer=new Customer();
                customer.setId(cus.getId());
                customer.setCardId(cardId);
                customer.setCode(userCardCode);
                customer.setIsBindPhone(true);
                try {
                    newCustomerService.dbUpdate(getCurrentBrandId(),customer);
                    log.info("更新会员信息成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return reply;
    }

    /**
     * 绑定等位红包
     *
     * @param brandId
     * @param queueId
     * @param opendId
     */
    @RequestMapping("/bindingQueue")
    private void bindingQueue(String brandId, String queueId, String opendId) {
        //通过openid查询
        Customer customer = customerService.login(brandId,opendId);
        //如果当前用户未注册，则创建一条空信息
        if (customer == null) {
            customer = new Customer();
            customer.setWechatId(opendId);
            customer.setBrandId(brandId);
            customer = customerService.register(customer);
        }
        JSONObject obj = new JSONObject(customer);
        log.info("\n【当前用户信息】\n" + obj.toString() + "\n");
        //绑定当前等位红包
        GetNumber getNumber = getNumberService.selectById(queueId);
        JSONObject obj1 = new JSONObject(getNumber);
        log.info("\n【当前红包信息】\n" + obj1.toString() + "\n");
        if (getNumber != null) {
            if (getNumber.getCustomerId() == null || getNumber.getCustomerId() == "") {
                getNumber.setCustomerId(customer.getId());
                getNumberService.update(getNumber);
                log.info("\n【等位红包绑定成功】\n customerId：" + customer.getId() + "\n queueId：" + queueId + " \n");
            } else {
                log.info("\n【等位红包已被绑定】\n customerId：" + getNumber.getCustomerId() + "\n queueId：" + queueId + " \n");
            }
        } else {
            log.info("\n【未查到相关的等位红包】\n  queueId：" + queueId + " \n");
        }
    }


    @RequestMapping("/getLocalIp")
    public String getLocalIp(){
        return MQSetting.getLocalIP();
    }

}
