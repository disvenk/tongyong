package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class MealItem implements Serializable {
    private Integer id;

    private String name;

    private Integer sort;

    private String articleName;

    private BigDecimal priceDif;

    private Integer mealAttrId;

    private String articleId;

    private String photoSmall;
    
    private boolean isDefault;

    private Integer kitchenId;
    
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

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName == null ? null : articleName.trim();
    }

    public BigDecimal getPriceDif() {
        return priceDif;
    }

    public void setPriceDif(BigDecimal priceDif) {
        this.priceDif = priceDif;
    }

    public Integer getMealAttrId() {
        return mealAttrId;
    }

    public void setMealAttrId(Integer mealAttrId) {
        this.mealAttrId = mealAttrId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

	public String getPhotoSmall() {
		return photoSmall;
	}

	public void setPhotoSmall(String photoSmall) {
		this.photoSmall = photoSmall;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getKitchenId() {
		return kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}
}