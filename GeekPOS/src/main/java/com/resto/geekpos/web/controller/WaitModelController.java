package com.resto.geekpos.web.controller;

import com.resto.brand.core.util.*;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.geekpos.web.config.SessionKey;
import com.resto.geekpos.web.constant.DictConstants;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.geekpos.web.websocket.SystemWebSocketHandler;
import com.resto.shop.web.constant.WaitModerState;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.CustomerService;
import com.resto.shop.web.service.GetNumberService;
import com.resto.shop.web.service.QueueQrcodeService;
import com.resto.shop.web.service.TableCodeService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 * Created by KONATA on 2016/10/14.
 */
@Controller
@RequestMapping("waitModel")
public class WaitModelController extends GenericController {
    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandUserService brandUserService;

    @Autowired
    private TableCodeService tableCodeService;

    @Autowired
    private GetNumberService getNumberService;

    @Autowired
    private ShopDetailService shopDetailService;

    @Autowired
    private BrandSettingService brandSettingService;

    @Autowired
    private CustomerService customerService;

    @Resource
    private WechatConfigService wechatConfigService;
    
    @Resource
    private QueueQrcodeService queueQrcodeService;

    @Resource
    private TemplateService templateService;

    @Autowired
    private BrandTemplateEditService brandTemplateEditService;

    @Autowired
    private RedisService redisService;

    @Autowired
    WeChatService weChatService;

    @Value("#{configProperties['jump.host']}")
    private String jumpHost;

    @Value("#{configProperties['offerNumber.host']}")
    private String offerNumberHost;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String userName = queryParams.get("username").toString();
        String passWord = queryParams.get("password").toString();
        if (passWord != null) {
            passWord = ApplicationUtils.pwd(passWord);
        }

        BrandUser brandUser = brandUserService.loginByWaitModel(userName, passWord);

        JSONObject json = new JSONObject();
        if (brandUser != null) {
            String brandId = brandUser.getBrandId();
            Brand brand = brandService.selectByPrimaryKey(brandId);
            ShopDetail shopDetail = shopDetailService.selectById(brandUser.getShopDetailId());
            BrandSetting setting = brandSettingService.selectById(brand.getBrandSettingId());
            if (setting.getWaitRedEnvelope().equals(DictConstants.USE_WAIT_MONEY)
                    && shopDetail.getWaitRedEnvelope().equals(DictConstants.USE_WAIT_MONEY)) { //开启等位红包
                shopDetailService.updateGeekposQueueLoginState(shopDetail.getId(),"signin");   //修改店铺geekpos登录信息
                json.put("code", "200");
                json.put("msg", "请求成功");
                json.put("data", new JSONObject(brandUser));
//                //将当前的用户信息保存起来
//                session.setAttribute(SessionKey.CURRENT_BRAND_ID, brandId);
//                session.setAttribute(SessionKey.CURRENT_SHOP, shopDetail);
//                session.setAttribute(SessionKey.CURRENT_SHOP_ID, shopDetail.getId());
                session.setAttribute(SessionKey.USER_INFO, brandUser);
            } else {
                json.put("code", "100");
                json.put("msg", "未开启等位红包");
            }
        } else {
            json.put("code", "100");
            json.put("msg", "帐号或密码错误");
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value = "/loginout", method = RequestMethod.POST)
    public void loginout(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String shopDetailId = queryParams.get("shopDetailId").toString();
        shopDetailService.updateGeekposQueueLoginState(shopDetailId,"signout");   //修改店铺geekpos退出登录信息

        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value = "/print", method = RequestMethod.POST)
    public void print(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String personNumber = queryParams.get("personNumber").toString();
        String phone = queryParams.get("phone").toString();
        String shopDetailId = queryParams.get("shopDetailId").toString();
        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);

        TableCode tableCode = tableCodeService.selectByPersonNumber(Integer.parseInt(personNumber), shopDetailId);   //获取桌号信息
        if (tableCode == null) {
            net.sf.json.JSONObject json = new net.sf.json.JSONObject();
            json.put("code", "100");
            json.put("msg", "当前取号就餐人数超出最大桌位容量，请重新输入。");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }

