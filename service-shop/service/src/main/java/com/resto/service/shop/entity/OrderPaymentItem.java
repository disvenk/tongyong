package com.resto.service.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentItem implements Serializable {

    private static final long serialVersionUID = -6340456597012043868L;

    private String id;

    private Date payTime;

    private BigDecimal payValue;

    private String remark;

    private Integer paymentModeId;

    /**
     * 用于保存支付方式
     */
    private String paymentModeVal;

    private String orderId;

    private String resultData;

    //新增字段用来存营收总额
    private  BigDecimal factIncome;

    //新增用来关联店铺id
    private BigDecimal originalAmount;

    private  String shopDetailId;

    private Integer isUseBonus;

}