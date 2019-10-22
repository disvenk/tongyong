package com.resto.shop.web.model;

import java.io.Serializable;

public class MealTempAttr implements Serializable {
    private Integer id;

    private String name;

    private Integer sort;

    private Integer mealTempId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getMealTempId() {
        return mealTempId;
    }

    public void setMealTempId(Integer mealTempId) {
        this.mealTempId = mealTempId;
    }
}