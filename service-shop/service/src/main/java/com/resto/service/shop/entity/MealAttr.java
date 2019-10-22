package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MealAttr implements Serializable {

    private static final long serialVersionUID = 1796820549774973805L;

    private Integer id;

    private String name;

    private Integer sort;

    private String articleId;

    private Integer printSort;

    private Integer choiceType;

    private List<MealItem> mealItems;

    private Integer choiceCount;

}