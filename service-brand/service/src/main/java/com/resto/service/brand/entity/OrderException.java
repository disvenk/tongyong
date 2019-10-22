package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderException implements Serializable {

    private static final long serialVersionUID = 6151020256278806481L;

    private  int id;

    private String orderId;

    private BigDecimal orderMoney;

    private String why;

    private String brandName;

    private String shopName;

    private Date createTime;

}