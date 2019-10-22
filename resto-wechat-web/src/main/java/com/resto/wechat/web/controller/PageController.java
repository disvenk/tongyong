package com.resto.wechat.web.controller;

import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.model.*;
import com.resto.brand.web.model.TableQrcode;
import com.resto.brand.web.service.*;
import com.resto.brand.web.service.TableQrcodeService;
import com.resto.shop.web.constant.OrderPayState;
import com.resto.shop.web.constant.OrderState;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.constant.ProductionStatus;
import com.resto.shop.web.model.*;
import com.resto.api.customer.entity.Customer;
import com.resto.shop.web.service.*;
import com.resto.wechat.web.config.SessionKey;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.Encrypter;
import com.resto.wechat.web.util.WeChatCardUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@Controller
public class PageController extends GenericController {

    @Resource
    BrandSettingService brandSettingService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    BrandService brandService;

    @Resource
    NewCustomerService newCustomerService;

    @Resource
    OrderService orderService;

    @Resource
    OrderPaymentItemService orderPaymentItemService;

    @Resource
    WechatConfigService wechatConfigService;

    @Resource
    TableQrcodeService tableQrcodeService;

    @Autowired
    WxServerConfigService wxServerConfigService;

    @Resource
    NewCustomCouponService newCustomCouponService;

    @Resource
    RedConfigService redConfigService;

    @Value("#{configProperties['paycode.host']}")
    private String payCodeHost;

    @Autowired
    RedisService redisService;

    @Autowired
    WeChatService weChatService;

    @Value("#{configProperties['wechat.host']}")
    private String wechatHost;

    @Value("#{configProperties['pay.host']}")
    private String payHost;

    @Value("#{configProperties['shoplistnew.host']}")
    private String newshoplistHost;

    @Value("#{configProperties['soup.host']}")
    private String soupHost;

    @Value("#{configProperties['recharge.host']}")
    private String rechargeHost;

    @Value("#{configProperties['qiehuan.host']}")
    private String qiehuanHost;

    @Value("#{configProperties['alipay.host']}")
    private String aliPayHost;

    @Value("#{configProperties['appraise.host']}")
    private String appraiseHost;

    @Value("#{configProperties['log.url.host']}")
    private String logUrlHost;

    @Value("#{configProperties['pos.action.url']}")
    private String url;

    @Value("#{configProperties['scan.host']}")
    private String scanHost;

    @Value("#{configProperties['openAliPay.host']}")
    private String openAliPayHost;

    @Value("#{configProperties['resto.version']}")
    private String version;