        ShopDetail shopDetail = shopDetailService.selectById(shopDetailId);             //获取店铺信息
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brandId);      //获取品牌信息
        List<GetNumber> getNumberList = getNumberService.selectByTableTypeShopId(tableCode.getCodeNumber(), shopDetailId);   //获取等位数

        GetNumber getNumber = new GetNumber();
        String getNumberId = ApplicationUtils.randomUUID();
        getNumber.setId(getNumberId);
        getNumber.setShopDetailId(shopDetailId);
        getNumber.setBrandId(brandId);
        getNumber.setPersonNumber(Integer.parseInt(personNumber));
        getNumber.setPhone(phone);
        getNumber.setCreateTime(new Date());
        getNumber.setTableType(tableCode.getCodeNumber());
        getNumber.setWaitNumber(getNumberService.getWaitNumber(getNumber));

        Integer index = getNumberService.selectCount(getNumber.getTableType(), getNumber.getCreateTime(),shopDetailId) + 1;
        getNumber.setCodeValue(getNumber.getTableType()+ index);
        
        getNumber.setFlowMoney((shopDetail.getBaseMoney().compareTo(new BigDecimal(0)) > 0) ? shopDetail.getBaseMoney() : brandSetting.getBaseMoney());
        getNumber.setHighMoney((shopDetail.getHighMoney().compareTo(new BigDecimal(0)) > 0) ? shopDetail.getHighMoney() : brandSetting.getHighMoney());
        getNumberService.insert(getNumber);

        //将指令发送到Tv端
        sendToTv(getNumber);
        
        GetNumber getNumberInfo = getNumberService.selectById(getNumber.getId());
        getNumberInfo.setCountByTableTpye(index);

//        String requestURL = request.getRequestURL().toString();
//        String imgUrl = requestURL.substring(0, requestURL.indexOf("print"));
//        getNumberInfo.setImgUrl(imgUrl + "getQCode" + "?id=" + getNumber.getId() + "&brandId=" + brandId
//                + "&shopDetailId=" + shopDetailId);
        getNumberInfo.setShopName(shopDetail.getName());
        String imgUrl = getQueueUrl(brandId, getNumberId);
        log.info("\n\n\n"+imgUrl+"\n\n\n");
        getNumberInfo.setImgUrl(imgUrl);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", net.sf.json.JSONObject.fromObject(getNumberInfo));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/WXPrint")
    public void WXPrint(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        String requestURL = request.getRequestURL().toString();
        String URI = request.getRequestURI();
        String jumpURL = null;

        String personNumber = queryParams.get("personNumber").toString();
        String phone = queryParams.get("phone").toString();
        String shopDetailId = queryParams.get("shopDetailId").toString();
        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);
        String customerId = queryParams.get("customerId").toString();

        TableCode tableCode = tableCodeService.selectByPersonNumber(Integer.parseInt(personNumber), shopDetailId);   //获取桌号信息
        if (tableCode == null) {
            net.sf.json.JSONObject json = new net.sf.json.JSONObject();
            json.put("code", "100");
            json.put("msg", "当前取号就餐人数超出最大桌位容量，请重新输入。");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }

        ShopDetail shopDetail = shopDetailService.selectById(shopDetailId);             //获取店铺信息
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brandId);      //获取品牌信息
        List<GetNumber> getNumberList = getNumberService.selectByTableTypeShopId(tableCode.getCodeNumber(), shopDetailId);   //获取等位数

        GetNumber getNumber = new GetNumber();
        String getNumberId = ApplicationUtils.randomUUID();
        getNumber.setId(getNumberId);
        getNumber.setShopDetailId(shopDetailId);
        getNumber.setBrandId(brandId);
        getNumber.setPersonNumber(Integer.parseInt(personNumber));
        getNumber.setPhone(phone);
        getNumber.setCreateTime(new Date());
        getNumber.setTableType(tableCode.getCodeNumber());
        getNumber.setWaitNumber(getNumberList.size());
        getNumber.setFlowMoney((shopDetail.getBaseMoney().compareTo(new BigDecimal(0)) > 0) ? shopDetail.getBaseMoney() : brandSetting.getBaseMoney());
        getNumber.setHighMoney((shopDetail.getHighMoney().compareTo(new BigDecimal(0)) > 0) ? shopDetail.getHighMoney() : brandSetting.getHighMoney());
        Integer index = getNumberService.selectCount(getNumber.getTableType(), getNumber.getCreateTime(), shopDetailId) + 1;
        getNumber.setCodeValue(getNumber.getTableType()+index);
        if(customerId != null){
        	String jump = null;
            GetNumber getNumberCustomer = getNumberService.getWaitInfoByCustomerId(customerId,shopDetailId);       //判断用户当天是否已经有等位排号记录
            if(getNumberCustomer == null || getNumberCustomer.getState() == 2 || getNumberCustomer.getState() == 3){
                getNumber.setCustomerId(customerId);
                getNumberService.insert(getNumber);
                jump = requestURL.substring(0, requestURL.length() - URI.length() + 1) + jumpHost + "?brandId=" + brandId + "&id=" + getNumberId + "&baseUrl=" +
                        requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
            } else{
                jump = requestURL.substring(0, requestURL.length() - URI.length() + 1) + jumpHost + "?brandId=" + brandId + "&id=" + getNumberCustomer.getId() + "&baseUrl=" +
                        requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
            }
            //将指令发送到Tv端
            sendToTv(getNumber);
            response.sendRedirect(jump);
            return;
        }
        getNumberService.insert(getNumber);
        
        GetNumber getNumberInfo = getNumberService.selectById(getNumber.getId());
        getNumberInfo.setCountByTableTpye(index);

