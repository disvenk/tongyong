package com.resto.wechat.web.controller;

import com.alibaba.fastjson.JSON;
import com.google.zxing.WriterException;
import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.api.customer.service.NewCustomerDetailService;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.*;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.WeChatService;
import com.resto.brand.web.service.WechatConfigService;
import com.resto.shop.web.constant.*;
import com.resto.shop.web.dto.ShareMoneyDto;
import com.resto.shop.web.exception.AppException;
import com.resto.api.appraise.entity.Appraise;
import com.resto.shop.web.model.*;
import com.resto.shop.web.model.Account;
import com.resto.shop.web.service.*;
import com.resto.api.customer.entity.Customer;
import com.resto.api.customer.entity.CustomerDetail;
import com.resto.wechat.web.config.SessionKey;
import com.resto.wechat.web.rpcinterceptors.DataSourceTarget;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.MobilePackageBean;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@RestController
@RequestMapping("customer")
public class CustomerController extends GenericController {

    @Resource
    CouponService couponService;

    @Resource
    NewCustomerService newCustomerService;

    @Resource
    CustomerService customerService;

    @Resource
    private AccountService accountService;

    @Resource
    SmsLogService smsLogService;

    @Resource
    NewCustomCouponService newcustomcouponService;

    @Autowired
    GetNumberService getNumberService;

    @Autowired
    OrderService orderService;

    @Autowired
    NewCustomerDetailService newCustomerDetailService;

    @Autowired
    AccountLogService accountLogService;

    @Autowired
    WechatConfigService wechatConfigService;

    @Resource
    NewCustomCouponService newCustomCouponService;

    @Resource
    NewAppraiseService newAppraiseService;

    @Resource
    BrandSettingService brandSettingService;

    @Resource
    RedPacketService redPacketService;

    @Resource
    com.resto.brand.web.service.TableQrcodeService tableQrcodeService;

    @Resource
    MemberActivityThingService memberActivityThingService;

    @Resource
    MemberActivityService memberActivityService;

    @Autowired
    ChargeOrderService chargeOrderService;

    @Autowired
    WeChatService weChatService;

    /**
     * 接受参数 isUsed ,distributionModeId
     *
     * @param coupon
     * @return
     */
    @RequestMapping("listCoupon")
    public Result listCoupon(Coupon coupon) {
        coupon.setCustomerId(getCurrentCustomerId());
        List<Coupon> list = couponService.listCoupon(coupon, getCurrentBrandId(), getCurrentShopId());
        return getSuccessResult(list);
    }



