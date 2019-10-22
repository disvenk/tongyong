package com.resto.wechat.web.interceptors;

import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.model.Coupon;
import com.resto.api.customer.entity.Customer;
import com.resto.shop.web.model.CustomerGroup;
import com.resto.shop.web.model.TableGroup;
import com.resto.shop.web.service.CouponService;
import com.resto.shop.web.service.CustomerGroupService;
import com.resto.shop.web.service.CustomerService;
import com.resto.shop.web.service.TableGroupService;
import com.resto.wechat.web.config.SessionKey;
import com.resto.wechat.web.controller.GenericController;
import com.resto.wechat.web.exception.GenericException;
import com.resto.wechat.web.rpcinterceptors.DataSourceTarget;
import com.resto.wechat.web.util.EmojiFilter;
import com.resto.wechat.web.util.Encrypter;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

public class WechatLoginInterceptor extends GenericController implements HandlerInterceptor {


    @Resource
    BrandService brandService;

    @Resource
    BrandSettingService brandSettingService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    NewCustomerService newCustomerService;

    @Resource
    CustomerService customerService;

    @Resource
    TableQrcodeService tableQrcodeService;

    @Resource
    NewEmployeeService newEmployeeService;

    @Resource
    AccountSettingService accountSettingService;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    CustomerGroupService customerGroupService;

    @Autowired
    RedisService redisService;

    @Autowired
    WeChatService weChatService;

    @Autowired
    CouponService couponService;

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        HttpSession session = request.getSession();

        String urlIgr = ".*/((pay)|(msg)|(wechatCard)|(receipt)|(wechatReceipt)).*";

        String header = request.getHeader("user-agent");

        /*getRequestURI:/test/test.jsp
        getRequestURL:http://localhost:8080/test/test.jsp */
         String requestURL = request.getRequestURL().toString();
        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
        if ("127".equalsIgnoreCase(brandSign)) {
            brandSign = "test0001";
        }
        //获取到品牌信息
        Brand brand = brandService.selectBySign(brandSign);

        //如果服务器没有记住品牌相关信息，或者品牌为调试号则进入信息获取与记录
        if (session.getAttribute(SessionKey.CURRENT_BRAND) == null || brand.getId().equals("d877a46cdbe14d618302f7c3f48bc186")) {
            //看当前品牌在缓存中是否有店铺的列表
            List<ShopDetail> shopList = (List<ShopDetail>) redisService.get(brand.getId() + "shopList");
            //没有则添加
            if (shopList == null) {
                shopList = shopDetailService.selectByBrandId(brand.getId());
                redisService.set(brand.getId() + "shopList", shopList);
            }
//            List<ShopDetail> shopList = shopDetailService.selectByBrandId(brand.getId());
            //将店铺id对应店铺名称的方式存入集合中
            Map<String, ShopDetail> shopMap = new HashMap<>();
            for (ShopDetail shopDetail : shopList) {
                shopMap.put(shopDetail.getId(), shopDetail);
            }
            //以下是当前品牌下的概要数据
            request.getSession().setAttribute(SessionKey.SHOP_MAP, shopMap);
            request.getSession().setAttribute(SessionKey.SHOP_LIST, shopList);
            request.getSession().setAttribute(SessionKey.CURRENT_BRAND, brand);
            request.getSession().setAttribute(SessionKey.CURRENT_WECHAT_CONFIG, brand.getWechatConfig());
            request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID, brand.getId());
        }
        //切换数据源
        DataSourceTarget.setDataSourceName(brand.getId());

        if (request.getRequestURI().matches(urlIgr)) {
            request.setAttribute("pass", true);
            return true;
        }

        if (request.getRequestURI().matches(".*/new/lastOrderByCustomer.*")) {
            log.info("判断用户是否存在加菜订单");
            request.setAttribute("pass", true);
            return true;
        }

        if (request.getRequestURI().matches(".*/weApp/get.*")) {
            log.info("微信小程序");
            request.setAttribute("pass", true);
            return true;
        }

        if (request.getRequestURI().matches(".*/new/eleme/version2.0/test.*")) {
            log.info("来自饿了么2.0的订单");
            request.setAttribute("pass", true);
            return true;
        }

        if (request.getRequestURI().matches(".*/customer/test/insertCustomer.*")) {
            log.info("插入测试用户数据");
            request.setAttribute("pass", true);
            return true;
        }

        Map<String, ShopDetail> shopMap = (Map<String, ShopDetail>) session.getAttribute(SessionKey.SHOP_MAP);

        //前端不管是哪个请求都一定会带有shopId这个参数的
        String shopDetailId = request.getParameter("shopId");