//        String requestURL = request.getRequestURL().toString();
//        String imgUrl = requestURL.substring(0, requestURL.indexOf("print"));
//        getNumberInfo.setImgUrl(imgUrl + "getQCode" + "?id=" + getNumber.getId() + "&brandId=" + brandId
//                + "&shopDetailId=" + shopDetailId);
        getNumberInfo.setShopName(shopDetail.getName());
        String imgUrl = getQueueUrl(brandId, getNumberId);
        log.info("\n\n\n"+imgUrl+"\n\n\n");
        getNumberInfo.setImgUrl(imgUrl);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", net.sf.json.JSONObject.fromObject(getNumberInfo));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value = "/getTableList", method = RequestMethod.POST)
    public void getTableList(HttpServletRequest request, HttpServletResponse response) {

        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String shopDetailId = queryParams.get("shopDetailId").toString();
        String brandId = queryParams.get("brandId").toString();

        DataSourceTarget.setDataSourceName(brandId);
        List<TableCode> tableCodes = tableCodeService.getTableList(shopDetailId);
        int sum = 0;
        for (TableCode tableCode : tableCodes) {
            sum += tableCode.getWaitNumber();
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("totalWaitCount", sum);
        data.put("tableList", tableCodes);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", data);
        ApiUtils.setBackInfo(response, json); // 返回信息设置


    }


    @RequestMapping(value = "/getWaitList", method = RequestMethod.POST)
    public void getWaitList(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String shopDetailId = queryParams.get("shopDetailId").toString();
        String brandId = queryParams.get("brandId").toString();
        String tableType = queryParams.get("tableType").toString();

        DataSourceTarget.setDataSourceName(brandId);
        List<GetNumber> getNumbers = getNumberService.selectByTableTypeShopId(tableType, shopDetailId);
        for (GetNumber getNumber : getNumbers) {
            getNumber.setCountByTableTpye(getNumberService.selectCount(getNumber.getTableType(), getNumber.getCreateTime(),shopDetailId));
        }
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", getNumbers);
        ApiUtils.setBackInfo(response, json); // 返回信息设置

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void update(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String shopDetailId = queryParams.get("shopDetailId").toString();
        String brandId = queryParams.get("brandId").toString();
        String id = queryParams.get("id").toString();
        String state = queryParams.get("state").toString();
        DataSourceTarget.setDataSourceName(brandId);

        GetNumber getNumber = getNumberService.selectById(id);

        getNumber = getNumberService.updateGetNumber(getNumber, Integer.parseInt(state));
        //将指令发送到Tv端
        sendToTv(getNumber);
        
        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
    
    /**
     * 发送指令到Tv端
     * @author lmx
     * @version 创建时间：2016年12月15日 下午4:39:04
     * @param getNumber
     * @return
     */
    public boolean sendToTv(GetNumber getNumber){
    	log.info("【发送指定到Tv】getNumberId："+getNumber.getId());
    	if(getNumber.getState() == null && getNumber.getCallNumber() == null ){//取号(插入时没有设置state,数据库默认值为0)
    		return SystemWebSocketHandler.receiveMsg(getNumber, "quhao");
    	}else if (getNumber.getState() == WaitModerState.WAIT_MODEL_NUMBER_ZERO){//叫号
    		return SystemWebSocketHandler.receiveMsg(getNumber, "jiaohao");
        } else if(getNumber.getState() == WaitModerState.WAIT_MODEL_NUMBER_ONE) {//就餐
    		return SystemWebSocketHandler.receiveMsg(getNumber, "jiucan");
        } else if(getNumber.getState() == WaitModerState.WAIT_MODEL_NUMBER_TWO) {//过号
    		return SystemWebSocketHandler.receiveMsg(getNumber, "guohao");
        }
		return true;
    }


    @RequestMapping(value = "/getWaitState", method = RequestMethod.POST)
    public void getWaitState(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandId = queryParams.get("brandId").toString();
        String id = queryParams.get("id").toString();

        DataSourceTarget.setDataSourceName(brandId);
        GetNumber getNumber = getNumberService.selectById(id);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        if (getNumber == null) {
            json.put("code", "100");
            json.put("msg", "没有找到该桌位信息");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }

        List<GetNumber> getNumberList = getNumberService.selectByTableTypeShopId(getNumber.getTableType(), getNumber.getShopDetailId());   //获取等位数

        Integer waitNumber = getNumberService.getWaitNumber(getNumber);
        getNumber.setCountByTableTpye(getNumberService.selectCount(getNumber.getTableType(), getNumber.getCreateTime(),getNumber.getShopDetailId()));
        Brand brand = brandService.selectById(brandId);
        getNumber.setImgUrl("http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?subpage=tangshi&type=wait&shopId=" + getNumber.getShopDetailId() + "&id=" + id + "&brandId=" + brandId);
        getNumber.setShopName(shopDetailService.selectById(getNumber.getShopDetailId()).getName());

        Customer customer = customerService.selectById(getNumber.getCustomerId());
        int rightShop = 0;
        if (customer != null) {
            if (customer.getLastOrderShop() != null) {
                if (customer.getLastOrderShop().equals(getNumber.getShopDetailId())) {
                    rightShop = 1;
                } else {
                    rightShop = 0;
                }
            } else {
                rightShop = 1;
            }
        }
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("rightShop", rightShop);
        json.put("data", getNumber);
        json.put("waitNumber", waitNumber);
        ApiUtils.setBackInfo(response, json); // 返回信息设置

    }

    @RequestMapping("/getQCode")
    public void getQCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        String requestURL = request.getRequestURL().toString();
        String URI = request.getRequestURI();

        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
        Brand brand = brandService.selectBySign(brandSign);

        String id = queryParams.get("id").toString();
        DataSourceTarget.setDataSourceName(brand.getId());

        String jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + jumpHost + "?brandId=" + brand.getId() + "&id=" + id + "&baseUrl=" +
                requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
        ;  //jump路径计算

        GetNumber getNumber = getNumberService.selectById(id);                                  //查询当前取号记录
        Customer customer = customerService.selectById(getNumber.getCustomerId());              //查询当前用户信息
        WechatConfig config = wechatConfigService.selectByBrandId(brand.getId());
        response.sendRedirect(jumpURL);
    }

    /**
     * 获取排队二维码信息
     * @param brandId
     * @param queueId
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getQueueUrl (String brandId,String queueId){
//    	String brandId = "974b0b1e31dc4b3fb0c3d9a0970d22e4";
//    	String token = "xOni7lmgJ5uVksPQW_OoL66Z8ftsIzLSw52vEWd403ZU60bSy290W_Yg3AXsQcsay27Lk60g7V2sxDoW2fPNtDA_r6Syt0VEJ0bClkKf_weORbnHvc_Rwfb7R6heDtNYDQGjAIALYO";
//    	String brandSign = "kc";
    	
    	String token = getTokenByBrandId(brandId);
    	String brandSign = getBrandSign();
    	JSONObject qrParam = new JSONObject();
    	qrParam.put("id", queueId);
    	qrParam.put("sign", brandSign);
    	
    	String result = weChatService.getParamQrCode(token, qrParam.toString());//二维码的附带参数字符串类型，长度不能超过64
		JSONObject obj = new JSONObject(result);
    	return obj.has("url")?obj.getString("url"):result;
    }
    
    /**
     * 获取当前店铺的自主取号页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/getQueueCodeUrl", method = RequestMethod.POST)
    public void getQueueCodeUrl(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandId = queryParams.get("brandId").toString();
        String shopDetailId = queryParams.get("shopDetailId").toString();
        
        String queueQrCodeId = null;
        
        log.info("\n\n\n【二维码】queueQrCodeId："+queryParams.get("queueQrCodeId")+"\n\n\n");
        if(queryParams.get("queueQrCodeId") != null && queryParams.get("queueQrCodeId") != ""){
        	queueQrCodeId = queryParams.get("queueQrCodeId").toString();
        	log.info("【老二维码】");
        }else{
        	queueQrCodeId = createQueueQrCode(brandId, shopDetailId, null);
        	log.info("【刷新二维码】");
        }
        
    	String token = getTokenByBrandId(brandId);
    	
    	JSONObject qrParam = new JSONObject();
    	qrParam.put("QrCodeId", queueQrCodeId);

    	log.info("\n"+qrParam.toString()+"\n");
    	String result = weChatService.getParamQrCode(token, qrParam.toString());//二维码的附带参数字符串类型，长度不能超过64
    	log.info("\n【微信返回】\n"+result);
    	
		JSONObject obj = new JSONObject(result);
    	net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", obj.has("url")?obj.getString("url"):result);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
    
    
    public String createQueueQrCode(String brandId,String shopId,String createUser){
    	String queueQrCodeId = ApplicationUtils.randomUUID();
    	QueueQrcode queueQrcode = new QueueQrcode();
    	queueQrcode.setId(queueQrCodeId);
    	queueQrcode.setBrandId(brandId);
    	queueQrcode.setShopId(shopId);
    	queueQrcode.setCreateUser(createUser);
    	long createDate = System.currentTimeMillis();
    	queueQrcode.setCreateTime(new Date(createDate));
    	long endDate = createDate += 2 * 60 * 60 * 1000;//两个小时
    	queueQrcode.setEndTime(new Date(endDate));
    	queueQrcode.setSign(getBrandSign());
    	queueQrcode.setType("quhao");//设置当期二维码的类型为 取号操作
    	queueQrcodeService.insert(queueQrcode);
    	return queueQrCodeId;
    }
    
    /**
     * 根据 brandId 获取 token
     * @param brandId
     * @return
     */
    public String getTokenByBrandId(String brandId){
    	WechatConfig wechatConfig = wechatConfigService.selectByBrandId(brandId);
    	String token = weChatService.getAccessToken(wechatConfig.getAppid(), wechatConfig.getAppsecret());
    	return token;
    }

    @RequestMapping("/getQueueInfo")
    public void getQueueInfo(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandSign = getBrandSign();
        Brand brand = brandService.selectBySign(brandSign);
        DataSourceTarget.setDataSourceName(brand.getId());

        String id = queryParams.get("id").toString();
        QueueQrcode queueQrcode = queueQrcodeService.selectById(id);

        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", new JSONObject(queueQrcode));
        ApiUtils.setBackInfo(response, json); // 返回信息设置

    }

    /**
     * 1.1版用户优先获取二维码后进来取票等位
     * @param request
     * @param response
     */
    @RequestMapping("/getQueueJump")
    public void getQueueJump(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        String requestURL = request.getRequestURL().toString();
        String URI = request.getRequestURI();
        String jumpURL = null;

        String id = queryParams.get("id").toString();
        String openId = queryParams.get("openId").toString();
        QueueQrcode queueQrcode = queueQrcodeService.selectById(id);          //查询当前id二维码信息
        DataSourceTarget.setDataSourceName(queueQrcode.getBrandId());
        Brand brand = brandService.selectById(queueQrcode.getBrandId());
        WechatConfig wConfig = brand.getWechatConfig();
        //先判断此用户当天是否存在等位信息
        Customer customer = customerService.selectByOpenIdInfo(openId);
        ShopDetail shop = shopDetailService.selectById(queueQrcode.getShopId());

        if(shop.getGeekposQueueIslogin() == 0){
            jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + offerNumberHost + "?id=" + id + "&customerId=" + customer.getId() + "&colse=colse&baseUrl=" +
                    requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
            response.sendRedirect(jumpURL);
            return;
        }
        QueueQrcode isqq = queueQrcodeService.selectByIdEndtime(id);          //判断当前id二维码信息是否有效
        GetNumber getNumber = getNumberService.getWaitInfoByCustomerId(customer.getId(),queueQrcode.getShopId());
        if(isqq == null && (getNumber.getState() == 3 || getNumber.getState() == 2)){
            jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + offerNumberHost+ "?id=" + id + "&customerId=" + customer.getId() + "&colse=colse&baseUrl=" +
                    requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
            response.sendRedirect(jumpURL);
            return;
        }else if(isqq == null && (getNumber.getState() == 0 || getNumber.getState() == 1)){
            jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + jumpHost + "?brandId=" + queueQrcode.getBrandId() + "&id=" + getNumber.getId() + "&baseUrl=" +
                    requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
            response.sendRedirect(jumpURL);
            return;
        }

        if(getNumber == null){     //如果这个人没取号记录
            jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + offerNumberHost + "?id=" + id + "&customerId=" + customer.getId() + "&baseUrl=" +
                    requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
            response.sendRedirect(jumpURL);
            return;
        }else{
            if(getNumber.getState() == 2 || getNumber.getState() == 3){
                jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + offerNumberHost + "?id=" + id + "&customerId=" + customer.getId() + "&baseUrl=" +
                        requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
                response.sendRedirect(jumpURL);
                return;
            }else{
                jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + jumpHost + "?brandId=" + queueQrcode.getBrandId() + "&id=" + getNumber.getId() + "&baseUrl=" +
                        requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
                response.sendRedirect(jumpURL);
                return;
            }
        }
    }



    // ---------------------------------------- 以下是geekpos2.0最新的链接 ----------------------------------

    /**
     * 2.0版用户优扫码 在进行取号
     * @param request
     * @param response
     */
    @RequestMapping("/getQRCodeJump")
    public void getQRCodeJump(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        String requestURL = request.getRequestURL().toString();
        String URI = request.getRequestURI();
        String jumpURL = null;

        String shopId = queryParams.get("shopId").toString();

        //先判断此用户当天是否存在等位信息
        Customer customer = getCurrentCustomer();
        ShopDetail shop = shopDetailService.selectById(shopId);
        BrandSetting brandSetting = brandSettingService.selectByBrandId(shop.getBrandId());

        GetNumber getNumber = getNumberService.getWaitInfoByCustomerId(customer.getId(),shopId);

        if(brandSetting.getWaitRedEnvelope() == 1 && shop.getWaitRedEnvelope() == 1){
            if(getNumber == null){     //如果这个人没取号记录
                jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + offerNumberHost+ "?brandId=" + shop.getBrandId() + "&shopId="+shopId+"&customerId="+customer.getId()+"&baseUrl=" +
                        requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
                response.sendRedirect(jumpURL);
                return;
            }else{
                if(getNumber.getState() == 2 || getNumber.getState() == 3){
                    jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + offerNumberHost + "?brandId=" + shop.getBrandId() + "&shopId="+shopId+"&customerId="+customer.getId()+"&baseUrl=" +
                            requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
                    response.sendRedirect(jumpURL);
                    return;
                }else{
                    jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + jumpHost + "?brandId=" + shop.getBrandId() + "&shopId="+shopId+"&id=" + getNumber.getId() + "&baseUrl=" +
                            requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
                    response.sendRedirect(jumpURL);
                    return;
                }
            }
        }else{
            jumpURL = requestURL.substring(0, requestURL.length() - URI.length() + 1) + jumpHost + "?brandId=" + shop.getBrandId() + "&shopId="+shopId+"&baseUrl=" +
                    requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
            response.sendRedirect(jumpURL);
            return;
        }
    }

    @RequestMapping("/addGetNumber")
    public void addGetNumber(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        String requestURL = request.getRequestURL().toString();
        String URI = request.getRequestURI();
        String jumpURL = null;

        String personNumber = queryParams.get("personNumber").toString();
        String shopDetailId = queryParams.get("shopDetailId").toString();
        String customerId = queryParams.get("customerId").toString();

        Customer customer = customerService.selectById(customerId);

        TableCode tableCode = tableCodeService.selectByPersonNumber(Integer.parseInt(personNumber), shopDetailId);   //获取桌号信息
        if (tableCode == null) {
            net.sf.json.JSONObject json = new net.sf.json.JSONObject();
            json.put("code", "100");
            json.put("msg", "当前取号就餐人数超出最大桌位容量，请重新输入。");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
            return;
        }

        ShopDetail shopDetail = shopDetailService.selectById(shopDetailId);             //获取店铺信息
        WechatConfig config = wechatConfigService.selectByBrandId(shopDetail.getBrandId());
        List<GetNumber> getNumberList = getNumberService.selectByTableTypeShopId(tableCode.getCodeNumber(), shopDetailId);   //获取等位数

        GetNumber getNumber = new GetNumber();
        String getNumberId = ApplicationUtils.randomUUID();
        getNumber.setId(getNumberId);
        getNumber.setShopDetailId(shopDetailId);
        getNumber.setBrandId(getCurrentBrandId());
        getNumber.setPersonNumber(Integer.parseInt(personNumber));
        getNumber.setPhone("");
        getNumber.setCreateTime(new Date());
        getNumber.setTableType(tableCode.getCodeNumber());
        getNumber.setWaitNumber(getNumberList.size());
        getNumber.setFlowMoney(new BigDecimal(0));
        getNumber.setHighMoney(new BigDecimal(0));
        getNumber.setCodeId(tableCode.getId());
        Integer index = getNumberService.selectCount(getNumber.getTableType(), getNumber.getCreateTime(), shopDetailId) + 1;
        if(index < 10 && index > 0){
            getNumber.setCodeValue(getNumber.getTableType() + "0" +index);
        }else{
            getNumber.setCodeValue(getNumber.getTableType()+index);
        }
        if(customerId != null){
            String jump = null;
            GetNumber getNumberCustomer = getNumberService.getWaitInfoByCustomerId(customerId,shopDetailId);       //判断用户当天是否已经有等位排号记录
            if(getNumberCustomer == null || getNumberCustomer.getState() == 2 || getNumberCustomer.getState() == 3){
                getNumber.setCustomerId(customerId);
                if (!redisService.setnxTime(shopDetailId +getNumber.getCodeValue(), 1, 30)) {
                    net.sf.json.JSONObject json = new net.sf.json.JSONObject();
                    json.put("code", "100");
                    json.put("msg", "取号失败，请重新取号！");
                    ApiUtils.setBackInfo(response, json); // 返回信息设置
                    return;
                }
                getNumberService.insertGetNumber(getNumber);
                //推送获取了等位
                BrandSetting setting = brandSettingService.selectByBrandId(customer.getBrandId());
                if(setting.getTemplateEdition()==0){
                    StringBuffer msg = new StringBuffer();
                    String url = requestURL.substring(0, requestURL.length() - URI.length() + 1) + "geekqueuing/waitModel/getQRCodeJump?shopId=" + shopDetailId;
                    msg.append("取号成功！您的等位号是"+getNumber.getCodeValue()+"，前方还有"+getNumberList.size()+"桌在等待，我们会尽快为您安排入座！<a href='" + url + "'>查看详情</a>");
                    weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                }else{
                    List<TemplateFlow> templateFlowList=templateService.selectTemplateId(config.getAppid(),"OPENTM206094658");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM206094658", TemplateSytle.GET_NUMBER_SUCCESS);

                    if(templateFlowList!=null&&templateFlowList.size()!=0){
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String jumpUrl ="";
                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();

                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                first.put("value", brandTemplateEdit.getStartSign());
                            }else {
                                first.put("value", "您好，取号成功！");
                            }
                        }else {
                            first.put("value", "您好，取号成功！");
                        }

                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", shopDetail.getName());
                        keyword1.put("color", "#000000");
                        GetNumber getNumber_ = getNumberService.selectGetNumberInfo(getNumber.getId());
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", getNumber_.getCodeValue());
                        keyword2.put("color", "#000000");
                        //获取此getNumber取号单前方还有多少位等位桌数
                        //List<GetNumber> getNumberList = getNumberService.selectBeforeNumberByCodeId(shop.getId(), getNumber.getCodeId(), getNumber.getCreateTime());
                        Map<String, Object> keyword3 = new HashMap<String, Object>();
                        keyword3.put("value", getNumberList.size());
                        keyword3.put("color", "#000000");
                        Map<String, Object> keyword4 = new HashMap<String, Object>();
                        keyword4.put("value", "--");
                        keyword4.put("color", "#000000");
                        Map<String, Object> keyword5 = new HashMap<String, Object>();
                        keyword5.put("value", "排队中");
                        keyword5.put("color", "#000000");
                        Map<String, Object> remark = new HashMap<String, Object>();

                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                remark.put("value", brandTemplateEdit.getEndSign());
                            }else {
                                remark.put("value", "我们会尽快为您安排入座！");
                            }
                        }else {
                            remark.put("value", "我们会尽快为您安排入座！");
                        }

                        remark.put("color", "#173177");
                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("keyword3", keyword3);
                        content.put("keyword4", keyword4);
                        content.put("keyword5", keyword5);
                        content.put("remark", remark);
                        String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                         //发送短信
                        if(setting.getMessageSwitch()==1){
                            com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();//SMS_109365264
                            com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_109365264");
                        }
                    }else{
                        Brand brand = brandService.selectById(customer.getBrandId());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
                }
                jump = requestURL.substring(0, requestURL.length() - URI.length() + 1) + jumpHost + "?&type=getNumberSuccess&brandId=" + getCurrentBrandId() + "&shopId=" + shopDetailId + "&id=" + getNumberId + "&baseUrl=" +
                        requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
            } else{
                jump = requestURL.substring(0, requestURL.length() - URI.length() + 1) + jumpHost + "?brandId=" + getCurrentBrandId() + "&shopId=" + shopDetailId + "&id=" + getNumberCustomer.getId() + "&baseUrl=" +
                        requestURL.substring(0, requestURL.length() - URI.length() + 1).substring(0, requestURL.substring(0, requestURL.length() - URI.length() + 1).length() - 1);
            }
            //将指令发送到Tv端
//            sendToTv(getNumber);
            response.sendRedirect(jump);
            return;
        }
        getNumberService.insert(getNumber);

        GetNumber getNumberInfo = getNumberService.selectById(getNumber.getId());
        getNumberInfo.setCountByTableTpye(index);

        getNumberInfo.setShopName(shopDetail.getName());
        String imgUrl = getQueueUrl(getCurrentBrandId(), getNumberId);
        log.info("\n\n\n"+imgUrl+"\n\n\n");
        getNumberInfo.setImgUrl(imgUrl);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", net.sf.json.JSONObject.fromObject(getNumberInfo));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    /**
     * 获取当前店铺的自主取号页面
     * @param request
     * @return
     */
    @RequestMapping("/getGeekUrl")
    public void getGeekUrl(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        String token = getTokenByBrandId(getCurrentBrandId());
        String shopId = queryParams.get("shopId").toString();

        JSONObject qrParam = new JSONObject();
        qrParam.put("QrCodeId", 123);
        qrParam.put("brandId", getCurrentBrandId());
        qrParam.put("shopId", shopId);

        String result = weChatService.getParamQrCode(token, qrParam.toString());//二维码的附带参数字符串类型，长度不能超过64

        JSONObject obj = new JSONObject(result);
        String img = obj.has("ticket")?obj.getString("ticket"):"";

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", weChatService.showQrcode(img));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/getNumberInfo")
    public void getNumberInfo(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandSign = getBrandSign();
        Brand brand = brandService.selectBySign(brandSign);
        DataSourceTarget.setDataSourceName(brand.getId());

        String id = queryParams.get("id").toString();
        GetNumber getNumber = getNumberService.selectGetNumberInfo(id);
        getNumber.setCountByTableTpye(getNumberService.selectCount(getNumber.getTableType(), getNumber.getCreateTime(), getNumber.getShopDetailId()));
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", new net.sf.json.JSONObject().fromObject(getNumber));
        ApiUtils.setBackInfo(response, json); // 返回信息设置

    }

    @RequestMapping("/getWaitCountByCodeId")
    public void getWaitCountByCodeId(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandSign = getBrandSign();
        Brand brand = brandService.selectBySign(brandSign);
        DataSourceTarget.setDataSourceName(brand.getId());

        String codeId = queryParams.get("codeId").toString();
        String shopId = queryParams.get("shopId").toString();
        Integer count = getNumberService.selectWaitCountByCodeId(shopId,codeId);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", count);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/getBeforeNumberByCodeId")
    public void getBeforeNumberByCodeId(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandSign = getBrandSign();
        Brand brand = brandService.selectBySign(brandSign);
        DataSourceTarget.setDataSourceName(brand.getId());

        String id = queryParams.get("id").toString();
        String shopId = queryParams.get("shopId").toString();
        GetNumber getNumber = getNumberService.selectGetNumberInfo(id);
        //获取此getNumber取号单前方还有多少位等位桌数
        List<GetNumber> getNumberList = getNumberService.selectBeforeNumberByCodeId(shopId, getNumber.getCodeId(), getNumber.getCreateTime());
        //获取此getNumber等位桌子类型的当前叫号getNumber
        GetNumber nowGetNmber = getNumberService.selectNowNumberByCodeId(shopId, getNumber.getCodeId());

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("count", getNumberList.size());
        json.put("nowGetNmber", new net.sf.json.JSONObject().fromObject(nowGetNmber));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}
