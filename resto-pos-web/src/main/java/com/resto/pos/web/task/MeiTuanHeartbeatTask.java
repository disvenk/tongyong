package com.resto.pos.web.task;


import com.alibaba.fastjson.JSON;
import com.resto.brand.core.meituanUtils.MeiTuanUtil;
import com.resto.brand.core.meituanUtils.utils.SignUtils;
import com.resto.brand.core.util.HttpClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 *  系统定时任务
 *  用于定时向美团上报pos服务运行情况（是否正常运行）
 *  时间间隔：30 S
 *  说明：http://developer.meituan.com/openapi#4.3.2
 * Created by Lmx on 2017/3/14.
 */
@Component("meituanHeartbeatTask")
public class MeiTuanHeartbeatTask {

    // TODO: 2017/3/15  基础版本，未做任何错误预测。待完善

//    @Scheduled(cron = "0/30 * *  * * ?")   //每30秒执行一次
    public void reportHeartbeat() {
        //单个上报
        Map<String, String> postParam = new HashMap<>();

        Map<String, String> data = new HashMap<>();
        //	开发者id
        data.put("developerId", MeiTuanUtil.DEVELOPERID);
        //ERP厂商门店id     （以  resto 店铺ID为准）
        data.put("ePoiId", "f48a0a35e0be4dd8aaeb7cf727603958");
        //终端唯一标识        （以服务器 IP 为准）
        data.put("posId", "192.168.1.1");
        //终端上报时间，毫秒时间戳
        data.put("time", Long.toString(System.currentTimeMillis()));

        postParam.put("data",JSON.toJSONString(data));
        postParam.put("sign", SignUtils.createSign(MeiTuanUtil.SIGNKEY,postParam));

        System.out.println(HttpClient.doPost(MeiTuanUtil.HEARTBEAT_URL,postParam));

//        //整体上报
//        Map<String, String> postParam = new HashMap<>();
//
//        Map<String, String> data = new HashMap<>();
//        data.put("developerId", MeiTuanUtil.DEVELOPERID);
//
//        data.put("time", Long.toString(System.currentTimeMillis()));
//        JSONArray list = new JSONArray();
//        for(int i = 0 ; i<=3;i++){
//            Map<String,String> posInfo = new HashMap<>();
//            posInfo.put("ePoiId", "f48a0a35e0be4dd8aaeb7cf727603958---"+i);
//            posInfo.put("posId", "192.168.1.1" + i);
//            list.add(posInfo);
//        }
//
//        data.put("list",list.toJSONString());
//
//        postParam.put("data",JSON.toJSONString(data));
//        postParam.put("sign", SignUtils.createSign(MeiTuanUtil.SIGNKEY,postParam));
//
//        System.out.println(HttpClient.doPost(MeiTuanUtil.HEARTBEAT_URL,postParam));
    }


//    /**
//     * 补充数据上报接口
//     */
//    @Scheduled(cron = "0/30 * *  * * ?")   //每12小时执行一次
//    public void additionalReportHeartbeat() {
//        Map<String, String> postParam = new HashMap<>();
//
//        Map<String, Object> data = new HashMap<>();
//
//        JSONArray list = new JSONArray();
//        for(int i = 0 ; i<10;i++){
//            Map<String,String> posInfo = new HashMap<>();
//            posInfo.put("posId", "192.168.1.1" + 1);
//            posInfo.put("ePoiId", "f48a0a35e0be4dd8aaeb7cf727603958---"+i);
//            posInfo.put("posType", "2");
//            posInfo.put("erpVersion", "V2.2");
//            posInfo.put("ePoiName", "RestoPos");
//            posInfo.put("addr", "张江高科技园区");
//            posInfo.put("phone", "17671111590");
//            list.add(posInfo);
//        }
//
//        data.put("list", list);
//        data.put("total", "10");
//
//        //终端上报时间，毫秒时间戳
//        data.put("time", Long.toString(System.currentTimeMillis()));
//
//        postParam.put("data",JSON.toJSONString(data));
//        postParam.put("developerId",MeiTuanUtil.DEVELOPERID);
//        postParam.put("sign", SignUtils.createSign(MeiTuanUtil.SIGNKEY,postParam));
//
//        System.out.println(HttpClient.doPost(MeiTuanUtil.ADDITIONAL_HEARTBEAT_URL,postParam));
//
//    }

}
