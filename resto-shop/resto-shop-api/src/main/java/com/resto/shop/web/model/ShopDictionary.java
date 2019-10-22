package com.resto.shop.web.model;

import java.io.Serializable;

public class ShopDictionary implements Serializable {
    private String restoShopid;

    private String weqianShopid;

    public String getRestoShopid() {
        return restoShopid;
    }

    public void setRestoShopid(String restoShopid) {
        this.restoShopid = restoShopid == null ? null : restoShopid.trim();
    }

    public String getWeqianShopid() {
        return weqianShopid;
    }

    public void setWeqianShopid(String weqianShopid) {
        this.weqianShopid = weqianShopid == null ? null : weqianShopid.trim();
    }
}