    @RequestMapping("/new/listCoupon")
    public void listCouponNew(Coupon coupon, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        List<Coupon> listall = new ArrayList<>();
        if(brandSetting!=null){
            int cd=brandSetting.getCouponCD();
            if(cd!=0){
                coupon.setCustomerId(getCurrentCustomerId());
                coupon.setBrandId(getCurrentBrandId());
                coupon.setShopDetailId(getCurrentShopId());
                List<Coupon> list = couponService.listCouponUsed(coupon);
                if(list!=null && !list.isEmpty()){
                    //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date nowtime = new Date();
                    Date usingtime = list.get(0).getUsingTime();
                    long diff=nowtime.getTime()-usingtime.getTime();
                    int hours = (int) (diff/(1000 * 60 * 60));
                    if(hours>=cd){
                        coupon.setCustomerId(getCurrentCustomerId());
                        listall = couponService.listCoupon(coupon, getCurrentBrandId(), getCurrentShopId());
                        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
                        json.put("statusCode", "0");
                        json.put("success", true);
                        json.put("message", "请求成功");
                        json.put("data", listall);
                        ApiUtils.setBackInfo(response, json); // 返回信息设置
                    }
                }else{
                    coupon.setCustomerId(getCurrentCustomerId());
                    listall = couponService.listCoupon(coupon, getCurrentBrandId(), getCurrentShopId());
                    net.sf.json.JSONObject json = new net.sf.json.JSONObject();
                    json.put("statusCode", "0");
                    json.put("success", true);
                    json.put("message", "请求成功");
                    json.put("data", listall);
                    ApiUtils.setBackInfo(response, json); // 返回信息设置
                }
            }else{
                coupon.setCustomerId(getCurrentCustomerId());
                listall = couponService.listCoupon(coupon, getCurrentBrandId(), getCurrentShopId());
                net.sf.json.JSONObject json = new net.sf.json.JSONObject();
                json.put("statusCode", "0");
                json.put("success", true);
                json.put("message", "请求成功");
                json.put("data", listall);
                ApiUtils.setBackInfo(response, json); // 返回信息设置
            }
        }
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "0");
        json.put("success", false);
        json.put("message", "请求成功");
        json.put("data", listall);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("info")
    public Result customerInfo() {
        Customer cus = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),getCurrentCustomerId());
        getSession().setAttribute(SessionKey.CURRENT_CUSTOMER, cus);
        return getSuccessResult(cus);
    }

    @RequestMapping("/new/info")
    public void customerInfoNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        WechatConfig wConfig = wechatConfigService.selectByBrandId(getCurrentBrandId());
        Customer cus = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),getCurrentCustomerId());
        Account account = accountService.selectById(cus.getAccountId());
        cus.setFrozenRemain(account.getFrozenRemain());
        cus.setRemain(account.getRemain());
        cus.setSumRemain(account.getRemain().add(account.getFrozenRemain()));
        //重新刷新用户是否关注
        String accessToken = weChatService.getAccessToken(wConfig.getAppid(), wConfig.getAppsecret());
        String customInfoJson = weChatService.getUserInfoSubscribe(accessToken, cus.getWechatId());
        JSONObject cusInfo = new JSONObject(customInfoJson);
        cus.setSubscribe(cusInfo.getInt("subscribe"));

        if (cus.getCustomerDetailId() != null) {
            cus.setCustomerDetail(newCustomerDetailService.dbSelectByPrimaryKey(getCurrentBrandId(),cus.getCustomerDetailId()));
        } else {
            String uuid = ApplicationUtils.randomUUID();
            cus.setCustomerDetailId(uuid);
            newCustomerService.dbUpdate(getCurrentBrandId(),cus);
            CustomerDetail customerDetail = new CustomerDetail();
            customerDetail.setId(uuid);
            newCustomerDetailService.dbSave(getCurrentBrandId(),customerDetail);
            cus.setCustomerDetail(customerDetail);
        }
        getSession().setAttribute(SessionKey.CURRENT_CUSTOMER, cus);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "0");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", new net.sf.json.JSONObject().fromObject(cus));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

