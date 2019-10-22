package com.resto.brand.core.meituanUtils.domain;

import java.util.List;

/**
 * Created by zhaoxueying on 16/11/3.
 */
public class WaiMaiDishPropertyVO {
    private String eDishCode;
    private List<WaiMaiDishProperty> properties;

    public String geteDishCode() {
        return eDishCode;
    }

    public void seteDishCode(String eDishCode) {
        this.eDishCode = eDishCode;
    }

    public List<WaiMaiDishProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<WaiMaiDishProperty> properties) {
        this.properties = properties;
    }
}