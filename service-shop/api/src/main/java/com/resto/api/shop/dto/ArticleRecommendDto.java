package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleRecommendDto implements Serializable {

    private static final long serialVersionUID = 4943586234344327066L;

    private String id;

    private String name;

    private int count;

    private int isUsed;

    private int sort;

    private String shopId;

    private Integer printType;

    private String kitchenId;

    private String imageUrl;

    private Integer choiceType;

    private List<ArticleRecommendPriceDto> articles;

}
