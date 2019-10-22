package com.resto.brand.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 使用到优惠券的订单信息
 */
@Data
public class UseCouponOrderDto implements Serializable {

    private String couponId;

    private Integer useCount;

    private BigDecimal payValue;

    private String newCustomCuponId;

    private String orderId;

    private BigDecimal OrderMoney;
}
