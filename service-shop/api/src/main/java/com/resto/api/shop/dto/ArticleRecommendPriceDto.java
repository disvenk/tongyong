package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ArticleRecommendPriceDto implements Serializable {

    private static final long serialVersionUID = -4545766342137683750L;

    private String id;

    private String recommendId;

    private String articleId;

    private Integer maxCount;

    private String articleName;

    private Integer sort;

    private Date createTime;

    private BigDecimal price;

    private String kitchenId;

    private String imageUrl;

}
