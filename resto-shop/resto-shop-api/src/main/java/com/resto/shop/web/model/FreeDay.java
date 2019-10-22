package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class FreeDay implements Serializable {
    private String id;
    
    @DateTimeFormat(pattern="yy-MM-dd")
    private Date freeDay;
    @DateTimeFormat(pattern="yy-MM-dd")
    private Date begin;
    @DateTimeFormat(pattern="yy-MM-dd")
    private Date end;
    private String shopDetailId;
    
    
    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFreeDay() {
        return freeDay;
    }

    public void setFreeDay(Date freeDay) {
        this.freeDay = freeDay;
    }
    
    
    
}