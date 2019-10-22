package com.resto.shop.web.model;

import java.io.Serializable;

public class KitchenGroupDetail  implements Serializable {
    private Integer id;

    private Integer kitchenId;

    private Integer kitchenGroupId;

    private String shopDetailId;

    private String brandId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(Integer kitchenId) {
        this.kitchenId = kitchenId;
    }

    public Integer getKitchenGroupId() {
        return kitchenGroupId;
    }

    public void setKitchenGroupId(Integer kitchenGroupId) {
        this.kitchenGroupId = kitchenGroupId;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }
}