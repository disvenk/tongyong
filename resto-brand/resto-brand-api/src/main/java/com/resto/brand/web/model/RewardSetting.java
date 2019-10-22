package com.resto.brand.web.model;

import java.io.Serializable;

public class RewardSetting implements Serializable {
    private String id;

    private String title;

    private String moneyList;

    private Integer minLevel;

    private Integer minLength;

    private Boolean isActivty;

    private String brandId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(String moneyList) {
        this.moneyList = moneyList == null ? null : moneyList.trim();
    }

    public Integer getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Boolean getIsActivty() {
        return isActivty;
    }

    public void setIsActivty(Boolean isActivty) {
        this.isActivty = isActivty;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }
}