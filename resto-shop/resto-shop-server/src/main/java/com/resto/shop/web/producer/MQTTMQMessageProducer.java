package com.resto.shop.web.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.*;
import com.resto.brand.core.util.MQTTMQStting;
import com.resto.shop.web.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Properties;

/**
 * 支持MQTT消息服务器
 */
@Component
@Slf4j
public class MQTTMQMessageProducer {


    private final static Producer mqttProducer;
    static{
        Properties pro = MQTTMQStting.getPropertiesWithAccessSecret();
        pro.setProperty(PropertyKeyConst.ProducerId,MQTTMQStting.PID_SHOP);
        log.info("启动MQTT消息服务器对应的MQ配置：" + JSON.toJSONString(pro));
        mqttProducer = ONSFactory.createProducer(pro);
        mqttProducer.start();
    }

    /**
     * 1.0创建订单推送消息
     * @param msg
     */
    public static void sendCreateOrderToPosMessage(String msg){
        log.info("创建订单向1.0POS推送消息：" + msg);
        Message message = new Message(MQTTMQStting.TOPIC_RESTO_SHOP,MQTTMQStting.TAG_PLACE_ORDER,msg.getBytes());
        sendMessageASync(message);
    }

    /**
     * 当订单已完成时需要推送到1.0Pos当前订单的金额，用作右上角展示
     * @param msg
     */
    public static void sendOrderCompletedToPosMessage(String msg){
        log.info("推送订单已完成消息：" + msg);
        Message message = new Message(MQTTMQStting.TOPIC_RESTO_SHOP,MQTTMQStting.TAG_PRINT_SUCCESS,msg.getBytes());
        sendMessageASync(message);
    }

    /**
     * 先付模式下现金、银联、积分、闪惠支付需POS端确认支付后执行打单
     * @param msg
     */
    public static void sendConfirmOrderToPosMessage(String msg){
        log.info("推送确认订单消息：" + msg);
        Message message = new Message(MQTTMQStting.TOPIC_RESTO_SHOP,MQTTMQStting.TAG_CONFIRM_ORDER,msg.getBytes());
        sendMessageASync(message);
    }

    /**
     * 当微信、支付宝支付成功后需要将订单推送到Pos
     * @param msg
     */
    public static void sendOrderWxOrAliPaySuccessToPosMessage(String msg){
        log.info("微信或支付宝支付成功推送：" + msg);
        Message message = new Message(MQTTMQStting.TOPIC_RESTO_SHOP,MQTTMQStting.TAG_ORDER_POS_PAY,msg.getBytes());
        sendMessageASync(message);
    }

    /**
     * 推送差评打印订单消息
     * @param msg
     */
    public static void sendBadAppraiseToPosMessage(String msg){
        log.info("推送差评打印订单消息：" + msg);
        Message message = new Message(MQTTMQStting.TOPIC_RESTO_SHOP,MQTTMQStting.TAG_BAD_APPRAISE_PRINT_ORDER,msg.getBytes());
        sendMessageASync(message);
    }

    /**
     * 发票管理推送mq
     * @param msg
     */
    public static void sendReceiptPrintToPosMessage(String msg){
        log.info("发票管理推送mq：" + msg);
        Message message = new Message(MQTTMQStting.TOPIC_RESTO_SHOP,MQTTMQStting.TAG_RECEIPT_PRINT_SUCCESS,msg.getBytes());
        sendMessageASync(message);
    }

    /**
     * R+外卖
     * @param msg
     */
    public static void sendPlatformOrderMessage(String msg){
        log.info("发票管理推送mq：" + msg);
        Message message = new Message(MQTTMQStting.TOPIC_RESTO_SHOP,MQTTMQStting.TAG_PLACE_PLATFORM_ORDER,msg.getBytes());
        sendMessageASync(message);
    }

    /**
     * 创建订单推送至NewPos
     */
    public static void sendCreateOrderToNewPosMessage(Order order, Long delayTime){
        JSONObject msgObj  = new JSONObject();
        msgObj.put("id", order.getId());
        msgObj.put("brandId", order.getBrandId());
        msgObj.put("shopDetailId", order.getShopDetailId());
        log.info("创建订单推送至NewPos：" + msgObj.toJSONString());
        Message message = new Message(MQTTMQStting.TOPIC_RESTO_SHOP,MQTTMQStting.TAG_ORDER_CREATED,msgObj.toJSONString().getBytes());
        Optional.ofNullable(delayTime).ifPresent(aLong -> {
            //拿到支付回调为保证查询订单的时候订单状态已更新成功，当前队列延迟5秒
            message.setStartDeliverTime(System.currentTimeMillis()+aLong);
        });
        sendMessageASync(message);
    }

    /**
     * 微信或支付宝支付成功推送至NewPos
     * @param order
     */
    public static void sendOrderWxOrAliPaySuccessToNewPosMessage(Order order){
        JSONObject msgObj  = new JSONObject();
        msgObj.put("id", order.getId());
        msgObj.put("brandId", order.getBrandId());
        msgObj.put("payMode", order.getPayMode());
        msgObj.put("shopDetailId", order.getShopDetailId());
        log.info("微信或支付宝支付成功推送至NewPos：" + msgObj.toJSONString());
        Message message = new Message(MQTTMQStting.TOPIC_RESTO_SHOP,MQTTMQStting.TAG_ORDER_NEW_POS_PAY,msgObj.toJSONString().getBytes());
        //拿到支付回调为保证查询订单的时候订单状态已更新成功，当前队列延迟5秒
        message.setStartDeliverTime(System.currentTimeMillis() + 5000L);
        sendMessageASync(message);
    }


    private static void sendMessageASync(final Message message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SendResult result = mqttProducer.send(message);
                    log.info("["+message.getTag()+"] "+"发送消息成功，messageId为：" + result.getMessageId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
