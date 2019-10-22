package com.resto.shop.web.dto;

import java.io.Serializable;

public class AritclekitchenPrinterDto implements Serializable {
    private Integer id;

    private String articleId;

    private Integer kitchenGroupId;

    private String shopId;

    private String brandId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

    public Integer getKitchenGroupId() {
        return kitchenGroupId;
    }

    public void setKitchenGroupId(Integer kitchenGroupId) {
        this.kitchenGroupId = kitchenGroupId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }
}