    @RequestMapping("index")
    public void index(HttpServletRequest request, HttpServletResponse response, String payMode, String orderId, String subpage) throws IOException {
        AppUtils.unpack(request, response);
        String URL = request.getRequestURL() + "";
        String URI = request.getRequestURI();
        String header = request.getHeader("user-agent");

        /*start*/
        if(request.getAttribute("aliPay") != null && request.getParameter("vv")!=null){
            String query = request.getQueryString();

            String basrUrl = URL.substring(0, URL.length() - URI.length() + 1).substring(0, URL.substring(0, URL.length() - URI.length() + 1).length() - 1);

            if (query == null) {
                query = "baseUrl=" + basrUrl;     //添加个品牌域名传参
                query = query == null ? "" : "?" + query;
            } else {
                if (query.indexOf("baseUrl=") == -1) {
                    query += "&baseUrl=" + basrUrl;     //添加个品牌域名传参
                }
                query = query == null ? "" : "?" + query;
            }

            query = query + "&version=" + version;
            String vv = request.getParameter("vv");

                TableQrcode tableQrcode = tableQrcodeService.selectById(Long.parseLong(Encrypter.decrypt(vv)));
            BrandSetting brandSetting = brandSettingService.selectByBrandId(tableQrcode.getBrandId());
            if (brandSetting.getOpenHttps() == 1) {
                basrUrl = basrUrl.replace("http", "https");
            }
                query = query + "&shopId=" + tableQrcode.getShopDetailId() + "&tableNumber=" + tableQrcode.getTableNumber();

                String aliPayURL = URL.substring(0, URL.length() - URI.length() + 1) + aliPayHost;  //计算域名
                response.sendRedirect(aliPayURL + query);
                return;
        }
        /*end*/



        String wechatURL = URL.substring(0, URL.length() - URI.length() + 1) + wechatHost;  //计算域名
        Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),getCurrentCustomer().getId());
        BrandSetting brandSetting = brandSettingService.selectByBrandId(customer.getBrandId());
        if (brandSetting.getOpenHttps() == 1) {
            wechatURL = wechatURL.replace("http", "https");
        }
        log.info("访问域名链接地址：" + wechatURL);
        if (request.getQueryString() != null && request.getQueryString().indexOf("shopId=") > -1 && request.getQueryString().indexOf("tableNumber=") > -1 &&
                StringUtils.isEmpty(request.getAttribute("tableNumber")) &&
                StringUtils.isEmpty(customer.getLastTableNumber())) {
            request.removeAttribute("shopId");
            request.removeAttribute("tableNumber");
        }

        Map map = new HashMap(4);
        map.put("brandName", getCurrentBrand().getBrandName());
        map.put("fileName", customer.getId());

        map.put("type", "UserAction");

        String query = request.getQueryString();
        //后付款成功后推送付款信息
        if ("houfuSuccess".equals(payMode)) {
            payContent(orderId);
        }
        String basrUrl = URL.substring(0, URL.length() - URI.length() + 1).substring(0, URL.substring(0, URL.length() - URI.length() + 1).length() - 1);
        if (brandSetting.getOpenHttps() == 1) {
            basrUrl = basrUrl.replace("http", "https");
        }
        if (query == null) {
            query = "baseUrl=" + basrUrl;     //添加个品牌域名传参
            query = query == null ? "" : "?" + query;
        } else {
            if (query.indexOf("baseUrl=") == -1) {
                query += "&baseUrl=" + basrUrl;     //添加个品牌域名传参
            }
            query = query == null ? "" : "?" + query;
        }
