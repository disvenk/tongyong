package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class DayAppraiseMessage implements Serializable {
    private String id;

    private String shopName;

    private Date dateTime;

    private Date date;

    private Boolean state;

    private Integer weekDay;

    private String wether;

    private Integer temperature;

    private Integer type;

    private Integer fiveStar;

    private Integer fourStar;

    private Integer oneThreeStar;

    private String daySatisfaction;

    private String xunSatisfaction;

    private String monthSatisfaction;

    private String shopId;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public String getWether() {
        return wether;
    }

    public void setWether(String wether) {
        this.wether = wether == null ? null : wether.trim();
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFiveStar() {
        return fiveStar;
    }

    public void setFiveStar(Integer fiveStar) {
        this.fiveStar = fiveStar;
    }

    public Integer getFourStar() {
        return fourStar;
    }

    public void setFourStar(Integer fourStar) {
        this.fourStar = fourStar;
    }

    public Integer getOneThreeStar() {
        return oneThreeStar;
    }

    public void setOneThreeStar(Integer oneThreeStar) {
        this.oneThreeStar = oneThreeStar;
    }

    public String getDaySatisfaction() {
        return daySatisfaction;
    }

    public void setDaySatisfaction(String daySatisfaction) {
        this.daySatisfaction = daySatisfaction == null ? null : daySatisfaction.trim();
    }

    public String getXunSatisfaction() {
        return xunSatisfaction;
    }

    public void setXunSatisfaction(String xunSatisfaction) {
        this.xunSatisfaction = xunSatisfaction == null ? null : xunSatisfaction.trim();
    }

    public String getMonthSatisfaction() {
        return monthSatisfaction;
    }

    public void setMonthSatisfaction(String monthSatisfaction) {
        this.monthSatisfaction = monthSatisfaction == null ? null : monthSatisfaction.trim();
    }
}