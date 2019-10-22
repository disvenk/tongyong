package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BrandAccountStream implements Serializable {

    private static final long serialVersionUID = -938689081870644156L;

    private Long id;

    private Long accountId;

    private String brandId;

    private Date createTime;

    private String remark;

    private BigDecimal totalAccount;

    private String userName;

    private BigDecimal addAccount;

    private BigDecimal remainAccount;

}