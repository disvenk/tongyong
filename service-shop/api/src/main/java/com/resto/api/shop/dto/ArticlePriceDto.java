package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ArticlePriceDto implements Serializable {

    private static final long serialVersionUID = 8790525684239456941L;

    private String id;

    private String unitIds;

    private BigDecimal price;

    private BigDecimal fansPrice;

    private String name;

    private String peference;

    private Integer sort;

    private String articleId;

    private Integer stockWorkingDay;

    private Integer stockWeekend;

    private Integer currentWorkingStock;

}