//    @RequestMapping("sendCodeMsg")
//    public TxSmsResult sendCodeMsg(String phone, HttpSession session) {
//        TxSmsResult r = new TxSmsResult(false);
//        if (StringUtils.isBlank(phone)) {
//            r.setMessage("请输入手机号码！");
//            return r;
//        }
//        String code = RandomStringUtils.randomNumeric(4);
//
//        List<Customer> customerList = customerService.selectListByBrandId(getCurrentBrandId());
//
//        StringBuffer tels = new StringBuffer();
//        for (Customer customer : customerList) {
//            tels.append(customer.getTelephone() + ",");
//        }
//        if (!tels.toString().contains(phone)) {
//            String result = smsLogService.sendCode(phone, code, getCurrentBrandId(), getCurrentShopId(), SmsLogType.AUTO_CODE);
//            JSONObject resultObj = new JSONObject(result);
//            if (!resultObj.optBoolean("success")) {
//                r.setMessage(resultObj.optString("msg"));
//                log.error("发送短信失败:" + result);
//                return r;
//            }
//            session.setAttribute("phone-code-" + phone, code);
//            log.info("发送短信成功:" + code);
//            return new TxSmsResult(true);
//        } else {
//            r.setMessage("该手机号已存在！");
//            return r;
//        }
//    }

    @RequestMapping("/new/sendCodeMsg")
    public void sendCodeMsgNew(String phone, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(phone)) {
            json.put("success", false);
            json.put("message", "请输入手机号码！");
            ApiUtils.setBackInfo(response, json);
        }else{
            String code = RandomStringUtils.randomNumeric(4);
            List<String> telephones = newCustomerService.selectTelephoneList(getCurrentBrandId());
            int count = 0;
            for (int i = 0; i < phone.length(); i++) {
                if (phone.charAt(i) == '.') {
                    count++;
                }
            }
            if (count > 1 || phone.length() != 11) {
                json.put("success", false);
                json.put("message", "号码格式错误");
                ApiUtils.setBackInfo(response, json);
            }else if (!telephones.contains(phone)) {
                json.put("success", true);
                json.put("message", "手机号符合要求");
                json.put("rcode", code);
                ApiUtils.setBackInfo(response, json);
            } else {
                json.put("success", false);
                json.put("message", "该手机号已存在");
                ApiUtils.setBackInfo(response, json);
            }
        }
    }

    @RequestMapping("/new/sendAliCodeMsgNew")
    public void sendAliCodeMsgNew(String phone, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        JSONObject json = new JSONObject();
        String code = RandomStringUtils.randomNumeric(4);
        String nickName ="";
        if(getCurrentCustomer()!=null){
            nickName=getCurrentCustomer().getNickname();
        }

		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		jsonObject.put("code",code);
		jsonObject.put("product",getCurrentBrand().getBrandName());
		com.alibaba.fastjson.JSONObject result = smsLogService.sendMessage(getCurrentBrandId(),getCurrentShopId(),SmsLogType.AUTO_CODE,SMSUtils.SIGN,SMSUtils.CODE_SMS_TEMP,phone,jsonObject);

        if (!result.getBoolean("success")) {
            json.put("success", false);
            json.put("message", result.getString("msg"));
            log.error("发送短信失败:" + result);
            ApiUtils.setBackInfo(response, json);
        }
        session.setAttribute("phone-code-" + phone, code);
		log.info("发送短信成功:" + code);
		json.put("success", true);
		json.put("message", "发送短信成功");
		json.put("rcode", code);
		ApiUtils.setBackInfo(response, json);
    }
    @RequestMapping("editPhone")
    public Result editPhone(String phone, String code, Integer couponType, String shareCustomer,String shareOrderId, HttpSession session) throws AppException {
        String vailCode = (String) session.getAttribute("phone-code-" + phone);
        Result result = new Result();
        if (vailCode == null || !vailCode.equals(code)) {
            result.setSuccess(false);
            result.setMessage("验证码错误!");
        } else {
            customerService.bindPhone(phone, getCurrentCustomerId(), couponType, getCurrentShopId(), shareCustomer,shareOrderId);
            result.setSuccess(true);
        }
        return result;
    }

    @RequestMapping("/new/editPhone")
    public void editPhoneNew(HttpServletRequest request, HttpServletResponse response) throws AppException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        String phone = String.valueOf(queryParams.get("phone"));
        String code = String.valueOf(queryParams.get("code"));
        String rcode = String.valueOf(queryParams.get("rcode"));
        Integer couponType = Integer.valueOf(String.valueOf(queryParams.get("couponType")));
        String shareCustomer = String.valueOf(queryParams.get("shareCustomer"));
        String shareOrderId = String.valueOf(queryParams.get("shareOrderId"));
        JSONObject json = new JSONObject();
        if (rcode == null || !rcode.equals(code)) {
            json.put("success", false);
            json.put("message", "验证码错误！");
            ApiUtils.setBackInfo(response, json);
        } else {
            Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),getCurrentCustomerId());
            if (customer.getIsBindPhone() == false && customer.getTelephone() == null) {
                customerService.bindPhone(phone, getCurrentCustomerId(), couponType, getCurrentShopId(), shareCustomer,shareOrderId);
               // customer = customerService.selectById(getCurrentCustomerId());
                request.getSession().setAttribute(SessionKey.CURRENT_CUSTOMER, customer);
                Map map = new HashMap(4);
                map.put("brandName", getCurrentBrand().getBrandName());
                map.put("fileName", getCurrentCustomerId());
                map.put("type", "UserAction");
                map.put("content", "用户:"+getCurrentCustomer().getNickname()+"进行了注册,请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            }
            //推送消息模板
            json.put("success", true);
            ApiUtils.setBackInfo(response, json);
        }
    }

    @RequestMapping("informationAccount")
    public ModelAndView informationAccount() {
        ModelAndView mv = new ModelAndView();
        Account account = accountService.selectAccountAndLogByCustomerId(getCurrentCustomerId());
        mv.addObject("data", account);
        mv.setViewName("informationAccount");
        return mv;
    }

    @RequestMapping("informationAccountAjax")
    public void informationAccountAjax(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        Account account = accountService.selectAccountAndLogByCustomerId(getCurrentCustomerId());

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "0");
        json.put("success", true);
        json.put("message", "请求成功");
        if (account != null) {
            json.put("data", new net.sf.json.JSONObject().fromObject(account));
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("informationCoupon")
    public ModelAndView informationCoupon() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("informationCoupon");
        return mv;
    }

    @RequestMapping("listCouponByStatus")
    public Result listCouponByStatus(String status) {
        return getSuccessResult(couponService.listCouponByStatus(status, getCurrentCustomerId(), getCurrentBrandId(), getCurrentShopId()));
    }

    @RequestMapping("/new/listCouponByStatus")
    public void listCouponByStatusNew(String status, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        List<Coupon> coupons = couponService.listCouponByStatus(status, getCurrentCustomerId(), getCurrentBrandId(), getCurrentShopId());
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "0");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", coupons);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    /**
     * 解绑手机号码
     */

    @RequestMapping("unBindPhone")
    public Result unbindPhone() {
        Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),getCurrentCustomerId());
        customer.setTelephone(null);
        newCustomerService.dbUpdate(getCurrentBrandId(),customer);
        return Result.getSuccess();
    }

    @RequestMapping("updateNewNoticeTime")
    public Result updateNewNoticeTime() {
        newCustomerService.updateNewNoticeTime(getCurrentBrandId(),getCurrentCustomerId());
        return Result.getSuccess();
    }

    @RequestMapping("/checkRegistered")
    public Result checkRegistered() {
        return new Result(newCustomerService.checkRegistered(getCurrentBrandId(),getCurrentCustomerId()));
    }

    @RequestMapping("/new/checkRegistered")
    public void checkRegisteredNew(String status, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("success", newCustomerService.checkRegistered(getCurrentBrandId(),getCurrentCustomerId()));
        json.put("message", "");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    @RequestMapping("/list_all")
    public Result listData() {
        return new JSONResult<>(newcustomcouponService.selectListByBrandId(getCurrentBrandId(),getCurrentShopId()));
    }

    @RequestMapping("/new/list_all")
    public void listDataNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        List<NewCustomCoupon> ncclist = newcustomcouponService.selectListByBrandId(getCurrentBrandId(),getCurrentShopId());

        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("success", true);
        json.put("message", "");
        json.put("data", ncclist);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/showCoupon")
    public Result showCoupon(Integer couponType) {
        return new JSONResult<>(newcustomcouponService.selectListByCouponType(getCurrentBrandId(), couponType, getCurrentShopId()));
    }

    @RequestMapping("/new/showCoupon")
    public void showCouponNew(Integer couponType, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        List<NewCustomCoupon> coupons = newcustomcouponService.selectListByCouponType(getCurrentBrandId(), couponType, getCurrentShopId());
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "");
        json.put("data", coupons);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/useCoupon")
    public void useCoupon(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String orderId = queryParams.get("orderId").toString();
        String couponId = queryParams.get("couponId").toString();

        couponService.useCouponById(orderId, couponId);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/getWaitInfo")
    public void getWaitInfo(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        GetNumber getNumber = getNumberService.getWaitInfoByCustomerId(getCurrentCustomerId(), getCurrentShopId());
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        if (getNumber != null) {
            if (getNumber.getShopDetailId().equals(getCurrentShopId())) {
                json.put("statusCode", "200");
                json.put("success", true);
                json.put("message", "请求成功");
                json.put("data", getNumber);
            } else {
                json.put("statusCode", "100");
                json.put("success", false);
                json.put("message", "请求失败");
            }
        } else {
            json.put("statusCode", "100");
            json.put("success", false);
            json.put("message", "请求失败");
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/lastOrderByCustomer")
    public void lastOrderByCustomer(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String groupId = queryParams.get("groupId").toString();
        String tableNumber = queryParams.get("tableNumber").toString();
        String customerId = queryParams.get("customerId").toString();
        String shopId = queryParams.get("shopId").toString();
        if(groupId == null || "".equals(groupId)){
            Order order = orderService.lastOrderByCustomer(customerId, shopId,tableNumber);

            JSONObject json = new JSONObject();
            json.put("statusCode", "200");
            json.put("success", true);
            json.put("message", "请求成功1");
            if (order != null) {
                //微信支付 杀进程 后 加菜   优先还给他钱
                if(order.getOrderState() == OrderState.SUBMIT && order.getPayType() == PayType.NOPAY && order.getPayMode() == OrderPayMode.WX_PAY && order.getPaymentAmount() != order.getOrderMoney()){
                    orderService.refundPaymentByUnfinishedOrder(order.getId());
                }
                log.error("测试order是否存在" + new JSONObject(order).toString());
                json.put("data", new JSONObject(order));
            }
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }else{
            Order order = orderService.lastOrderByCustomerGroupId(customerId, shopId,groupId,tableNumber);
            JSONObject json = new JSONObject();
            json.put("statusCode", "200");
            json.put("success", true);
            json.put("message", "请求成功2");
            if (order != null) {
                //微信支付 杀进程 后 加菜   优先还给他钱
                if(order.getOrderState() == OrderState.SUBMIT && order.getPayType() == PayType.NOPAY && order.getPayMode() == OrderPayMode.WX_PAY && order.getPaymentAmount() != order.getOrderMoney()){
                    orderService.refundPaymentByUnfinishedOrder(order.getId());
                }
                json.put("data", new JSONObject(order));
            }
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }

    }

    @RequestMapping("/new/vCode")
    public void getNewVCode(String baseUrl, HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
        OutputStream out = response.getOutputStream();
//        QRCodeUtil.createQRCode(baseUrl + "/wechat/index?shopId=" + getCurrentShopId() + "&shareCustomer=" + getCurrentCustomerId() + "&df=fx&baseUrl=" + baseUrl + "#tangshi", "png", out);
        QRCodeUtil.createQRCode(baseUrl + "/wechat/index?notIntoShop=1&df=" + getCurrentCustomer().getSerialNumber(), "png", out);
    }

    //修改个人信息
    @RequestMapping("/new/modifyCustomer")
    public void modifyCustomer(String customerId, String content, String field, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),customerId);
        CustomerDetail customerDetail = newCustomerDetailService.dbSelectByPrimaryKey(getCurrentBrandId(),customer.getCustomerDetailId());
        Map map = new HashMap(4);
        map.put("brandName", getCurrentBrand().getBrandName());
        map.put("fileName", getCurrentCustomerId());
        map.put("type", "UserAction");
        if ("vocation".equals(field)) {
            customerDetail.setVocation(content);
            map.put("content", "用户:"+getCurrentCustomer().getNickname()+"将个人信息中的职业修改为:"+content+",请求服务器地址为:" + MQSetting.getLocalIP());
        } else if ("company".equals(field)) {
            customerDetail.setCompany(content);
            map.put("content", "用户:"+getCurrentCustomer().getNickname()+"将个人信息中的公司修改为:"+content+",请求服务器地址为:" + MQSetting.getLocalIP());
        } else if ("school".equals(field)) {
            customerDetail.setSchool(content);
            map.put("content", "用户:"+getCurrentCustomer().getNickname()+"将个人信息中的学校修改为:"+content+",请求服务器地址为:" + MQSetting.getLocalIP());
        } else if ("personalNote".equals(field)) {
            customerDetail.setPersonalNote(content);
            map.put("content", "用户:"+getCurrentCustomer().getNickname()+"将个人信息中的个人说明修改为:"+content+",请求服务器地址为:" + MQSetting.getLocalIP());
        }
        newCustomerDetailService.dbUpdate(getCurrentBrandId(),customerDetail);
        doPostAnsc(map);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/modifyBirthDate")
    public void modifyBirthDate(String customerId, String birthDate, Integer age, String constellation, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        JSONObject json = new JSONObject();
        try {
            Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),customerId);
            CustomerDetail customerDetail = newCustomerDetailService.dbSelectByPrimaryKey(getCurrentBrandId(),customer.getCustomerDetailId());
            customerDetail.setBirthDate(DateUtil.fomatDate(birthDate));
            customerDetail.setAge(age);
            customerDetail.setConstellation(constellation);
            customerDetail.setShopDetailId(getCurrentShopId());
            newCustomerDetailService.dbUpdate(getCurrentBrandId(),customerDetail);
            List<NewCustomCoupon> newCustomCoupons = newCustomCouponService.selectBirthCoupon();
            Integer day = 0;
            for (NewCustomCoupon coupon : newCustomCoupons) {
                if (coupon.getDistanceBirthdayDay() > day) {
                    day = coupon.getDistanceBirthdayDay();
                }
            }
            json.put("data", day);
            Map map = new HashMap(4);
            map.put("brandName", getCurrentBrand().getBrandName());
            map.put("fileName", getCurrentCustomerId());
            map.put("type", "UserAction");
            map.put("content", "用户:"+getCurrentCustomer().getNickname()+"将个人信息中的年龄修改为:"+age+"、生日修改为;"+DateUtil.fomatDate(birthDate)+"、星座修改为:"+constellation+",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
        }catch (Exception e){
            log.error(e.getMessage() + "保存生日信息出错！");
            e.printStackTrace();
        }
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/shareCustomerCount")
    public void shareCustomerCount(String customerId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),customerId);
        Integer count = newCustomerService.selectByShareCustomer(getCurrentBrandId(),customerId);
        BigDecimal sumMoney = accountLogService.selectByShareMoney(customer.getAccountId());
        Integer number = accountLogService.selectByShareMoneyCount(customer.getAccountId());

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("count", count);
        json.put("number", number);
        json.put("sumMoney", sumMoney);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/customerShare")
    public void customerShare(Integer type,String customerId,String shopId,String appraiseId,HttpServletRequest request,HttpServletResponse response){
        AppUtils.unpack(request, response);
        boolean isGetShareCoupon = true;
        List<Coupon> coupons = new ArrayList<>();
        try {
            Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),customerId);
            if (customer.getIsBindPhone() == true) {
                Appraise appraise = newAppraiseService.dbSelectByPrimaryKey(getCurrentBrandId(),appraiseId);
                if (appraise != null) {
                    Order newOrder = new Order();
                    newOrder.setId(appraise.getOrderId());
                    newOrder.setIsGetShareCoupon(Common.YES);
                    orderService.update(newOrder);
                    coupons = newCustomCouponService.giftCoupon(customer, 3, shopId);
                    if (coupons.size() > 0) {
                        BrandSetting setting = brandSettingService.selectByBrandId(appraise.getBrandId());
                        WechatConfig config = wechatConfigService.selectByBrandId(appraise.getBrandId());
                        StringBuffer str = new StringBuffer();
                        BigDecimal value = new BigDecimal(0);
                        for (Coupon coupon : coupons) {
                            value = value.add(coupon.getValue());
                        }
                        Map<String, Object> valueMap = new HashMap<>();
                        valueMap.put("money",value);
                        valueMap.put("weChatWelcomeUrl", setting.getWechatWelcomeUrl());
                        valueMap.put("shopId", appraise.getShopDetailId());
                        String pushMessage = "感谢您的分享！${money}元优惠券已送达您的账户，<a href='${weChatWelcomeUrl}?dialog=myCoupon&subpage=my&shopId=${shopId}'>点击查看~</a>";
                        StrSubstitutor transformation = new StrSubstitutor(valueMap);
                        pushMessage = transformation.replace(pushMessage);
                        weChatService.sendCustomerMsg(pushMessage, customer.getWechatId(), config.getAppid(), config.getAppsecret());
                    } else {
                        isGetShareCoupon = false;
                    }
                    Map map = new HashMap(4);
                    map.put("brandName", getCurrentBrand().getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "用户:" + customer.getNickname() + "进行了分享订单Id:" + appraise.getOrderId() + ",分享对象为:" + (type == 1 ? "朋友圈" : "朋友") + ",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                } else {
                    isGetShareCoupon = false;
                }
            } else {
                isGetShareCoupon = false;
            }
        } catch (Exception e){
            isGetShareCoupon = false;
            e.printStackTrace();
            log.error("发放分享优惠卷出错！");
        }
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("isGetShareCoupon", isGetShareCoupon);
        json.put("couponList",coupons);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/customer")
    public void customer(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Customer cus = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),getCurrentCustomerId());
        Account account = accountService.selectById(cus.getAccountId());
        cus.setAccount(account.getRemain());
        cus.setFrozenRemain(account.getFrozenRemain());
        cus.setRemain(account.getRemain());
        cus.setSumRemain(account.getRemain().add(account.getFrozenRemain()));
        getSession().setAttribute(SessionKey.CURRENT_CUSTOMER, cus);

        if (cus.getCustomerDetailId() != null) {
            cus.setCustomerDetail(newCustomerDetailService.dbSelectByPrimaryKey(getCurrentBrandId(),cus.getCustomerDetailId()));
        } else {
            String uuid = ApplicationUtils.randomUUID();
            cus.setCustomerDetailId(uuid);
            newCustomerService.dbUpdate(getCurrentBrandId(),cus);
            CustomerDetail customerDetail = new CustomerDetail();
            customerDetail.setId(uuid);
            newCustomerDetailService.dbSave(getCurrentBrandId(),customerDetail);
            cus.setCustomerDetail(customerDetail);
        }
        getSession().setAttribute(SessionKey.CURRENT_CUSTOMER, cus);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", new net.sf.json.JSONObject().fromObject(cus));
        json.put("remain", cus.getAccount());
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/updateCustomerShareLink")
    public void updateCustomerShareLink(String shareLinkJson, Boolean isLink ,HttpServletRequest request, HttpServletResponse response){
        AppUtils.unpack(request, response);
        Customer oldCustomer = getCurrentCustomer();
        try {
            if (!oldCustomer.getIsBindPhone()) {
                String shareLink = shareLinkJson.substring(shareLinkJson.indexOf("=") + 1);
                if (isLink == null) {
                    Map<String, String> shareLinkMap = JSON.parseObject(shareLinkJson, Map.class);
                    BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrand().getId());
                    Map<String, Object> valueMap = new HashMap<>();
                    valueMap.put("brandSign", getCurrentBrand().getBrandSign());
                    valueMap.put("httpType", brandSetting.getOpenHttps() == 1 ? "https" : "http");
                    StrSubstitutor sub = new StrSubstitutor(valueMap);
                    shareLink = "${httpType}://${brandSign}.restoplus.cn/wechat/index?";
                    shareLink = sub.replace(shareLink);
                    int i = 0;
                    for (Map.Entry<String, String> maps : shareLinkMap.entrySet()) {
                        shareLink = shareLink.concat(i == 0 ? "" : "&").concat(maps.getKey()).concat("=").concat(maps.getValue());
                        i++;
                    }
                    if (!shareLinkMap.containsKey("df")) {
                        shareLink = shareLink.concat("&allowInviteRegistration=true&shareRegister=true#tangshi");
                    }
                }
                Customer newCustomer = new Customer();
                newCustomer.setId(oldCustomer.getId());
                newCustomer.setShareLink(shareLink);
                newCustomerService.dbUpdate(getCurrentBrandId(),newCustomer);
                oldCustomer.setShareLink(shareLink);
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("发放分享优惠卷出错！");
        }
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("customer", oldCustomer);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/getShareMoneyList")
    public void getShareMoneyList(String customerId, Integer currentPage, Integer showCount, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        List<ShareMoneyDto> list = redPacketService.selectShareMoneyList(customerId, currentPage, showCount);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/getShareCustomerList")
    public void getShareCustomerList(String customerId, Integer currentPage, Integer showCount, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        List<Customer> list = newCustomerService.selectShareCustomerList(getCurrentBrandId(),customerId, currentPage, showCount);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/penScan")
    public void penScan(String customerId, String vv, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        com.resto.brand.web.model.TableQrcode tableQrcode = tableQrcodeService.selectById(Long.parseLong(com.resto.wechat.web.util.Encrypter.decrypt(vv)));
        Map customerMap = new HashMap(4);
        customerMap.put("brandName", getCurrentBrand().getBrandName());
        customerMap.put("fileName", customerId);
        customerMap.put("type", "UserAction");
        customerMap.put("content", "用户:"+customerId+"扫码二维码，tableId为:" + tableQrcode.getId()+",桌号为：" + tableQrcode.getTableNumber() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(customerMap);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/useCheckedAccountLog")
    public void useCheckedAccountLog(boolean type, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Map customerMap = new HashMap(4);
        customerMap.put("brandName", getCurrentBrand().getBrandName());
        customerMap.put("fileName", getCurrentCustomerId());
        customerMap.put("type", "UserAction");
        String flag = type ? "关闭" : "开启";
        customerMap.put("content", "用户:"+getCurrentCustomerId() + flag + "了余额支付开关，请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(customerMap);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/checkCustomerSubscribe")
    public void checkCustomerSubscribe(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        JSONObject json = new JSONObject();
        //重新刷新用户是否关注
//        try {
//            String accessToken = weChatService.getAccessToken(getCurrentBrand().getWechatConfig().getAppid(), getCurrentBrand().getWechatConfig().getAppsecret());
//            String customInfoJson = weChatService.getUserInfoSubscribe(accessToken, getCurrentCustomer().getWechatId());
//            JSONObject cusInfo = new JSONObject(customInfoJson);
//
//            if(cusInfo.getInt("subscribe") == 1){
//                json.put("statusCode", "200");
//                json.put("success", true);
//                json.put("message", "请求成功");
//                json.put("subscribe", 1);
//            }else{
//                json.put("statusCode", "200");
//                json.put("success", true);
//                json.put("message", "请求成功");
//                json.put("subscribe", 0);
//            }
//        } catch (Exception e){
//            json.put("statusCode", "100");
//            json.put("success", false);
//            json.put("message", "请求失败");
//        }
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("subscribe", 1);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    /**
     * 查询注册用户是否存在会员折扣
     * @param telephone
     * @param request
     * @param response
     */
    @RequestMapping("/getMemberActivityByTelephone")
    public void getMemberActivityByTelephone(String telephone, HttpServletRequest request, HttpServletResponse response){
        AppUtils.unpack(request, response);

        MemberActivityThing memberActivityThing = memberActivityThingService.selectByTelephone(telephone);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        if(memberActivityThing != null){
            MemberActivity memberActivity = memberActivityService.selectById(memberActivityThing.getActivityId());
            if(memberActivity.getType() == 1){
                json.put("data", new net.sf.json.JSONObject().fromObject(memberActivityThing));
            }else{
                json.put("data", "");
            }
        }else{
            json.put("data", "");
        }
        ApiUtils.setBackInfo(response, json);
    }

    /**
     *
     * 插入customer
     * @param nickname
     * @param request
     * @param response
     */
    @RequestMapping("/test/insertCustomer")
    public void insertCustomer(String nickname, String brandId, String shopId, String phone, HttpServletRequest request, HttpServletResponse response) throws AppException {
        AppUtils.unpack(request, response);
        //切换数据源
        DataSourceTarget.setDataSourceName(brandId);
        Customer customer = new Customer();
        customer.setWechatId(ApplicationUtils.randomUUID());
        customer.setNickname(nickname);
        customer.setBrandId(brandId);
        customer = customerService.register(customer);
        customerService.bindPhone(phone, customer.getId(), 1, shopId, null,null);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("withdrawals")
    public void withdrawals(BigDecimal money, String customerId, HttpServletRequest request, HttpServletResponse response){
        AppUtils.unpack(request, response);

        JSONResult jsonResult= chargeOrderService.withdrawals(money, customerId);

        JSONObject json = new JSONObject();
        if (jsonResult.isSuccess() == true) {
            json.put("statusCode", "200");
            json.put("success", true);
            json.put("message", jsonResult.getMessage());
        } else {
            json.put("success", false);
            json.put("message", jsonResult.getMessage());
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/withdrawalsRemaim")
    public void withdrawalsRemaim(String customerId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        BigDecimal money = chargeOrderService.selectByCustomerIdNotChangeId(customerId);
        if(money == null){
            money = new BigDecimal(0);
        }
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("withdrawalsRemaim", money);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/lastOrderBySevenMode")
    public void lastOrderBySevenMode(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        String currentCustomerId = getCurrentCustomerId();
        Order order = orderService.getLastOrderBySevenMode(getCurrentCustomerId());
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        if (order != null) {
            json.put("data", new JSONObject(order));
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

}
