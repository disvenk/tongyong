package com.resto.brand.core.meituanUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.meituanUtils.utils.SignUtils;
import com.resto.brand.core.util.HttpClient;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lmx on 2017/3/14.
 */
public class MeiTuanUtil {

    /**
     *  测试用的        appAuthToken
     *  映射的店铺：  混合支付  （db8822e4195d43728cfc0fc9478eed51）
     *  美团绑定的店铺：
     */
    public static final String testAppAuthToken = "a38ff0fdbb0aefaff69a703b4492a91362640b31805631e8788daeafeb46dad579cf4b31a93e95d6a69a073045a5e6ee2a5e1919bd34d51b8ba93e4c093f6038";

    /**
     *  测试用的                    ePoiId
     *  关联美团店铺ID：       kfpttest_zl11_17
     *  关联美团外卖店铺ID：kfpttest_zl11_17
     *  映射的店铺：混合支付
     */
    public static final String testEPoiId = "db8822e4195d43728cfc0fc9478eed51";

    /**
     * 开发者ID
     */
    public static final String DEVELOPERID = "100562";

    /**
     * SignKey
     */
    public static final String SIGNKEY = "x0ur5ae3gigj3l7h";

    /**
     * 心跳数据上报地址   URL
     * http://developer.meituan.com/openapi#4.3.2
     */
    public static final String HEARTBEAT_URL = "http://heartbeat.meituan.com/pos/heartbeat";

    /**
     * 补充数据上报接口   URL
     * http://developer.meituan.com/openapi#4.3.3
     */
    public static final String ADDITIONAL_HEARTBEAT_URL = "http://heartbeat.meituan.com/data/pos";

    /**
     * 【闪惠】 根据订单ID查询订单详情   URL
     */
    public static final String SHANHUI_ORDER_QUERYBYID_URL = "http://api.open.cater.meituan.com/shanhui/order/queryById";

    /**
     * 【闪惠】 查询历史订单
     */
    public static final String SHANHUI_ORDER_QUERYLISTBYEPOIID_URL = "http://api.open.cater.meituan.com/shanhui/order/queryListByEpoiId";

    /**
     * 【闪惠】 确认订单    URL
     */
    public static final String SHANHUI_ORDER_CONFIRM_URL = "http://api.open.cater.meituan.com/shanhui/order/confirm";

    /**
     * 【闪惠】 订单退款    URL
     */
    public static  final String SHANHUI_ORDER_REFUND_URL = "http://api.open.cater.meituan.com/shanhui/order/refund";

    /**
     * 【外卖】 菜品列表   URL
     */
    public static  final String WAIMAI_ARTICLE_LIST_URL = "http://api.open.cater.meituan.com/waimai/dish/queryBaseListByEPoiId";


    /**
     * 【外卖】 确认订单   URL
     */
    public static  final String WAIMAI_ORDER_CONFIRM_URL = "http://api.open.cater.meituan.com/waimai/order/confirm";

