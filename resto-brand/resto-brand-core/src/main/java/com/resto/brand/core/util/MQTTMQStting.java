package com.resto.brand.core.util;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * MQTT消息服务器MQ配置
 */
public class MQTTMQStting {

    public static String TOPIC_RESTO_SHOP;
    public static String PID_SHOP;
    public static String CID_SHOP;
    public static String CID_ORDER;
    public static String TOPIC_NAME;//队列名称（用于提示作用）

    //TAG列表Begin
    //订单已完成
    public static final String TAG_PRINT_SUCCESS = "TAG_PRINT_SUCCESS";

    //1.0POS创建订单
    public static final String TAG_PLACE_ORDER="TAG_PLACE_ORDER";

    //NewPos创建订单
    public static final String TAG_ORDER_CREATED= "TAG_ORDER_CREATED";

    //确认订单
    public static final String TAG_CONFIRM_ORDER = "TAG_CONFIRM_ORDER";

    //1.0POS微信、支付宝支付
    public static final String TAG_ORDER_POS_PAY= "TAG_ORDER_POS_PAY";

    //NewPOS微信、支付宝支付
    public static final String TAG_ORDER_NEW_POS_PAY= "TAG_ORDER_NEW_POS_PAY";

    //差评打印订单
    public static final String TAG_BAD_APPRAISE_PRINT_ORDER = "TAG_BAD_APPRAISE_PRINT_ORDER";

    //发票管理推送mq
    public static final String TAG_RECEIPT_PRINT_SUCCESS = "TAG_RECEIPT_PRINT_SUCCESS";

    //外卖
    public static final String TAG_PLACE_PLATFORM_ORDER = "PLACE_PLATFORM_ORDER";
    //TAG列表Begin

    static Logger log = Logger.getLogger(MQTTMQStting.class);

    static {
        String property = System.getProperty("resources.config.path");
        File file = new File(property+"/mqttmq.properties");
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(in);
            TOPIC_RESTO_SHOP = properties.getProperty("topicrestoshop");
            PID_SHOP = properties.getProperty("pidshop");
            CID_SHOP = properties.getProperty("cidshop");
            CID_ORDER = properties.getProperty("cidorder");
            TOPIC_NAME = properties.getProperty("topicname");
            log.info("\n\n【消息队列】："+TOPIC_NAME+"\n\n");
        } catch (IOException e) {
            log.error("\n\n + 未获取到mq的配置文件 + \n\n");
            e.printStackTrace();
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Properties getPropertiesWithAccessSecret(){
        Properties newPro =new Properties();
        newPro.put(PropertyKeyConst.AccessKey, MQSetting.ACCESS_KEY);
        newPro.put(PropertyKeyConst.SecretKey, MQSetting.ACCESS_KEY_SECRET);
        return newPro;
    }
}
