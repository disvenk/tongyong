package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MealAttrDto implements Serializable {

    private static final long serialVersionUID = 1796820549774973805L;

    private Integer id;

    private String name;

    private Integer sort;

    private String articleId;

    private Integer printSort;

    private Integer choiceType;

    private List<MealItemDto> mealItems;

    private Integer choiceCount;

}