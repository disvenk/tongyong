package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MealItem implements Serializable {

    private static final long serialVersionUID = 7341228705803329126L;

    private Integer id;

    private String name;

    private Integer sort;

    private String articleName;

    private BigDecimal priceDif;

    private Integer mealAttrId;

    private String articleId;

    private String photoSmall;
    
    private boolean isDefault;

    private Integer kitchenId;

}