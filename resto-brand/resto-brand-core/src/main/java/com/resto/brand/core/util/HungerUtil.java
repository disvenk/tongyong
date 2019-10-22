package com.resto.brand.core.util;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KONATA on 2016/10/27.
 * 饿了吗 订单接口
 */

public class HungerUtil {
    /**
     * consumer_key: 0170804777
     consumer_secret: 87217cb263701f90316236c4df00d9352fb1da76

     restaurant_id: 62028381
     restaurant_name: 饿了么开放平台测试
     餐厅下单测试地址: https://www.ele.me/shop/25381

     */


    public static final String baseUrl = "http://v2.openapi.ele.me";

    public static String HungerConnection(Map<String,String> map,String url,String consumerKey,String consumerSecret,String method) throws UnsupportedEncodingException, NoSuchAlgorithmException {


        String time = new Date().getTime()+"";
        map.put("consumer_key",consumerKey);
        map.put("timestamp",time);
        String sign =  ElemeHelper.genSig(baseUrl+url,map,consumerSecret);
        StringBuffer requestUrl = new StringBuffer();
        String str = ElemeHelper.concatParams(map);

        requestUrl.append(baseUrl).append(url).append("?")
                .append(str)
                .append("&sig=").append(sign);

        String result = "";
        switch (method){
            case "put":
                result = HttpRequest.put(requestUrl.toString()).body();
                break;
            case "get":
                result = HttpRequest.get(requestUrl.toString()).body();
                break;
            case "post":
                result = HttpRequest.post(requestUrl.toString()).body();
                break;
            case "delete":
                result = HttpRequest.delete(requestUrl.toString()).body();
                break;
            default:
                break;
        }

        return result;

    }




    public static void main(String [] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Map<String, String> map = new HashMap<>();
        map.put("restaurant_ids","1242922");
//        map.put("order_mode","1");
        String result = HungerConnection(map,"/restaurants/batch_status/","0450724319","208e7bc8063e9dff2f77da9868a1653f23d364c2","get");
        JSONObject object = new JSONObject(result);



//        String result = HungerConnection(map,"/order/101430198581739386/","0450724319","208e7bc8063e9dff2f77da9868a1653f23d364c2");

        System.out.println(result);
    }



}
