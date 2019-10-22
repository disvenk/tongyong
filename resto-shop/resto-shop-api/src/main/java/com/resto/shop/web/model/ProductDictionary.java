package com.resto.shop.web.model;

import java.io.Serializable;

public class ProductDictionary implements Serializable {
    private String restoProductid;

    private String weiqianProduct;

    private String weiqianShopid;

    public String getRestoProductid() {
        return restoProductid;
    }

    public void setRestoProductid(String restoProductid) {
        this.restoProductid = restoProductid == null ? null : restoProductid.trim();
    }

    public String getWeiqianProduct() {
        return weiqianProduct;
    }

    public void setWeiqianProduct(String weiqianProduct) {
        this.weiqianProduct = weiqianProduct == null ? null : weiqianProduct.trim();
    }

    public String getWeiqianShopid() {
        return weiqianShopid;
    }

    public void setWeiqianShopid(String weiqianShopid) {
        this.weiqianShopid = weiqianShopid == null ? null : weiqianShopid.trim();
    }
}