package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountSetting implements Serializable {

    private static final long serialVersionUID = 3405160134754147042L;

    private Long id;

    private Integer accountId;

    private String brandSettingId;
    //新用户注册 0false 1 true
    private Byte openNewCustomerRegister;

    //每个用户单价
    private BigDecimal newCustomerValue;

    //短信发送
    private Byte openSendSms;

    //短信发送单价
    private BigDecimal sendSmsValue;

    //所有订单
    private Byte openAllOrder;

    //所有订单百分比
    private Double allOrderValue;

    //回头消费订单
    private Byte openBackCustomerOrder;

    //回头消费订单百分比
    private Double backCustomerOrderValue;

    //resto外卖订单
    private Byte openOutFoodOrder;
    //resto外卖订单百分比
    private Double outFoodOrderValue;

    //第三方外卖订单
    private Byte openThirdFoodOrder;

    //第三方外卖订单百分比
    private Double thirdFoodOrderValue;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //提醒手机号
    private String telephone;

    //余额提醒设置
    private String remainAccount;//这个参数只是用来做接受参数用的不保存在数据库中

    //是否需要发短信提醒
	private int type;
}