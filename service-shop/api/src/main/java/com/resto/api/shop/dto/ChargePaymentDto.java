package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChargePaymentDto implements Serializable {

    private static final long serialVersionUID = 1676445780332159340L;

    private String id;

    private BigDecimal paymentMoney;

    private Date createTime;

    private String chargeOrderId;

    private String payData;
    
    //关联查询充值订单的内容
    //1.返还的金额
    private BigDecimal rewardMoney;
    
    //关联查询客户内容
    //1.客户的电话
    private String telephone;
    
    //查询品牌的名称 可以直接从session中获取
    private String brandName;
    //查询店铺的名称
    private String shopDetailName;
    
    //记录店铺的id
    private String shopDetailId;

    private Integer isUseBonus;

}