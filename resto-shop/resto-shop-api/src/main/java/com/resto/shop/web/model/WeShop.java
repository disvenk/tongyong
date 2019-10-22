package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.List;

public class WeShop implements Serializable {
    private Long id;

    private String brandId;

    private String shopId;

    private String shopName;

    List<WeShopScore> weShopScoreList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public List<WeShopScore> getWeShopScoreList() {
        return weShopScoreList;
    }

    public void setWeShopScoreList(List<WeShopScore> weShopScoreList) {
        this.weShopScoreList = weShopScoreList;
    }
}