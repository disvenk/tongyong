package com.resto.pos.web.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PropertyUtil {

    /**
     * 获取特定路径下的property文件内容
     * @param path
     * @return
     */
    public static Map<String, String> getPropertyValue(String path) {
        Map<String, String> map = new HashMap();
        Properties prop = new Properties();
        try {
            //读取云巴配置文件
            InputStream in = new BufferedInputStream(new FileInputStream(System.getProperty(path)));
            //加载属性列表
            prop.load(in);
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                map.put(key, prop.getProperty(key));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
