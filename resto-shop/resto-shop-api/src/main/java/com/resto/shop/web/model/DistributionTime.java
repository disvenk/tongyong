package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class DistributionTime implements Serializable {
    private Integer id;
    
    @DateTimeFormat(pattern="HH:mm")
    private Date beginTime;
    
    @DateTimeFormat(pattern="HH:mm")
    private Date stopOrderTime;

    private String remark;

    private String shopDetailId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getStopOrderTime() {
        return stopOrderTime;
    }

    public void setStopOrderTime(Date stopOrderTime) {
        this.stopOrderTime = stopOrderTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }
}