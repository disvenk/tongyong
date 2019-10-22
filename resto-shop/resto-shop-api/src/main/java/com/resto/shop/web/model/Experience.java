package com.resto.shop.web.model;

import java.io.Serializable;

public class Experience implements Serializable {
    private Integer id;

    private Integer showType;

    private String title;

    private String picUrl;

    private String shopDetailId;

    private String photoSquare;

    private Integer showSort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public String getPhotoSquare() {
        return photoSquare;
    }

    public void setPhotoSquare(String photoSquare) {
        this.photoSquare = photoSquare == null ? null : photoSquare.trim();
    }

    public Integer getShowSort() {
        return showSort;
    }

    public void setShowSort(Integer showSort) {
        this.showSort = showSort;
    }
}