package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ThirdCustomer implements Serializable {

    private static final long serialVersionUID = 5547220325285500326L;

    private Long id;

    private String telephone;

    private BigDecimal money;

    private String customerId;

    private Integer type;

    private String remark;

}
