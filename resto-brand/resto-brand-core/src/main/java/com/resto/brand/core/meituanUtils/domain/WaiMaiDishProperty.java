package com.resto.brand.core.meituanUtils.domain;

import java.util.List;

/**
 * Created by zhaoxueying on 16/11/3.
 * 外卖菜品属性
 */
public class WaiMaiDishProperty {
    private String propertyName;
    private List<String> values;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}