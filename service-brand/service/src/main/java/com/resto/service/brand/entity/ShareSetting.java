package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ShareSetting implements Serializable {

    private static final long serialVersionUID = 7915915894408191638L;

    private String id;

    private String shareTitle;

    private String shareIcon;

    private Integer minLevel;

    private Integer minLength;

    private BigDecimal rebate;

    private Boolean isActivity;

    private String brandId;

    private String dialogText;

    private String brandName;

    private BigDecimal minMoney;

    private BigDecimal maxMoney;

    private String registerButton;

    private Integer delayTime;

    private Integer openMultipleRebates;

    private BigDecimal afterRebate;

    private BigDecimal afterMinMoney;

    private BigDecimal afterMaxMoney;

}