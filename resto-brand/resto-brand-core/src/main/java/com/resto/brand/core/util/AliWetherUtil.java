package com.resto.brand.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yz on 2017-05-26.
 */
public class AliWetherUtil {
    public final static String AppCode = "bef95664f60a426bad1410d2910f1319";
    public  final static String host = "https://ali-weather.showapi.com";//
    public  final static String path = "/gps-to-weather";//GPS经纬度坐标查询7天预报详情
    public  final static String historyPath = "/weatherhistory";
    public  final static String method = "GET";

    public  static void   getWetherHistory(String area,String time){
        String keyTime = time.replaceAll("-","");//用来作对比
        String timeTemp = time.replaceAll("-","").substring(0,6);
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + AppCode);
        Map<String, String> querys = new HashMap<>();
        querys.put("area",area);
        //querys.put("areaid", "101291401");
        querys.put("month", "201601");
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, historyPath, method, headers, querys);
            //获取response的body
            String result = EntityUtils.toString(response.getEntity());
            if (result != null) {
                JSONObject obj = JSONObject.parseObject(result);
                    /*获取返回状态码*/
                result = obj.getString("showapi_res_code");
                if (result != null && result.equals("0")) {
                    String showapi_res_body = obj.getString("showapi_res_body");
                    JSONObject showapi_res_json = JSONObject.parseObject(showapi_res_body);
                    JSONArray jsonArray = JSONArray.parseArray(showapi_res_json.getString("list"));
                    if(!jsonArray.isEmpty()){
                        for(int i=0;i<jsonArray.size();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(keyTime.equals(jsonObject.getString("time"))){

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }


    public  static JSONObject   getWetherGps(String laitude,String longitude){

        JSONObject jsonObject = new JSONObject();
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + AppCode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("from", "5");
        querys.put("lat", laitude);//纬度
        querys.put("lng",longitude);//经度
        querys.put("need3HourForcast", "0");//是否需要当天每3/6小时一次的天气预报列表。1为需要，0为不
        querys.put("needAlarm", "0");//是否需要天气预警。1为需要，0为不需要。
        querys.put("needHourData", "0");//是否需要每小时数据的累积数组。由于本系统是半小时刷一次实时状态，因此实时数组最大长度为48。每天0点长度初始化为0. 1为需要 0为不
        querys.put("needIndex", "0");//是否需要返回指数数据，比如穿衣指数、紫外线指数等。1为返回，0为不返回。
        querys.put("needMoreDay", "0");//是否需要返回7天数据中的后4天。1为返回，0为不返回。
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //获取response的body
            String result = EntityUtils.toString(response.getEntity());
            if (result != null) {
                JSONObject obj = JSONObject.parseObject(result);
                    /*获取返回状态码*/
                result = obj.getString("showapi_res_code");
                      /*如果状态码是0说明返回数据成功*/
                if (result != null && result.equals("0")) {
                    String showapi_res_body = obj.getString("showapi_res_body");
                    JSONObject bodyJson = JSON.parseObject(showapi_res_body);
                    JSONObject cityInfoJson = JSON.parseObject(bodyJson.getString("cityInfo"));//城市信息
                    JSONObject f1Json = JSON.parseObject(bodyJson.getString("f1"));//今天天气情况
                    System.out.println(JSONObject.toJSONString(f1Json));
                    jsonObject = f1Json;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static void main(String[] args) {
        getWetherGps("31.246187","121.444644");//测试gps查询

        //getWetherHistory("丽江","2016-01-02");


    }


}
