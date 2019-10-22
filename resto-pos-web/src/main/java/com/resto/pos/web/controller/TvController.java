package com.resto.pos.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.BrandUserService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.pos.web.android.AndroidNotification;
import com.resto.pos.web.android.AndroidUnicast;
import com.resto.pos.web.android.PushClient;
import com.resto.pos.web.config.SessionKey;
import com.resto.pos.web.entity.TvUserData;
import com.resto.pos.web.rpcinterceptors.DataSourceTarget;
import com.resto.shop.web.model.OrderItem;
import com.resto.shop.web.service.OrderItemService;
import com.resto.shop.web.service.TvModeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tv端登录接口，安卓设备          --   2017年1月20日 14:49:15    已废
 * Created by Lmx on 2017/1/19.
 */
@Controller
@RequestMapping("tv")
public class TvController extends GenericController {

    private final String UMENG_APP_KEY = "58d33577734be4250e001758";

    private final String UMENG_APP_MASTER_SECRET = "zh0lmrslljn25hlyb85vkt0eg7ntsp0m";

    @Resource
    OrderItemService orderItemService;
    @Resource
    private BrandUserService brandUserService;
    @Resource
    private BrandSettingService brandSettingService;
    @Resource
    private BrandService brandService;
    @Resource
    private ShopDetailService shopDetailService;
    @Resource
    private TvModeService tvModeService;

    private PushClient client = new PushClient();

//    @RequestMapping("/login")
//    @ResponseBody
//    public Result login(String name, String password, String deviceToken, HttpSession session){
//        if(StringUtils.isEmpty(name)){
//            return new Result("用户名不能为空!",false);
//        }
//        if(StringUtils.isEmpty(password)){
//            return new Result("密码不能为空!",false);
//        }
//        if(StringUtils.isEmpty(deviceToken)){
//            return new Result("Device Token不能为空!",false);
//        }
//        BrandUser loginUser = brandUserService.authentication(new BrandUser(name, ApplicationUtils.pwd(password)));
//        if(loginUser != null){
//            Brand brand =  brandService.selectById(loginUser.getBrandId());
//            BrandSetting brandSetting = brandSettingService.selectByBrandId(loginUser.getBrandId());
//            ShopDetail shopDetail = shopDetailService.selectById(loginUser.getShopDetailId());
//            TvUserData tvdata = new TvUserData();
//            tvdata.setShopId(loginUser.getShopDetailId());
//            tvdata.setBrandId(loginUser.getBrandId());
//            tvdata.setTvShopMode(shopDetail.getTvMode());
//            tvdata.setUrl("http://"+ brand.getServerIp() +"/pos/tv/getOrderItems");
//            shopDetail.setAppkey(UMENG_APP_KEY);
//            shopDetail.setAppMasterSecret(UMENG_APP_MASTER_SECRET);
//            shopDetail.setDeviceToken(deviceToken);
//            shopDetailService.update(shopDetail);
//
//            TvMode tvMode = tvModeService.selectByDeviceShopIdSource(shopDetail.getId(), deviceToken, shopDetail.getTvMode());
//            if(tvMode == null){
//                TvMode tm = new TvMode();
//                if(shopDetail.getTvMode() == TvModeType.UMENG){
//                    tm.setAppKey(UMENG_APP_KEY);
//                    tm.setAppMasterSecret(UMENG_APP_MASTER_SECRET);
//                }
//                tm.setDeviceToken(deviceToken);
//                tm.setAppSource(shopDetail.getTvMode());
//                tm.setShopDetailId(loginUser.getShopDetailId());
//                tm.setBrandId(loginUser.getBrandId());
//                tvModeService.insert(tm);
//            }
//            session.setAttribute(SessionKey.CURRENT_BRAND_SETTING,brandSetting);
//            session.setAttribute(SessionKey.USER_INFO,loginUser);
//            session.setAttribute(SessionKey.CURRENT_SHOP, shopDetail);
//            session.setAttribute(SessionKey.CURRENT_SHOP_ID, shopDetail.getId());
//            session.setAttribute(SessionKey.CURRENT_BRAND, brand);
//            return getSuccessResult(tvdata);
//        }else{
//            return new Result("用户名或密码错误!",false);
//        }
//    }

    @RequestMapping("/login")
    @ResponseBody
    public Result login(String name, String password, String appkey, String appMasterSecret, String deviceToken, HttpSession session){
        if(StringUtils.isEmpty(name)){
            return new Result("用户名不能为空!",false);
        }
        if(StringUtils.isEmpty(password)){
            return new Result("密码不能为空!",false);
        }
        if(StringUtils.isEmpty(appkey)){
            return new Result("appkey不能为空!",false);
        }
        if(StringUtils.isEmpty(appMasterSecret)){
            return new Result("appMasterSecret不能为空!",false);
        }
        if(StringUtils.isEmpty(deviceToken)){
            return new Result("Device Token不能为空!",false);
        }
        BrandUser loginUser = brandUserService.authentication(new BrandUser(name, ApplicationUtils.pwd(password)));
        if(loginUser != null){
            Brand brand =  brandService.selectById(loginUser.getBrandId());
            ShopDetail shopDetail = shopDetailService.selectById(loginUser.getShopDetailId());
            TvUserData tvdata = new TvUserData();
////            tvdata.setIp(brand.getServerIp());
//            tvdata.setIp("192.168.1.110");
//            tvdata.setPort(9999);
            tvdata.setShopId(loginUser.getShopDetailId());
            tvdata.setBrandId(loginUser.getBrandId());
//            String url = "";
////            if("test".equals(brand.getBrandSign())){
////                url = "pos.test.restoplus.cn:8580/pos/tv/getOrderItems";
////            }else{
////                url = "pos.restoplus.cn/pos/tv/getOrderItems";
////            }
//            url = "192.168.1.110:9998/pos/tv/getOrderItems";
//            tvdata.setUrl("http://"+ brand.getServerIp() +":8580/pos/tv/getOrderItems");
            tvdata.setUrl("http://"+ brand.getServerIp() +"/pos/tv/getOrderItems");
            shopDetail.setAppkey(appkey);
            shopDetail.setAppMasterSecret(appMasterSecret);
            shopDetail.setDeviceToken(deviceToken);
            shopDetailService.update(shopDetail);
            session.setMaxInactiveInterval(36000);
            session.setAttribute(SessionKey.USER_INFO,loginUser);
            session.setAttribute(SessionKey.CURRENT_SHOP, shopDetail);
            session.setAttribute(SessionKey.CURRENT_SHOP_ID, shopDetail.getId());
            session.setAttribute(SessionKey.CURRENT_BRAND, brand);
            return getSuccessResult(tvdata);
        }else{
            return new Result("用户名或密码错误!",false);
        }
    }