//        query =  StringUtils.isEmpty(query) ? "?t=" + new Date().getTime() : query + "&t=" + new Date().getTime();
        query = query + "&version=" + version;
        String vv = request.getParameter("vv");
        String tableNumber = request.getParameter("tableNumber");
        String df = request.getParameter("df");
        if (!org.apache.commons.lang.StringUtils.isEmpty(df)) {
            Customer c = new Customer();
            c.setSerialNumber(Long.valueOf(df));
            Customer shareCust = newCustomerService.dbSelectOne(getCurrentBrandId(),c);
            if (shareCust != null&& !org.apache.commons.lang.StringUtils.isEmpty(request.getParameter("notIntoShop"))){
                query = query + "&df=fx&shareCustomer=" + shareCust.getId();
            }else if (shareCust != null) {
                query = query + "&df=fx&shareCustomer=" + shareCust.getId();
                //----YYQ 注释   现在不需要进入对应店铺，只需要进入店铺列表
//                query = query + "&shopId=" + shareCust.getLastOrderShop() + "&df=fx&shareCustomer=" + shareCust.getId();
            }
        }
        if (vv != null) {
            TableQrcode tableQrcode = tableQrcodeService.selectById(Long.parseLong(Encrypter.decrypt(vv)));
            query = query + "&shopId=" + tableQrcode.getShopDetailId() + "&tableNumber=" + tableQrcode.getTableNumber();

            ShopDetail shop = shopDetailService.selectByPrimaryKey(tableQrcode.getShopDetailId());
            //进入后付店铺，如果是扫码进入  贼判断这个店铺是否是后付
            //如果是后付店铺，查询改桌号是否存在未支付的订单，则进入后付买单页面。
            if(shop.getAllowAfterPay() == 0){
                Order noPayOrder = orderService.selectOrderByTableNumberNoPay(tableQrcode.getTableNumber().toString(), tableQrcode.getShopDetailId());
                if(noPayOrder != null){

                    noPayOrder.setCustomerId(getCurrentCustomerId());
                    orderService.update(noPayOrder);

                    map.put("content", "用户成功扫码进入微信点餐界面，并且该桌号存在未支付订单，已经绑定给该用户,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                    if (brandSetting.getOpenHttps() == 1) {
                        response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1).replace("http", "https") + wechatHost + query + "&orderBossId=" + noPayOrder.getId() + "&articleBefore=1&dialog=closeRedPacke");
                    } else {
                        response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1) + wechatHost + query + "&orderBossId=" + noPayOrder.getId() + "&articleBefore=1&dialog=closeRedPacke");
                    }
                    return;
                }
            }
        }
        if (request.getAttribute("aliPay") != null) {
            String aliPayURL = URL.substring(0, URL.length() - URI.length() + 1) + aliPayHost;  //计算域名
            response.sendRedirect(aliPayURL + query);
            return;
        }

        //从推送进入支付订单
        String orderBossId = request.getParameter("orderBossId");
        if (orderBossId != null) {
            Order order = orderService.selectById(orderBossId);
            if (order.getIsPay() != OrderPayState.ALIPAYING) {
                order.setIsPay(OrderPayState.NOT_PAY);
                orderService.update(order);
            }
        }

        if (header.contains("AliApp")) {
            String aliPayURL = URL.substring(0, URL.length() - URI.length() + 1) + aliPayHost;  //计算域名
            response.sendRedirect(aliPayURL + query);
            return;
        }

        if (query.indexOf("shopId=") == -1 && query.indexOf("tableNumber=") == -1 && customer.getLastTableNumber() != null && !"".equals(customer.getLastTableNumber())) {
            query = query + "&shopId=" + customer.getLastOrderShop() + "&tableNumber=" + customer.getLastTableNumber();
        }
        if (StringUtils.isEmpty(customer.getRegisterShopId())) {
            customer.setRegisterShopId(getCurrentShopId());
        }
        customer.setLastTableNumber("");
        newCustomerService.dbUpdate(getCurrentBrandId(),customer);

        //如果进入的品牌公众号内店铺数量为1个的时候   不需要切换店铺，同时也不需要进入选择店铺页面；
//        if (query.indexOf("shopId=") == -1) {
//            List<ShopDetail> shops = shopDetailService.selectByBrandId(getCurrentBrandId());
//            if (shops.size() == 1) {
//                query = query + "&qiehuan=qiehuan";
//            }
//        }
        //判断店铺是否开启了进入店铺选择页面
//        if (brandSetting.getOpenShoplist() == 0 && query.indexOf("qiehuan=") == -1) {
//            query = query + "&qiehuan=qiehuan";
//        }
        //判断用户是否存在最近的一笔有效消费记录，如果存在则优先进入对应门店
