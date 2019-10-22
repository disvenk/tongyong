package com.resto.brand.core.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.aliyun.openservices.ons.api.PropertyKeyConst;

public class MQSetting {
    public static String TOPIC_RESTO_SHOP;
    public static String PID_SHOP;
    public static String CID_SHOP;
    public static String CID_ORDER;
    public static String TOPIC_NAME;//队列名称（用于提示作用）

//    public static final Boolean OPEN_TASK;  //当前MQ是否开启定时任务

    // 数据库地址
//    private static String URL;
//    private static String DRIVER;
//    private static String USERNAME;
//    private static String PASSWORD;

    static Logger log = Logger.getLogger(MQSetting.class);

    public static final String ACCESS_KEY="7hMxRK4UHVbNbRWu";
    public static final String ACCESS_KEY_SECRET="JhPl9Q1BPDxyM4cPpQoLY92OP5YwBR";

    public static final String TAG_ALL = "*";
    public static final String TAG_CANCEL_ORDER = "CANCEL_ORDER";
    public static final String TAG_AUTO_REFUND_ORDER = "AUTO_REFUND_ORDER";
    public static final String TAG_AUTO_SEND_REMMEND = "AUTO_SEND_REMMEND";
    public static final String TAG_ORDER_PRODUCTION_STATE_CHANGE = "ORDER_PRODUCTION_STATE_CHANGE";
    public static final String TAG_AUTO_CONFIRM_ORDER = "AUTO_CONFIRM_ORDER";
    public static final String TAG_SHOW_ORDER = "SHOW_ORDER";
    public static final String TAG_CHECK_ORDER = "CHECK_ORDER";
    public static final String TAG_NOT_PRINT_ORDER = "NOT_PRINT_ORDER";
    public static final String TAG_NOT_ALLOW_CONTINUE = "NOT_ALLOW_CONTINUE_ORDER";
    public static final String TAG_NOTICE_SHARE_CUSTOMER = "NOTICE_SHARE_CUSTOMER";
    public static final String SEND_CALL_MESSAGE = "SEND_CALL_MESSAGE";
    public static final String TAG_PLACE_PLATFORM_ORDER = "PLACE_PLATFORM_ORDER";
    //下单
    public static final String TAG_PLACE_ORDER="PLACE_ORDER";

    public static final String TAG_PLACE_NOPAY_ORDER="PLACE_NOPAY_ORDER";
    
    //就餐提醒
    public static final String TAG_REMIND_MSG = "REMIND_MSG";

    //取消订单通知
    public static final String TAG_NOTICE_ORDER="NOTICE_ORDER";

    //模式5 （后付款模式） 支付后通知pos端 删除 未支付的订单
    public static final String TAG_DELETE_ORDER="DELETE_ORDER";
    //大boss模式不允许加菜 并且订单可以评论
    public static final String TAG_BOSS_ORDER="TAG_BOSS_ORDER";

    //等位红包订单
    public static final String TAG_QUEUE_ORDER="TAG_QUEUE_ORDER";

    public static final String TAG_PRINT_SUCCESS = "TAG_PRINT_SUCCESS";

    public static final String TAG_BAD_APPRAISE_PRINT_ORDER = "TAG_BAD_APPRAISE_PRINT_ORDER";

    //品牌账户账户欠费提醒
    public static final String TAG_BRAND_ACCOUNT_SEND = "TAG_BRAND_ACCOUNT_SEND";

    //发票管理推送mq
    public static final String TAG_RECEIPT_PRINT_SUCCESS = "TAG_RECEIPT_PRINT_SUCCESS";

    public static final String TAG_SHOP_CHANGE = "TAG_SHOP_CHANGE";

    public static final String TAG_ORDER_CREATED= "TAG_ORDER_CREATED";

    public static final String TAG_ORDER_PAY= "TAG_ORDER_PAY";

    public static final String TAG_ORDER_CANCEL= "TAG_ORDER_CANCEL";

    public static final String TAG_REMOVE_TABLE_GROUP = "TAG_REMOVE_TABLE_GROUP";

    public static final String TAG_SERVER_COMMAND = "TAG_SERVER_COMMAND";// 用于给pos2.0发送指令的消息队列

    public static final String TAG_SHARE_GIVE_MONEY = "TAG_SHARE_GIVE_MONEY";

    //品牌账户欠费提醒时间 也就是欠费后发短信提醒 12小时后再次提醒如果（在这之内充值的钱够则不提醒否则会再次提醒）
	//public static  long DELAY_TIME = 1000*60*60*24;//线上用 24小时
	public static  long DELAY_TIME = 1000*60*60*24;//测试用5分钟 也就是5分钟后才能再发短信

    static {
        String property = System.getProperty("resources.config.path");
        File file = new File(property+"/mq.properties");
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
            //        OPEN_TASK = Integer.parseInt(topicMap.get("topic_name").toString()) == 1 ? true :false;
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


    public static final String DEFAULT_CHAT_SET = "UTF-8"; //默认 字符串序列化编码

    public static Properties getPropertiesWithAccessSecret(){
        Properties newPro =new Properties();
        newPro.put(PropertyKeyConst.AccessKey, MQSetting.ACCESS_KEY);
        newPro.put(PropertyKeyConst.SecretKey, MQSetting.ACCESS_KEY_SECRET);
        return newPro;
    }

//    public static Map<String, Object> getQueueInfo(){
//        Map map = PropertiesUtil.setDb();
//        USERNAME = Encrypter.decrypt(map.get("jdbc.username").toString());
//        PASSWORD = Encrypter.decrypt(map.get("jdbc.password").toString());
//        DRIVER = map.get("jdbc.driver").toString();
//        URL = map.get("jdbc.url").toString();
//        JdbcUtils jdbcUtils = new JdbcUtils(USERNAME, PASSWORD, DRIVER, URL);
//        Connection connection = null;
//        Map<String, Object> result = new HashMap<String, Object>();
//        try {
//            String localIp = getLocalIP();
//            log.info("\n【当前公网IP】\n"+localIp+"\n");
//            connection = jdbcUtils.getConnection();
//            String sql = "select * from brand_queue where ip = ?";
//            List<Object> params = new ArrayList<Object>();
//            params.add(localIp);
//            result = jdbcUtils.findSimpleResult(sql, params);
//        } catch (SQLException e) {
//            log.info("数据库连接错误！");
//            e.printStackTrace();
//        }finally {
//            jdbcUtils.close(connection, null, null);
//            JdbcUtils.close();
//        }
//        if(result.isEmpty()){//判断是否有对应的消息队列
//            log.error("\n\n【未查到当前IP对应的消息队列】\n\n");
//        }
//        return result;
//    }

    /**
     * 获取本机IP地址
     * 在服务器中，获取的为服务器公网IP
     * 在开发环境中，获取的为127.0.0.1
     * @return
     */
    public static String getLocalIP(){
        String SERVER_IP = "";
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (SERVER_IP != null) {
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return SERVER_IP;
    }
}