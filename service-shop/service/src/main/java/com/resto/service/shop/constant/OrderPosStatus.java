package com.resto.service.shop.constant;

/**
 * Created by KONATA on 2017/3/22.
 */
public class OrderPosStatus {

    public static final String SEND_MSG_SUCCESS = "sendMsgSuccess"; //发送消息队列成功

    public static final String RECEIVE_MSG_SUCCESS = "getMsgSuccess"; //消息队列接收成功

    public static final String SEND_ORDER_SUCCESS = "sendOrderSuccess"; //pos端服务器发送订单成功

    public static final String RECEIVE_ORDER_SUCCESS = "receiveOrderSuccess"; //pos端客户端接收订单成功

    public static final String PRINT_SUCCESS = "orderPrintSuccess";
}