    /**
     *  用于定时向美团上报pos服务运行情况（是否正常运行）
     * @param ePoiId        ERP厂商门店id     （以  resto 店铺ID为准）
     * @param posId         终端唯一标识        （以当前服务器 IP 为准）
     */
    public static void reportHeartbeat(String ePoiId,String posId){
        Map<String, String> postParam = new HashMap<>();
        JSONObject data = new JSONObject();
        data.put("developerId", DEVELOPERID);
        data.put("ePoiId", ePoiId);
        data.put("posId", posId);
        data.put("time", Long.toString(System.currentTimeMillis()));
        postParam.put("data", data.toJSONString());
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));
        HttpClient.doPost(HEARTBEAT_URL,postParam);
    }

    /**
     * 【闪惠】 根据订单查询订单详情
     * @param shanhuiOrderId        闪惠订单ID
     * @return
     */
    public static String shanhuiOrderQueryById(String shanhuiOrderId,String appAuthToken){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("appAuthToken",appAuthToken);
        postParam.put("charset","UTF-8");
        postParam.put("orderId",shanhuiOrderId);
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost(SHANHUI_ORDER_QUERYBYID_URL,postParam);
    }

    /**
     * 【闪惠】 查询历史订单
     * @param page
     * @param pageSize
     * @param date
     * @return
     */
    public static String shanhuiOrderQueryListByEpoiid(int page,int pageSize,String date,String appAuthToken){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("appAuthToken",appAuthToken);
        postParam.put("charset","UTF-8");
        postParam.put("date","2017-03-15");
        postParam.put("page",   "1");
        postParam.put("pageSize",   "10");
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost(SHANHUI_ORDER_QUERYLISTBYEPOIID_URL,postParam);
    }

    /**
     * 【闪惠】    确认订单
     * @param orderId                     Resto 订单ID
     * @param shanhuiOrderId        闪惠订单ID
     * @return
     */
    public static  String shanhuiOrderConfirm(String orderId,String shanhuiOrderId,String appAuthToken){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("appAuthToken",appAuthToken);
        postParam.put("charset","UTF-8");
        postParam.put("eOrderId",orderId);
        postParam.put("orderId",   shanhuiOrderId);
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost(SHANHUI_ORDER_CONFIRM_URL,postParam);
    }

    /**
     * 【闪惠】 订单退款
     * @param shanhuiOrderId        闪惠订单ID
     * @return
     */
    public static String shanhuiOrderRefund(String shanhuiOrderId,String appAuthToken){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("appAuthToken",appAuthToken);
        postParam.put("orderId",   shanhuiOrderId);
        postParam.put("charset","UTF-8");
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost(SHANHUI_ORDER_REFUND_URL,postParam);
    }

    /**
     * 【外卖】查询美团外卖的菜品列表
     * @param appAuthToken
     * @param ePoiId
     * @return
     */
    public static String waimaiArticleList(String appAuthToken,String ePoiId){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("appAuthToken",appAuthToken);
        postParam.put("charset","UTF-8");
        postParam.put("version","1");
        postParam.put("ePoiId",ePoiId);
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost(WAIMAI_ARTICLE_LIST_URL,postParam);
    }

    /**
     * 【外卖】菜品映射
     * @param appAuthToken
     * @param ePoiId
     * @return
     */
    public static String waimaiArticleMapping(String appAuthToken,String ePoiId){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("appAuthToken",appAuthToken);
        postParam.put("charset","UTF-8");
        postParam.put("version","1");
        postParam.put("ePoiId",ePoiId);
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));

        JSONArray dishMappings = new JSONArray();
        JSONObject info = new JSONObject();
        info.put("dishId","162898790");
        info.put("eDishCode","23c8886d02e2465ab397d0ba5822c3a8");

        JSONObject skuInfo = new JSONObject();
        skuInfo.put("dishSkuId","174745433");
        skuInfo.put("eDishSkuCode","3ce8a24df61e4d3483fe3191a60ef838");

        info.put("waiMaiDishSkuMappings",skuInfo);

        dishMappings.add(info);

        postParam.put("dishMappings",dishMappings.toJSONString());
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost("http://api.open.cater.meituan.com/waimai/dish/mapping",postParam);
    }

    /**
     *  【外卖】    确认订单
     * @param appAuthToken
     * @param orderId
     * @return
     */
    public static String waimaiOrderConfirm(String appAuthToken,String orderId){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("charset","UTF-8");
        postParam.put("appAuthToken",appAuthToken);
        postParam.put("orderId",orderId);
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost(WAIMAI_ORDER_CONFIRM_URL,postParam);
    }


    /**
     *  【外卖】    更改菜品价格
     * @return
     */
    public static String waimaiUpdatePrice(){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("appAuthToken",testAppAuthToken);
        postParam.put("charset","UTF-8");
        postParam.put("version","1");
        postParam.put("ePoiId",testEPoiId);
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));

        JSONObject skus = new JSONObject();
        skus.put("price","0.1");
        skus.put("skuId","3ce8a24df61e4d3483fe3191a60ef838");

        JSONObject info = new JSONObject();
        info.put("eDishCode","23c8886d02e2465ab397d0ba5822c3a8");
        info.put("skus",skus);

        JSONArray dishSkuPrices = new JSONArray();
        dishSkuPrices.add(info);

        postParam.put("dishSkuPrices",dishSkuPrices.toJSONString());
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost("http://api.open.cater.meituan.com/waimai/dish/updatePrice",postParam);
    }

    /**
     *  【外卖】    新增/更改 菜品
     * @return
     */
    public static String waimaiDishBatchUpload(){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("appAuthToken",testAppAuthToken);
        postParam.put("charset","UTF-8");
        postParam.put("version","1");
        postParam.put("ePoiId",testEPoiId);
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));

        JSONArray skus = new JSONArray();

        JSONObject sku1 = new JSONObject();
        sku1.put("skuId","1000_1");
        sku1.put("spec","大份");
        sku1.put("stock","*");
        sku1.put("price",10.0);

        JSONObject sku2 = new JSONObject();
        sku2.put("skuId","1000_2");
        sku2.put("spec","小份");
        sku2.put("stock","*");
        sku2.put("price",13.0);

        skus.add(sku1);
        skus.add(sku2);

        JSONObject info = new JSONObject();
        info.put("boxNum",1);
        info.put("boxPrice",1.0);
        info.put("categoryName","缤纷小食");
        info.put("dishName","片鸭");
        info.put("EDishCode","1000");//Resto+ 菜品ID
        info.put("EpoiId",testEPoiId);
        info.put("isSoldOut",0);
        info.put("minOrderCount",1);
        info.put("picture","http://p1.meituan.net/xianfu/e44a01f8f48663731a82d88a041da25814336.jpg");
        info.put("price",0);
        info.put("unit","份");
        info.put("skus",skus);

        JSONArray dishes = new JSONArray();
        dishes.add(info);

        postParam.put("dishes",dishes.toJSONString());
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost("http://api.open.cater.meituan.com/waimai/dish/batchUpload",postParam);
    }

    /**
     *  【外卖】    开启门店
     * @return
     */
    public static String waimaiPoiOpen(){
        Map<String, String> postParam = new HashMap<>();

        postParam.put("appAuthToken",testAppAuthToken);
        postParam.put("charset","UTF-8");
        postParam.put("timestamp",Long.toString(System.currentTimeMillis()));
        postParam.put("sign", SignUtils.createSign(SIGNKEY,postParam));

        return HttpClient.doPost("http://api.open.cater.meituan.com/waimai/poi/open",postParam);
    }

    public static void main(String[] args) {
//        【闪惠支付】
//        查询订单
//        System.out.println(shanhuiOrderQueryById("80064047779"));
//        查询历史订单
//        System.out.println(shanhuiOrderQueryListByEpoiid(1,10,"2017-10-10"));
//        确认订单      HFEMMF1ODDTW9X78V
//        System.out.println(shanhuiOrderConfirm("110","HFEMMF1ODDTW9X78V"));
//        订单退款
//        System.out.println(shanhuiOrderRefund("HFEMMF1ODDTW9X78V"));
//        一键退款
//        org.json.JSONObject result = new org.json.JSONObject(shanhuiOrderQueryById("80071334299",testAppAuthToken)).getJSONObject("data");
//        System.out.println(shanhuiOrderRefund(result.getString("dpOrderCode"),testAppAuthToken));


//        【美团外卖】
//        菜品列表
//        System.out.println(waimaiArticleList(testAppAuthToken,testEPoiId));
//        菜品映射
//        System.out.println(waimaiArticleMapping(testAppAuthToken,testEPoiId));


    }


}
