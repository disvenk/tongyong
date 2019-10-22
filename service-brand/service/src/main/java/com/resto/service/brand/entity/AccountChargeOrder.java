package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountChargeOrder implements Serializable {

    private static final long serialVersionUID = -3907258215671303551L;

    private String id;

    private String brandId;

    //订单状态
    private int orderStatus;

    //订单创建时间
    private Date createTime;

    //订单完成时间
    private Date pushOrderTime;

    //充值金额
    private BigDecimal chargeMoney;

    //流水号
    private String tradeNo;

    //支付方式
    private Integer payType;

    //第三方描述
    private String remark;

    //数据状态
    private Boolean status;

}