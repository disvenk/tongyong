package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class Recommend implements Serializable {
    private String id;

    private String name;

    private Integer count;

    private Boolean isUsed;

    private Integer sort;

    private String shopDetailId;

    private Date createTime;

    private Boolean printType;

    private String kitchenId;

    private Boolean choiceType;

    private Boolean openArticleLibrary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getPrintType() {
        return printType;
    }

    public void setPrintType(Boolean printType) {
        this.printType = printType;
    }

    public String getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(String kitchenId) {
        this.kitchenId = kitchenId == null ? null : kitchenId.trim();
    }

    public Boolean getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(Boolean choiceType) {
        this.choiceType = choiceType;
    }

    public Boolean getOpenArticleLibrary() {
        return openArticleLibrary;
    }

    public void setOpenArticleLibrary(Boolean openArticleLibrary) {
        this.openArticleLibrary = openArticleLibrary;
    }
}