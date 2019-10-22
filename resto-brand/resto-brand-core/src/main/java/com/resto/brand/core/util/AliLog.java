package com.resto.brand.core.util;

import net.sf.json.JSONObject;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class AliLog {

    private static Logger logger = Logger.getLogger(AliLog.class);

    public static void setAliLog(String brandName, String content, String fileName, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("brandName", brandName);
        map.put("content", content);
        map.put("fileName", fileName);
        map.put("type", type);
        JSONObject json = JSONObject.fromObject(map);
//        MDC.put("MsgLog", json.toString());
        logger.special("数据:" + map);

    }
}
