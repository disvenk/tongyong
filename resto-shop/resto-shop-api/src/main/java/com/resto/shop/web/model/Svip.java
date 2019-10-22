package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Svip implements Serializable{
    private String id;

    private BigDecimal chargeMoney;

    private Date beSvipTime;

    private Integer svipExpire;

    private Date beginDateTime;

    private Date endDateTime;

    private String activityId;

    private String customerId;

    private Integer svipExpireType;

    private String shopDetailId;

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public Integer getSvipExpireType() {
        return svipExpireType;
    }

    public void setSvipExpireType(Integer svipExpireType) {
        this.svipExpireType = svipExpireType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public Date getBeSvipTime() {
        return beSvipTime;
    }

    public void setBeSvipTime(Date beSvipTime) {
        this.beSvipTime = beSvipTime;
    }

    public Integer getSvipExpire() {
        return svipExpire;
    }

    public void setSvipExpire(Integer svipExpire) {
        this.svipExpire = svipExpire;
    }

    public Date getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(Date beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }
}