//        Order order = orderService.selectAfterValidOrderByCustomerId(customer.getId());
//        if(order != null && query.indexOf("qiehuan=") == -1 && query.indexOf("shopId") == -1){
//            ShopDetail oShop = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
//            System.out.println(order.getShopDetailId());
//            System.out.println(oShop.getIsOpen());
//            if(oShop != null && oShop.getIsOpen()){
//                query = query +"&shopId=" + order.getShopDetailId() + "&quehuan=qiehuan&oldCustomer=1";
//            }
//        }
        log.info("当前query的值为：" + query);
        if("payCode".equals(subpage)){
            map.put("content", "用户成功进入付款码页面界面,请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            if (brandSetting.getOpenHttps() == 1) {
                response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1).replace("http", "https") + payCodeHost + query + "#fans");
            } else {
                response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1) + payCodeHost + query + "#fans");
            }
        } else if (request.getParameter("dialog") != null && "waitScan".equals(request.getParameter("dialog"))) {
            if (brandSetting.getOpenHttps() == 1) {
                log.info(URL.substring(0, URL.length() - URI.length() + 1).replace("http", "https") + scanHost + query);
                response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1).replace("http", "https") + scanHost + query);
            } else {
                response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1) + scanHost + query);
            }
        } else if ((query.indexOf("tableNumber=") == -1 || !"".equals(request.getAttribute("tableNumber")) && request.getAttribute("tableNumber") != null)
                && query.indexOf("shopId") == -1 && query.indexOf("qiehuan=") == -1
                && query.indexOf("dialog") == -1 ) {
            map.put("content", "用户成功进入微信点餐界面,请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            log.info("query:" + query);
            if (brandSetting.getOpenHttps() == 1) {
                if("1".equals(brandSetting.getSwitchMode())){
                response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1).replace("http", "https") + qiehuanHost + query);
                }else if("2".equals(brandSetting.getSwitchMode())){
                    response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1).replace("http", "https") + newshoplistHost + query);
                }
            } else {
                if("1".equals(brandSetting.getSwitchMode())){
                response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1) + qiehuanHost + query);
                }else if("2".equals(brandSetting.getSwitchMode())){
                    response.sendRedirect(URL.substring(0, URL.length() - URI.length() + 1) + newshoplistHost + query);
                }
            }
        } else if ("tangshi".equals(subpage)) {
            map.put("content", "用户成功进入微信点餐界面,请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            response.sendRedirect(wechatURL + query + "#tangshi");
        } else if ("home".equals(subpage)) {
            map.put("content", "用户成功进入主页界面,请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            response.sendRedirect(wechatURL + query + "#home");
        } else if ("my".equals(subpage)) {
            map.put("content", "用户成功进入我的界面,请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            response.sendRedirect(wechatURL + query + "#my");
        } else if ("fans".equals(subpage)) {
            map.put("content", "用户成功进入粉丝圈界面,请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            response.sendRedirect(wechatURL + query + "#fans");
        } else {
            map.put("content", "用户成功进入微信点餐界面,请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            response.sendRedirect(wechatURL + query + "#tangshi");
        }
    }

    @RequestMapping("/jsconfig")
    @ResponseBody
    public void jsconfig(String query, String payMode, String qiehuan, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        String URL = request.getRequestURL() + "";
        String URI = request.getRequestURI();
        String wechatURL = URL.substring(0, URL.length() - URI.length() + 1);

        String requestURL = request.getRequestURL().toString();
        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
        Brand brand = brandService.selectBySign(brandSign);
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
        if (brandSetting.getOpenHttps() == 1) {
            wechatURL = wechatURL.replace("http", "https");
        }
        query = query == null ? "" : "?" + query;
        String href;
        if ("houfu".equals(payMode)) {
            href = wechatURL + payHost + query;  //计算域名
        } else if ("qiehuan".equals(qiehuan)) {
            href = wechatURL + qiehuanHost + query;  //计算域名
        } else if ("recharge".equals(qiehuan)){
            href = wechatURL + rechargeHost + query;  //计算域名
        } else if ("scan".equals(qiehuan)) {
            href = wechatURL + scanHost + query;  //计算域名
        } else if ("openAliPay".equals(qiehuan)) {
            href = wechatURL + openAliPayHost + query;  //计算域名
        }else if ("soup".equals(qiehuan)){
            href = wechatURL + soupHost + query;  //计算域名
        } else {
            href = wechatURL + wechatHost + query;  //计算域名
        }
        String[] jsApiList = new String[]{
                WeChatService.onMenuShareTimeline, WeChatService.onMenuShareAppMessage,
                WeChatService.getLocation, WeChatService.openLocation,
                WeChatService.scanQRCode, WeChatService.chooseImage, WeChatService.previewImage,
                WeChatService.uploadImage, WeChatService.downloadImage, WeChatService.hideMenuItems,
        };
        WechatConfig currentConfig = brand.getWechatConfig();
        String appId = currentConfig.getAppid();
        String appsecret = currentConfig.getAppsecret();

        log.info("获取微信JS Api列表的href路径：" + href);
        String accessToken = weChatService.getJsAccessToken(appId, appsecret);
        JSONObject jsConfig = WeChatCardUtils.getJsConfig(jsApiList, false, href, appId, accessToken);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", jsConfig);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/setting")
    @ResponseBody
    public void brandSettingInfo(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        JSONObject json = new JSONObject();
//        String requestURL = request.getRequestURL().toString();
//        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
//        Brand brand = (Brand) redisService.get(brandSign);
//        if (brand == null) {
//            brand = brandService.selectBySign(brandSign);
//            redisService.set(brandSign,brand);
//        }
        Brand brand = getCurrentBrand();
        BrandSetting setting = (BrandSetting) redisService.get(brand.getId() + "setting");
        if (setting == null) {
            setting = brandSettingService.selectByBrandId(brand.getId());
            redisService.set(brand.getId() + "setting", setting);
        }
//        BrandSetting setting =brandSettingService.selectByBrandId(brand.getId());
        if (StringUtils.isEmpty(setting.getWechatBrandName())) {
            setting.setWechatBrandName(brand.getBrandName());
        }
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        List<NewCustomCoupon> newCustomCoupons =(List<NewCustomCoupon>) redisService.get(brand.getId() + "newCustomCoupon");
        if (newCustomCoupons == null) {
            newCustomCoupons = newCustomCouponService.selectBirthCoupon();
            redisService.set(brand.getId() + "newCustomCoupon", newCustomCoupons);
        }
//        List<NewCustomCoupon> newCustomCoupons =newCustomCouponService.selectBirthCoupon();
        if (newCustomCoupons.size() != 0) {
            json.put("data", new JSONObject(setting).put("birthGift", true));
        } else {
            json.put("data", new JSONObject(setting).put("birthGift", false));
        }
        json.put("brand", new JSONObject(brand));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/jsWxPayconfig")
    @ResponseBody
    public void jsWxPayconfig(String query, String payMode, String qiehuan, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        String URL = request.getRequestURL() + "";
        String URI = request.getRequestURI();
        String wechatURL = URL.substring(0, URL.length() - URI.length() + 1);

        String requestURL = request.getRequestURL().toString();
        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
        Brand brand = brandService.selectBySign(brandSign);
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
        if (brandSetting.getOpenHttps() == 1) {
            wechatURL = wechatURL.replace("http", "https");
        }

        String shopId = getCurrentShop() != null ? getCurrentShop().getId()  : null;

        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        query = query == null ? "" : "?" + query;
        String href;
        if ("houfu".equals(payMode)) {
            href = wechatURL + payHost + query;  //计算域名
        } else if ("qiehuan".equals(qiehuan)) {
            href = wechatURL + wechatHost + query;  //计算域名
        } else if ("recharge".equals(qiehuan)){
            href = wechatURL + rechargeHost + query;  //计算域名
        } else if ("scan".equals(qiehuan)) {
            href = wechatURL + scanHost + query;  //计算域名
        } else if ("openAliPay".equals(qiehuan)) {
            href = wechatURL + openAliPayHost + query;  //计算域名
        }else if ("soup".equals(qiehuan)){
            href = wechatURL + soupHost + query;  //计算域名
        } else {
            href = wechatURL + wechatHost + query;  //计算域名
        }
        String[] jsApiList = new String[]{
                WeChatService.chooseWXPay,
        };
        String appId;
        String appsecret;
        if(shopDetail != null && shopDetail.getWxServerId() != null){
            WxServerConfig wxServerConfig =
                    wxServerConfigService.selectById(shopDetail.getWxServerId());
            appId = wxServerConfig.getAppid();
            appsecret = wxServerConfig.getAppsecret();
        }else{
            WechatConfig currentConfig = brand.getWechatConfig();
            appId = currentConfig.getAppid();
            appsecret = currentConfig.getAppsecret();
        }


        log.info("获取微信JS Api列表的href路径：" + href);
        String accessToken = weChatService.getJsAccessToken(appId, appsecret);
        JSONObject jsConfig = WeChatCardUtils.getJsConfig(jsApiList, false, href, appId, accessToken);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", jsConfig);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/shopmode")
    public void shopmode(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        HttpSession session = request.getSession();

        ShopDetail shopDetail = (ShopDetail) session.getAttribute(SessionKey.CURRENT_SHOP);
        int shopMode;
        if (shopDetail == null) {
            shopMode = 0;
        } else {
            try {
                if (!StringUtils.isEmpty(shopDetail.getShopMode())) {
                    shopMode = shopDetail.getShopMode();
                } else {
                    shopMode = 0;
                }
            } catch (Exception e) {
                shopMode = 0;
                log.error(e.getMessage());
            }
        }
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("shopMode", shopMode);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/ipconfig")
    public void ipConfig(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Brand brand = (Brand) redisService.get(getCurrentBrandId() + "info");
        if (brand == null) {
            brand = brandService.selectById(getCurrentBrandId());
            redisService.set(getCurrentBrandId() + "info", brand);
        }
//        Brand brand = brandService.selectById(getCurrentBrandId());
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("ipconfig", brand.getWechatImgUrl());
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("redConfig")
    public void redConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AppUtils.unpack(request, response);
        List<RedConfig> redConfig = redConfigService.selectListByShopId(getCurrentShopId());

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", redConfig);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    public void payContent(String orderId) {
        Order order = orderService.selectById(orderId);
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        Brand brand = brandService.selectById(order.getBrandId());
        if (order != null && (order.getOrderMode() == ShopMode.HOUFU_ORDER || order.getOrderMode() == ShopMode.BOSS_ORDER) && order.getOrderState() == OrderState.PAYMENT
                && order.getProductionStatus() == ProductionStatus.PRINTED) {
            Customer customer = newCustomerService.dbSelectByPrimaryKey(shopDetail.getBrandId(),order.getCustomerId());
            WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            List<OrderPaymentItem> paymentItems = orderPaymentItemService.selectByOrderId(order.getId());
            String money = "(";
            for (OrderPaymentItem orderPaymentItem : paymentItems) {
                money += PayMode.getPayModeName(orderPaymentItem.getPaymentModeId()) + "： " + orderPaymentItem.getPayValue() + " ";
            }
            StringBuffer msg = new StringBuffer();
            BigDecimal sum = order.getOrderMoney();
            List<Order> orders = orderService.selectByParentId(order.getId(), order.getPayType()); //得到子订单
            for (Order child : orders) { //遍历子订单
                sum = sum.add(child.getOrderMoney());
            }
            msg.append("您的订单").append(order.getSerialNumber()).append("已于").append(DateFormatUtils.format(paymentItems.get(0).getPayTime(), "yyyy-MM-dd HH:mm"));
            msg.append("支付成功。订单金额：").append(sum).append(money).append(") ");
            weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
        }
    }

    @RequestMapping("appraise")
    public void index(HttpServletRequest request, HttpServletResponse response, String appraiseId) throws IOException {
        AppUtils.unpack(request, response);
        String URL = request.getRequestURL() + "";
        String URI = request.getRequestURI();

        String wechatURL = URL.substring(0, URL.length() - URI.length() + 1) + appraiseHost;  //计算域名

        String query = "?appraiseId=" + appraiseId + "&baseUrl=" + URL.substring(0, URL.length() - URI.length());

        response.sendRedirect(wechatURL + query);
    }

    @RequestMapping("/xiaoximobanOpen")
    public void xiaoximobanOpen(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        String result = weChatService.getApiSetIndustry("wxbe17c6db0bf1e316", "120a67cd001cd486674b14397e2d4f25");

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", result);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}
