package com.resto.shop.web.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class OrderItemActual implements Serializable {

    private Long id;

    private String orderId;

    private String orderItemId;

    private Integer count;

    private Integer type;

    private BigDecimal actualAmount;
}