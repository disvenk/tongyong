package com.resto.shop.web;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ShopServerRpcBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopServerRpcBootstrap.class);

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        LOGGER.info("start server");
        for (String arg : args) {
            if (arg.contains("=")) {
                String[] kv = arg.split("=");
                String key = kv[0].replaceAll("-D", "");
                String value = StringUtils.trimToEmpty(kv[1]);
                LOGGER.info("set default property: " + key + "=" + value);
                System.setProperty(key, value);
            }
        }

        @SuppressWarnings("unused")
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext.xml"});
        context.start();
        System.in.read();
        LOGGER.info("start server success");
    }
}