    @RequestMapping("/getOrderItems")
    @ResponseBody
    public Result getOrderItems(String orderId,String brandId) {
        log.info("orderId:" + orderId);
        log.info("brandId:" + brandId);
        if(StringUtils.isEmpty(orderId)){
            return new Result("orderId不能为空!",false);
        }
        if(StringUtils.isEmpty(brandId)){
            return new Result("brandId不能为空!",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        Map<String, String> param = new HashMap<>();
        param.put("orderId", orderId);
        List<OrderItem> orderItems = orderItemService.listByOrderId(param);
        List<String> list = new ArrayList<>();
        int count = 0;
        for (OrderItem orderItem : orderItems) {
            String articleName = orderItem.getArticleName().trim();
            if(orderItem.getArticleName().trim().length() > 7){
                articleName = orderItem.getArticleName().trim().substring(0,7) + "...";
            }
            list.add(articleName + "\t" + orderItem.getCount() + "份");
            if(orderItem.getParentId()==null || orderItem.getParentId().equals("")){
                count += orderItem.getCount();
            }
        }
        list.add("餐品总计："+count+"份");
        return getSuccessResult(list);
    }

    @RequestMapping("/new")
    @ResponseBody
    public Result testnew(String code,String orderId) {
        testremove(code,orderId);
//        JSONObject data = new JSONObject();
//        data.put("type","new");
//        data.put("code",code);
//        data.put("orderId",orderId);
//        data.put("data","");
//        String key = "31164cebcc4b422685e8d9a32db12ab8";
//        SocketThread.sendmsg(key,data.toString());
        try {
            sendAndroidUnicast("new", code, orderId, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSuccessResult();
    }


    @RequestMapping("/ready")
    @ResponseBody
    public Result testready(String code,String orderId) {
        testremove(code,orderId);
//        JSONObject data = new JSONObject();
//        data.put("type","ready");
//        data.put("code",code);
//        data.put("orderId",orderId);
//        data.put("data","");
//        String key = "31164cebcc4b422685e8d9a32db12ab8";
//        SocketThread.sendmsg(key,data.toString());
        try {
            sendAndroidUnicast("ready", code, orderId, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSuccessResult();
    }

    @RequestMapping("/call")
    @ResponseBody
    public Result testcall(String code,String orderId) {
        testremove(code,orderId);
//        JSONObject data = new JSONObject();
//        data.put("type","call");
//        data.put("code",code);
//        data.put("orderId",orderId);
//        data.put("data","");
//        String key = "31164cebcc4b422685e8d9a32db12ab8";
//        SocketThread.sendmsg(key,data.toString());
        try {
            sendAndroidUnicast("call", code, orderId, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSuccessResult();
    }


    @RequestMapping("/remove")
    @ResponseBody
    public Result testremove(String code,String orderId) {
//        JSONObject data = new JSONObject();
//        data.put("type","remove");
//        data.put("code",code);
//        data.put("orderId",orderId);
//        data.put("data","");
//        String key = "31164cebcc4b422685e8d9a32db12ab8";
//        SocketThread.sendmsg(key,data.toString());
        try {
            sendAndroidUnicast("remove", code, orderId, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSuccessResult();
    }

    public void sendAndroidUnicast(String type, String code, String orderId, String data) throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(getCurrentShop().getAppkey(),getCurrentShop().getAppMasterSecret());
        unicast.setDeviceToken(getCurrentShop().getDeviceToken());
        unicast.setTicker("Android unicast ticker");
        unicast.setTitle("中文的title");
        unicast.setText("Android unicast text");
        unicast.goAppAfterOpen();
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        unicast.setProductionMode();
        // Set customized fields
        unicast.setExtraField("type",type);
        unicast.setExtraField("code",code);
        unicast.setExtraField("orderId",orderId);
        unicast.setExtraField("data",data);
        client.send(unicast);
    }


    /**
     * 新版电视叫号登录接口
     * @param loginname
     * @param password
     * @param session
     * @return
     */
    @RequestMapping("/tvLogin")
    @ResponseBody
    public Result tvLogin(String loginname,String password,HttpSession session){
        BrandUser loginUser = brandUserService.authentication(new BrandUser(loginname, ApplicationUtils.pwd(password)));
        if(loginUser==null){
            return new Result("账户或密码错误~~~",false);
        }else{
            JSONObject obj = new JSONObject();
            ShopDetail shop = shopDetailService.selectById(loginUser.getShopDetailId());
            Brand brand =  brandService.selectById(loginUser.getBrandId());
            obj.put("url",brand.getServerIp());
            obj.put("shopInfo", shop);
            return getSuccessResult(obj);
        }
    }
}
