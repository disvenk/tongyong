package com.resto.service.shop.constant;

/**
 * Created by KONATA on 2017/1/6.
 */
public class OrderPayState {

    public static final int NOT_PAY = 0; //未支付

    public static final int PAYING = 1; //支付中

    public static final int PAYED = 2 ; //已支付

    public final static Integer ALIPAYING = 11;     //选择过支付宝支付  或者   支付宝支付中

    public final static Integer ALIPAYED = 12;     //支付宝支付
}
