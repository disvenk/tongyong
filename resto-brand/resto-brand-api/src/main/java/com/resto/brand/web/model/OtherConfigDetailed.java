package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.Date;

public class OtherConfigDetailed implements Serializable {
    private Long id;

    private String data;

    private Date updateTime;

    private String otherConfigSign;

    private String shopDetailId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOtherConfigSign() {
        return otherConfigSign;
    }

    public void setOtherConfigSign(String otherConfigSign) {
        this.otherConfigSign = otherConfigSign == null ? null : otherConfigSign.trim();
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }
}