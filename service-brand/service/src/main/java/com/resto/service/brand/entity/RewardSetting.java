package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RewardSetting implements Serializable {

    private static final long serialVersionUID = -8634805456236037164L;

    private String id;

    private String title;

    private String moneyList;

    private Integer minLevel;

    private Integer minLength;

    private Boolean isActivty;

    private String brandId;

}