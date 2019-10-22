package com.resto.shop.web.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SvipActivity implements Serializable{
    private String id;

    private String activityName;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date beginDateTime;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date endDateTime;

    private Long beginTimes;

    private Long endTimes;

    private BigDecimal svipPrice;

    private Integer svipExpire;

    private Integer activityImgType;

    private String activityImg;

    private Byte activityStatus;

    private Date createTime;

    private Integer systemImgType;

    private Integer svipExpireType;

    public Long getBeginTimes() {
        return beginTimes;
    }

    public void setBeginTimes(Long beginTimes) {
        this.beginTimes = beginTimes;
    }

    public Long getEndTimes() {
        return endTimes;
    }

    public void setEndTimes(Long endTimes) {
        this.endTimes = endTimes;
    }

    public Integer getSystemImgType() {
        return systemImgType;
    }

    public void setSystemImgType(Integer systemImgType) {
        this.systemImgType = systemImgType;
    }

    public Integer getSvipExpireType() {
        return svipExpireType;
    }

    public void setSvipExpireType(Integer svipExpireType) {
        this.svipExpireType = svipExpireType;
    }

    public Integer getActivityImgType() {
        return activityImgType;
    }

    public void setActivityImgType(Integer activityImgType) {
        this.activityImgType = activityImgType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
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

    public BigDecimal getSvipPrice() {
        return svipPrice;
    }

    public void setSvipPrice(BigDecimal svipPrice) {
        this.svipPrice = svipPrice;
    }

    public Integer getSvipExpire() {
        return svipExpire;
    }

    public void setSvipExpire(Integer svipExpire) {
        this.svipExpire = svipExpire;
    }

    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg == null ? null : activityImg.trim();
    }

    public Byte getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Byte activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}