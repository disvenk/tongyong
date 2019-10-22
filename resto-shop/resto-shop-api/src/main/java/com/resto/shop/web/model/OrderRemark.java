package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class OrderRemark implements Serializable {

    private String id;
    private String boOrderRemarkId;
    private Date createTime;
    private String shopDetailId;
    private String brandId;
    private String remarkName;
    private Integer sort;
    private Integer state;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoOrderRemarkId() {
        return boOrderRemarkId;
    }

    public void setBoOrderRemarkId(String boOrderRemarkId) {
        this.boOrderRemarkId = boOrderRemarkId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public OrderRemark() {
    }

    public OrderRemark(String id, String boOrderRemarkId, Date createTime, String shopDetailId, String brandId) {
        this.id = id;
        this.boOrderRemarkId = boOrderRemarkId;
        this.createTime = createTime;
        this.shopDetailId = shopDetailId;
        this.brandId = brandId;
    }
}
