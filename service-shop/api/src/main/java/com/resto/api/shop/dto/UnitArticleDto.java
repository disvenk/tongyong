package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UnitArticleDto implements Serializable {

    private static final long serialVersionUID = -8527259028865208807L;

    private String id;

    private String ArticleId;

    private Integer choiceType;

    private Integer isUsed;

    private String name;

    private Integer sort;

    private String detailName;

    private String detailId;

    private Integer detailSort;

    private BigDecimal detailPrice;

    private Integer detailUsed;

}
