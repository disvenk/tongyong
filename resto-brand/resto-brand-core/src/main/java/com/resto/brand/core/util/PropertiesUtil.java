package com.resto.brand.core.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by KONATA on 2017/9/15.
 */
public class PropertiesUtil {

    public static Map<String, String> setDb() {
        Map map = new HashMap(4);
        Properties prop = new Properties();
        try {
            //读取属性文件a.properties

            InputStream in = new BufferedInputStream(new FileInputStream(System.getProperty("dbpath")));
            prop.load(in);     ///加载属性列表
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                map.put(key, prop.getProperty(key));
            }
            in.close();


        } catch (Exception e) {
            System.out.println(e);
        }
        return map;
    }


}
