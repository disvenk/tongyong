package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountSettingDto implements Serializable {

    private static final long serialVersionUID = -5515191724628540043L;

    private Long id;

    private Integer accountId;

    private String brandSettingId;

    @ApiModelProperty(value = "新用户注册 0false 1 true")
    private Byte openNewCustomerRegister;

    @ApiModelProperty(value = "每个用户单价")
    private BigDecimal newCustomerValue;

    @ApiModelProperty(value = "短信发送")
    private Byte openSendSms;

    @ApiModelProperty(value = "短信发送单价")
    private BigDecimal sendSmsValue;

    @ApiModelProperty(value = "所有订单")
    private Byte openAllOrder;

    @ApiModelProperty(value = "所有订单百分比")
    private Double allOrderValue;

    @ApiModelProperty(value = "回头消费订单")
    private Byte openBackCustomerOrder;

    @ApiModelProperty(value = "回头消费订单百分比")
    private Double backCustomerOrderValue;

    @ApiModelProperty(value = "resto外卖订单")
    private Byte openOutFoodOrder;

    @ApiModelProperty(value = "resto外卖订单百分比")
    private Double outFoodOrderValue;

    @ApiModelProperty(value = "第三方外卖订单")
    private Byte openThirdFoodOrder;

    @ApiModelProperty(value = "第三方外卖订单百分比")
    private Double thirdFoodOrderValue;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "提醒手机号")
    private String telephone;

    @ApiModelProperty(value = "余额提醒设置")
    private String remainAccount;//这个参数只是用来做接受参数用的不保存在数据库中

    @ApiModelProperty(value = "是否需要发短信提醒")
	private int type;
}