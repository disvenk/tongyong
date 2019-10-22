package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.List;

public class WeShopInfo implements Serializable {
    private Long id;

    private String title;

    private String detailTitle;

    private String flag;

    private String shopId;

    private String brandId;

    private Long index;

    private List<WeOrder> weOrders;

    public List<WeOrder> getWeOrders() {
        return weOrders;
    }

    public void setWeOrders(List<WeOrder> weOrders) {
        this.weOrders = weOrders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle == null ? null : detailTitle.trim();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
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

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }
}