//        String shareCustomer = request.getParameter("shareCustomer");
        //取会员序列号
        String df = request.getParameter("df");//未知
        Long usefulTime = request.getParameter("usefulTime") != null ? Long.parseLong(request.getParameter("usefulTime")) : 0;//未知
        String shareCustomer = null;
        Boolean shareRegister = StringUtils.isNotBlank(request.getParameter("shareRegister")) ? true : false;
        Boolean isShareLink = StringUtils.isNotBlank(request.getParameter("isShareLink")) ? true : false;

        if (!StringUtils.isEmpty(df)) {
            Customer c=new Customer();
            c.setSerialNumber(Long.valueOf(df));
            Customer shareCust = newCustomerService.dbSelectOne(getCurrentBrandId(),c);
            if (shareCust != null) {
                shareCustomer = shareCust.getId();
                //判断分享用户是否有上次消费的门店记录
                if (!StringUtils.isEmpty(shareCust.getLastOrderShop())&& StringUtils.isEmpty(request.getParameter("notIntoShop"))) {
//                    shopDetailId = shareCust.getLastOrderShop();    //----YYQ 注释   现在不需要进入对应店铺，只需要进入店铺列表
                }if(!StringUtils.isEmpty(request.getParameter("notIntoShop"))){
                    //如果扫分享二维码 进入店铺  不进入任意门店 。  进入店铺选择页面Ian
                } else {
                    //取当前品牌下任意一家门店id
                    //----YYQ 注释   现在不需要进入对应店铺，只需要进入店铺列表
//                    shopDetailId = shopDetailService.selectByBrandId(brand.getId()).get(0).getId();
                }
            }
        } else {
            shareCustomer = request.getParameter("shareCustomer");
        }

        if (request.getRequestURI().matches(".*/new/third.*")) {
            log.info("来自饿了吗的订单");
            request.setAttribute("pass", true);
            return true;
        }
        if (request.getRequestURI().matches(".*/new/penScan.*")) {
            log.info("记录扫描二维码的日志");
            request.setAttribute("pass", true);
            return true;
        }
        if (request.getRequestURI().matches(".*/new/settable.*")) {
            log.info("设置桌号");
            request.setAttribute("pass", true);
            return true;
        }
        if (request.getRequestURI().matches(".*/new/aliPayOrder.*")) {
            log.info("获取支付宝订单");
            request.setAttribute("pass", true);
            return true;
        }

         if (header.contains("AliApp")) {
       //if (header!=null) {
            log.info("支付宝扫码桌号");
            request.setAttribute("pass", true);
            request.setAttribute("aliPay", true);
            return true;
        }
        if (request.getRequestURI().matches(".*/duoren.*")) {

            request.setAttribute("pass", true);
            return true;
        }

        //获取品牌设置
        BrandSetting brandSetting = brandSettingService.selectById(brand.getBrandSettingId());
        //yz 2017/07/29 存品牌账户相关信息 -- begin
        if (brandSetting.getOpenBrandAccount() == 1) {
            request.getSession().setAttribute(SessionKey.OPENBRANDACCOUNT, true);
            AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
            request.getSession().setAttribute(SessionKey.CURRENT_ACCOUNT_SETTING, accountSetting);
        } else {
            request.getSession().setAttribute(SessionKey.OPENBRANDACCOUNT, false);
        }
        //yz 存品牌账户相关信息 --- end


        //判断二维码是否加密
        String tName = request.getParameter("tableNumber");
        String vv = request.getParameter("vv");
        if (tName != null && tName != "") {
            ShopDetail shopDetail = shopDetailService.selectById(shopDetailId);
            if (shopDetail.getIsNewQrcode() == 1) {
                //不允许使用旧的二维码
                if (brandSetting.getOpenHttps() == 1) {
                    response.sendRedirect("https://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/errorCode.html");
                } else {
                    response.sendRedirect("http://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/errorCode.html");
                }
                return false;
            }
        }
        TableQrcode tableQrcode = null;
        if (vv != null) {
            tableQrcode = tableQrcodeService.selectById(Long.parseLong(Encrypter.decrypt(vv)));
            if (tableQrcode.getState() == 0) {
                if (brandSetting.getOpenHttps() == 1) {
                    response.sendRedirect("https://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/errorCode.html");
                } else {
                    response.sendRedirect("http://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/errorCode.html");
                }
                return false;
            } else {
                shopDetailId = tableQrcode.getShopDetailId();
                tName = tableQrcode.getTableNumber().toString();
            }
        }
        //----二维码解密结束-------------------------------------------------------------------
        if (shopDetailId != null) {
            ShopDetail shopd = shopMap.get(shopDetailId);
            session.setAttribute(SessionKey.CURRENT_SHOP, shopd);
        }

        //returnShop   用户不关注  但是仍然进入店铺
        String returnShop = request.getParameter("returnShop");
        String code = request.getParameter("code");
        //获取微信配置
        WechatConfig wConfig = brand.getWechatConfig();
        //用户不为空的情况下 start
        if (session.getAttribute(SessionKey.CURRENT_CUSTOMER) != null) {
            Customer customer = (Customer) session.getAttribute(SessionKey.CURRENT_CUSTOMER);
            customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),customer.getId());
            //如果用户有手机号
            if (customer.getTelephone() != null) {
                //并且看黑名单里面是否有这个用户
                if (brand.getPhoneList().contains(customer.getTelephone()) || brand.getPhoneList().contains(customer.getWechatId())) {
                    return false;
                }
            }
                //如果白名单不为空
            if (!StringUtils.isEmpty(brand.getWhitePhoneList())) {
                //并且用户手机号不为空且用户在白名单之中
                if (!StringUtils.isEmpty(customer.getTelephone()) && brand.getWhitePhoneList().contains(customer.getTelephone())) {
                    //通过
                } else if (brand.getWhitePhoneList().contains(customer.getWechatId())) {
                    //通过
                } else {
                    return false;
                }
            }

            if (tName != null && (usefulTime == null || usefulTime == 0)) {
                customer.setLastTableNumber(tName);
                newCustomerService.dbUpdate(getCurrentBrandId(),customer);
                ShopDetail shop = shopMap.get(shopDetailId);
                if (requestURL.contains("index") && shop.getOpenManyCustomerOrder() == Common.YES) {
                    //扫码进入系统时
                    //如果这个桌子下存在组
                    //判断是这个人是否在这个桌子下的一个已支付的组里
                    log.info("扫码进入start");
                    log.info("扫描的桌号为" + tName);
//                    TableGroup groupList = tableGroupService.getTableGroupByState(shopDetailId, customer.getId(), tName, 1);
//                    if (groupList == null) {
//                        //如果不在，那么删除这个人在这个桌子上所有的组关系
//                        customerGroupService.removeGroupByCustomerId(shopDetailId, tName, customer.getId());
//                    }
                    //先得到这个店铺下 该桌位 是否已经存在组
                    List<TableGroup> tableGroups = tableGroupService.getTableGroupByShopId(shopDetailId, tName);
                    if (CollectionUtils.isEmpty(tableGroups)) {
                        //如果组未存在，则自动创建
                        String groupId = ApplicationUtils.randomUUID();
                        TableGroup group = new TableGroup();
                        group.setCreateCustomerId(customer.getId());
                        group.setBrandId(brand.getId());
                        group.setGroupId(groupId);
                        group.setShopDetailId(shopDetailId);
                        group.setTableNumber(tName);
                        tableGroupService.insertGroup(group);
                        //创建人与组的关系表
                        CustomerGroup customerGroup = new CustomerGroup();
                        customerGroup.setBrandId(brand.getId());
                        customerGroup.setShopDetailId(shopDetailId);
                        customerGroup.setTableNumber(tName);
                        customerGroup.setIsLeader(Common.YES);
                        customerGroup.setHeadPhoto(customer.getHeadPhoto());
                        customerGroup.setCustomerName(customer.getNickname());
                        customerGroup.setCustomerId(customer.getId());
                        customerGroup.setGroupId(groupId);
                        customerGroupService.insert(customerGroup);
                    }
                }

            }
            //2017-08-15  缓存中存在customer的情况下也要去更新微信用户信息  wtl
            String accessToken = weChatService.getAccessToken(wConfig.getAppid(), wConfig.getAppsecret());
            String customInfoJson = weChatService.getUserInfoSubscribe(accessToken, customer.getWechatId());
            JSONObject cusInfo = new JSONObject(customInfoJson);
            Integer subscribe = 1;
            try {
                subscribe = cusInfo.getInt("subscribe");
            } catch (Exception e) {
                log.error("未获取到公众号是否关注，默认关注！" + e.getMessage());
            }
            if (vv != null) {
                Map customerMap = new HashMap(4);
                customerMap.put("brandName", getCurrentBrand().getBrandName());
                customerMap.put("fileName", customer.getId());
                customerMap.put("type", "UserAction");
                customerMap.put("content", "用户:" + customer.getId() + "扫码进入店铺，tableId为:" + tableQrcode.getId() + ",桌号为：" + tableQrcode.getTableNumber() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(customerMap);
            }
            getCustomerInfoSubscribe(customer, cusInfo, subscribe);
            if (0 == customer.getSubscribe() && brandSetting.getIntoShopSubscribe() == 1 && !"open".equals(returnShop) && tName != null && shareCustomer == null) {
                log.info("用户关注:" + customer.getSubscribe());
                if (brandSetting.getOpenHttps() == 1) {
                    response.sendRedirect("https://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/subscribe.html?baseUrl=https://" + brand.getBrandSign() + ".restoplus.cn" + (shopDetailId != null ? "&shopId=" + shopDetailId : "") + (tName != null ? "&tableNumber=" + tName : ""));
                } else {
                    response.sendRedirect("http://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/subscribe.html?baseUrl=http://" + brand.getBrandSign() + ".restoplus.cn" + (shopDetailId != null ? "&shopId=" + shopDetailId : "") + (tName != null ? "&tableNumber=" + tName : ""));
                }
                return false;
            } else if (0 == customer.getSubscribe() && brandSetting.getIntoShopSubscribe() == 1 && !"open".equals(returnShop) && StringUtils.isNotBlank(df)) {
                //如果用户未关注公众号并且品牌开启了关注页面在扫个人邀请码进来的时候跳到关注页面
                log.info("扫邀请码进入系统");
                if (brandSetting.getOpenHttps() == 1) {
                    response.sendRedirect("https://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/subscribe.html?df=" + df + "&baseUrl=https://" + brand.getBrandSign() + ".restoplus.cn&shopId=" + shopDetailId + "&shareCustomer=" + shareCustomer + "");
                } else {
                    response.sendRedirect("http://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/subscribe.html?df=" + df + "&baseUrl=http://" + brand.getBrandSign() + ".restoplus.cn&shopId=" + shopDetailId + "&shareCustomer=" + shareCustomer + "");
                }
                return false;
            }
            //判断用户是否存在桌号有效期
            if (usefulTime != null && usefulTime > 0) {
                Long nowDate = new Date().getTime();
                if (((nowDate - usefulTime) / 1000) > 7200) {
                    if (brandSetting.getOpenHttps() == 1) {
                        response.sendRedirect("https://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/views/error.html");
                    } else {
                        response.sendRedirect("http://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/views/error.html");
                    }
                    return false;
                }
            }
            String shareLink = customer.getShareLink();
            customer.setShareLink("clearShareLink");
            newCustomerService.dbUpdate(getCurrentBrandId(),customer);
            session.setAttribute(SessionKey.CURRENT_CUSTOMER, customer);
            System.out.println("存在邀请链接为：" + shareLink);
            System.out.println("链接中含有shareRegister参数：" + shareRegister);
            if (StringUtils.isNotBlank(shareLink) && !"clearShareLink".equalsIgnoreCase(shareLink) && !shareRegister && !isShareLink) {
                System.out.println("重定向路径为：" + shareLink);
                response.sendRedirect(shareLink);
                return false;
            }
            request.setAttribute("pass", true);
            return true;
        }
        //用户不为空的情况下 end

        //浏览器进入wechat页面  需要web=open  以及userId
        String web = request.getParameter("web");
        if ("open".equals(web)) {
            String userId = request.getParameter("userId");
            if (userId != null) {
                Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),userId);
                session.setAttribute(SessionKey.CURRENT_CUSTOMER, customer);
                //如果上次的店铺有消费订单，则表示不是第一进来
                if (customer.getLastOrderShop() != null) {
                    ShopDetail shopd = shopMap.get(customer.getLastOrderShop());
                    session.setAttribute(SessionKey.CURRENT_SHOP, shopd);
                }
                request.setAttribute("pass", true);
                return true;
            } else {
                return false;
            }
        }

        //调试 start
        if (System.getenv("debug") != null) {
            List<Customer> customers = newCustomerService.dbSelect(getCurrentBrandId(),null);
            for (Customer customer : customers) {
                if (customer.getNickname().equals("0.0")) {
                    customer = customerService.login(getCurrentBrandId(),customer.getWechatId());
                    session.setAttribute(SessionKey.CURRENT_CUSTOMER, customer);
                    String query = request.getParameter("shopId");
                    if (query != null && "0e798f7cda1d495283945d450d908040".equals(query)) {
                        ShopDetail shopd = shopMap.get(query);
                        session.setAttribute(SessionKey.CURRENT_SHOP, shopd);
                    } else if (customer.getLastOrderShop() != null) {
                        ShopDetail shopd = shopMap.get(customer.getLastOrderShop());
                        session.setAttribute(SessionKey.CURRENT_SHOP, shopd);
                    } else if (shopMap.size() == 1) {
                        session.setAttribute(SessionKey.CURRENT_SHOP, shopMap.values().iterator().next());
                    }
                    request.setAttribute("pass", true);
                    return true;
                }
            }
            log.error("not debug user find ");
            throw new GenericException("not debug user find ");
        }
        //调试 end

        String state = request.getParameter("state");
        String[] codeValues = request.getParameterValues("code");
        if (codeValues != null && codeValues.length != 0) {
            code = codeValues[codeValues.length - 1];
        }
        if (code == null) {
            state = System.currentTimeMillis() + "";
            //获取到要访问的接口路径(不含域名)
            String query = request.getQueryString();
            query = query == null ? "" : "?" + query;

            //如果用户的code为空则需要进行网页授权，然后进行重定向回之前访问的路径
            String authorizeUrl = weChatService.getUserAuthorizeUrl(state, request.getRequestURL() + query, wConfig.getAppid());
            response.sendRedirect(authorizeUrl);
            return false;
        } else {
            //如果code空就使用code获得用户的信息
            //获取授权认证token
            String accessJson = weChatService.getUrlAccessToken(code, wConfig.getAppid(), wConfig.getAppsecret());
            JSONObject access = new JSONObject(accessJson);
            String token = access.optString("access_token");
            String openid = access.optString("openid");

            Customer customer = customerService.login(getCurrentBrandId(),openid);

            //获取通用接口token
            String accessToken = weChatService.getAccessToken(wConfig.getAppid(), wConfig.getAppsecret());
            //拉取用户信息，不是网页授权是的接口
            String customInfoJson = weChatService.getUserInfoSubscribe(accessToken, openid);
            JSONObject cusInfo = new JSONObject(customInfoJson);
            Integer subscribe = 1;
            try {
                subscribe = cusInfo.getInt("subscribe");
            } catch (Exception e) {
                log.error("未获取到公众号是否关注，默认关注！" + e.getMessage());
            }
            if (0 == subscribe) {
                //未关注调用网页授权的接口获取用户信息
                customInfoJson = weChatService.getUserInfo(token, openid);
                cusInfo = new JSONObject(customInfoJson);
            }

            //用户在系统中不存在
            if (customer == null) {
                if (cusInfo.has("openid")) {
                    //设置customer 的 相关信息
                    customer = new Customer();
                    if (StringUtils.isNotBlank(shareCustomer)) {
                        customer.setIsShare(1);
                    }
                    //封装customer
                    getCustomerInfo(brand, customer, cusInfo, subscribe);
                    //查看当前用户的unionId是否有对应用户，有则返回用户Id
                    String customerId = null;
                    if(customer.getUnionId() != null){
                        customerId = customerService.selectCustomerByUnionId(customer.getUnionId());
                    }
                    if (StringUtils.isNotBlank(customerId)) {
                        customer.setId(customerId);
                        newCustomerService.dbUpdate(getCurrentBrandId(),customer);
                        customer = customerService.login(getCurrentBrandId(),customer.getWechatId());
                    } else {
                        customer = customerService.register(customer);
                    }
                } else {
                    state = System.currentTimeMillis() + "";
                    String query = request.getQueryString();
                    query = query == null ? "" : "?" + query;
                    String authorizeUrl = weChatService.getUserAuthorizeUrl(state, request.getRequestURL() + query, wConfig.getAppid());
                    response.sendRedirect(authorizeUrl);
                    return false;
                }
            }
            //用户在系统中存在
            else {
                if (cusInfo.has("openid")) {
                    System.out.println("---------------------------tfefefefefe" + brand.getPhoneList());

                    if (customer.getTelephone() != null) {
                        if (brand.getPhoneList().contains(customer.getTelephone()) || brand.getPhoneList().contains(customer.getWechatId())) {
                            return false;
                        }
                    }
                    if (!StringUtils.isEmpty(brand.getWhitePhoneList())) {
                        if (!StringUtils.isEmpty(customer.getTelephone()) && brand.getWhitePhoneList().contains(customer.getTelephone())) {
                        } else if (brand.getWhitePhoneList().contains(customer.getWechatId())) {
                        } else {
                            return false;
                        }
                    }
                    String shareLink = customer.getShareLink();
                    customer.setShareLink("clearShareLink");
                    //封装customer
                    getCustomerInfo(brand, customer, cusInfo, subscribe);
                    newCustomerService.dbUpdate(getCurrentBrandId(),customer);
                    if (StringUtils.isNotBlank(customer.getTelephone())) {
                        NewEmployee newEmployee = newEmployeeService.selectByTelephone(customer.getTelephone());
                        if (newEmployee != null) {
                            newEmployee.setWechatAvatar(customer.getHeadPhoto());
                            newEmployeeService.update(newEmployee);
                        }
                    }
                    log.info("邀请链接为：" + shareLink);
                    log.info("是否点过邀请链接" + shareRegister);
                    if (StringUtils.isNotBlank(shareLink) && !"clearShareLink".equalsIgnoreCase(shareLink) && !shareRegister && !isShareLink) {
                        log.info("重定向路径为：" + shareLink);
                        response.sendRedirect(shareLink);
                        return false;
                    }
                } else {
                    state = System.currentTimeMillis() + "";
                    String query = request.getQueryString();
                    query = query == null ? "" : "?" + query;
                    String authorizeUrl = weChatService.getUserAuthorizeUrl(state, request.getRequestURL() + query, wConfig.getAppid());
                    response.sendRedirect(authorizeUrl);
                    return false;
                }
            }
            //------判断自己分享链接结束------------------
            if (customer.getLastOrderShop() != null) {
                ShopDetail shopd = shopMap.get(customer.getLastOrderShop());
                session.setAttribute(SessionKey.CURRENT_SHOP, shopd);
            } else if (shopMap.size() == 1) {
                session.setAttribute(SessionKey.CURRENT_SHOP, shopMap.values().iterator().next());
            }
            if (shopDetailId != null && (usefulTime == null || usefulTime == 0)) {
                ShopDetail shopd = shopMap.get(shopDetailId);
                session.setAttribute(SessionKey.CURRENT_SHOP, shopd);
                customer.setLastOrderShop(shopd.getId());
                customer.setLastTableNumber(tName);
                newCustomerService.dbUpdate(getCurrentBrandId(),customer);
            }
            session.setAttribute(SessionKey.CURRENT_CUSTOMER, customer);

            if (0 == subscribe && brandSetting.getIntoShopSubscribe() == 1 && !"open".equals(returnShop) && shareCustomer == null) {
                log.info("用户关注:" + customer.getSubscribe());
                if (brandSetting.getOpenHttps() == 1) {
                    response.sendRedirect("https://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/subscribe.html?baseUrl=https://" + brand.getBrandSign() + ".restoplus.cn" + (shopDetailId != null ? "&shopId=" + shopDetailId : "") + (tName != null ? "&tableNumber=" + tName : ""));
                } else {
                    response.sendRedirect("http://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/subscribe.html?baseUrl=http://" + brand.getBrandSign() + ".restoplus.cn" + (shopDetailId != null ? "&shopId=" + shopDetailId : "") + (tName != null ? "&tableNumber=" + tName : ""));
                }
                return false;
            } else if (0 == customer.getSubscribe() && brandSetting.getIntoShopSubscribe() == 1 && !"open".equals(returnShop) && StringUtils.isNotBlank(df)) {
                //如果用户未关注公众号并且品牌开启了关注页面在扫个人邀请码进来的时候跳到关注页面
                log.info("扫邀请码进入系统");
                if (brandSetting.getOpenHttps() == 1) {
                    response.sendRedirect("https://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/subscribe.html?df=" + df + "&baseUrl=https://" + brand.getBrandSign() + ".restoplus.cn&shopId=" + shopDetailId + "&shareCustomer=" + shareCustomer + "");
                } else {
                    response.sendRedirect("http://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/subscribe.html?df=" + df + "&baseUrl=http://" + brand.getBrandSign() + ".restoplus.cn&shopId=" + shopDetailId + "&shareCustomer=" + shareCustomer + "");
                }
                return false;
            }
            //判断用户是否存在桌号有效期
            if (usefulTime != null && usefulTime > 0) {
                Long nowDate = new Date().getTime();
                if (((nowDate - usefulTime) / 1000) > 7200) {
                    if (brandSetting.getOpenHttps() == 1) {
                        response.sendRedirect("https://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/views/error.html");
                    } else {
                        response.sendRedirect("http://" + brand.getBrandSign() + ".restoplus.cn/restowechat/src/views/error.html");
                    }
                    return false;
                }
            }
            if (vv != null) {
                Map customerMap = new HashMap(4);
                customerMap.put("brandName", getCurrentBrand().getBrandName());
                customerMap.put("fileName", customer.getId());
                customerMap.put("type", "UserAction");
                customerMap.put("content", "用户:" + customer.getId() + "扫码进入店铺，tableId为:" + tableQrcode.getId() + ",桌号为：" + tableQrcode.getTableNumber() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(customerMap);
            }

            if (requestURL.contains("index")) {
                if (!StringUtils.isEmpty(shopDetailId)) {
                    ShopDetail shop = shopMap.get(shopDetailId);
                    if (shop.getOpenManyCustomerOrder() == Common.YES) {
                        //扫码进入系统时
                        //如果这个桌子下存在组
                        //判断是这个人是否在这个桌子下的一个已支付的组里
                        log.info("扫码进入start");
                        log.info("扫描的桌号为" + tName);
//                TableGroup groupList = tableGroupService.getTableGroupByState(shopDetailId, customer.getId(), tName, 1);
//                if (groupList == null) {
//                    如果不在，那么删除这个人在这个桌子上所有的组关系
//                    customerGroupService.removeGroupByCustomerId(shopDetailId, tName, customer.getId());
//                }
                        //先得到这个店铺下 该桌位 是否已经存在组
                        List<TableGroup> tableGroups = tableGroupService.getTableGroupByShopId(shopDetailId, tName);
                        if (CollectionUtils.isEmpty(tableGroups)) {
                            //如果组未存在，则自动创建
                            String groupId = ApplicationUtils.randomUUID();
                            TableGroup group = new TableGroup();
                            group.setCreateCustomerId(customer.getId());
                            group.setBrandId(brand.getId());
                            group.setGroupId(groupId);
                            group.setShopDetailId(shopDetailId);
                            group.setTableNumber(tName);
                            tableGroupService.insertGroup(group);
                            //创建人与组的关系表
                            CustomerGroup customerGroup = new CustomerGroup();
                            customerGroup.setBrandId(brand.getId());
                            customerGroup.setShopDetailId(shopDetailId);
                            customerGroup.setTableNumber(tName);
                            customerGroup.setIsLeader(Common.YES);
                            customerGroup.setHeadPhoto(customer.getHeadPhoto());
                            customerGroup.setCustomerName(customer.getNickname());
                            customerGroup.setCustomerId(customer.getId());
                            customerGroup.setGroupId(groupId);
                            customerGroupService.insert(customerGroup);
                        }
                    }
                }

            }
            request.setAttribute("pass", true);
            return true;
        }
    }


    /**
     * 通过获取到的数据封装用户信息
     * @param brand
     * @param customer
     * @param cusInfo
     * @param subscribe
     */
    private void getCustomerInfo(Brand brand, Customer customer, JSONObject cusInfo, Integer subscribe){
        customer.setWechatId(cusInfo.getString("openid"));
        customer.setNickname(EmojiFilter.filterEmoji(cusInfo.getString("nickname")));
        customer.setSex(cusInfo.getInt("sex"));
        customer.setProvince(cusInfo.getString("province"));
        customer.setCity(cusInfo.getString("city"));
        customer.setCountry(cusInfo.getString("country"));
        customer.setHeadPhoto(cusInfo.getString("headimgurl"));
        customer.setSubscribe(subscribe);
        customer.setBrandId(brand.getId());
        //获取unionId，当前公众号关联到微信开放平台后才会返回该Id
        String unionId = cusInfo.optString("unionid", null);
        customer.setUnionId(unionId);
        if(unionId != null && !StringUtils.isEmpty(customer.getId())){
            mergeCustomer(unionId, customer);
        }
    }

    private void getCustomerInfoSubscribe(Customer customer, JSONObject cusInfo, Integer subscribe){
        customer.setSubscribe(subscribe);
        String unionId = cusInfo.optString("unionid", null);
        customer.setUnionId(unionId);
        if(unionId != null && !StringUtils.isEmpty(customer.getId())){
            mergeCustomer(unionId, customer);
        }
    }

    //获取除了这个customerId的所有unionId相同的用户  更换绑定优惠券
    public void mergeCustomer(String unionId, Customer customer){
        List<Customer> list = newCustomerService.selectByUnionIdNotCustomerId(getCurrentBrandId(),unionId, customer.getId());
        if(list.size() > 0){
            String telephone = null;
            for(Customer c : list){
                List<Coupon> coupons = couponService.getListByCustomerId(c.getId());
                if(coupons.size() > 0){
                    for(Coupon coupon : coupons){
                        coupon.setCustomerId(customer.getId());
                        couponService.update(coupon);
                    }
                }
                if(c.getIsBindPhone()){
                    telephone = c.getTelephone();
                    c.setTelephone(null);
                    newCustomerService.dbUpdate(c.getBrandId(),c);
                }
                customerService.delete(c.getId());
            }
            if(telephone != null){
                customer.setTelephone(telephone);
                newCustomerService.dbUpdate(customer.getBrandId(),customer);
            }
        }
    }

}
