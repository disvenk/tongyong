package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.List;

public class MealTemp implements Serializable {
    private Integer id;

    private String name;

    private String brandId;

    private List<MealTempAttr> attrs;
    
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

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

	public List<MealTempAttr> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<MealTempAttr> attrs) {
		this.attrs = attrs;
	}
}