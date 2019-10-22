package com.resto.api.appraise.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/6/9.
 */
public class AppraiseNewDto implements Serializable{

    private static final long serialVersionUID = 2932139431348157356L;

    private String brandName;

    private Integer brandNum=0;

    private String shopName;

    private Integer shopNum=0;

    private String name;

    private Integer num=0;

    private BigDecimal appraiseRatio = new BigDecimal(0);

    private BigDecimal allLevel = new BigDecimal(0);

    private BigDecimal service = new BigDecimal(0);//服务

    private BigDecimal conditions = new BigDecimal(0);//环境

    private BigDecimal price = new BigDecimal(0);//性价比

    private BigDecimal ambience = new BigDecimal(0);//氛围

    private BigDecimal exhibit = new BigDecimal(0);//出品

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getBrandNum() {
        return brandNum;
    }

    public void setBrandNum(Integer brandNum) {
        this.brandNum = brandNum;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShopNum() {
        return shopNum;
    }

    public void setShopNum(Integer shopNum) {
        this.shopNum = shopNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getAppraiseRatio() {
        return appraiseRatio;
    }

    public void setAppraiseRatio(BigDecimal appraiseRatio) {
        this.appraiseRatio = appraiseRatio;
    }

    public BigDecimal getAllLevel() {
        return allLevel;
    }

    public void setAllLevel(BigDecimal allLevel) {
        this.allLevel = allLevel;
    }

    public BigDecimal getService() {
        return service;
    }

    public void setService(BigDecimal service) {
        this.service = service;
    }

    public BigDecimal getConditions() {
        return conditions;
    }

    public void setConditions(BigDecimal conditions) {
        this.conditions = conditions;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmbience() {
        return ambience;
    }

    public void setAmbience(BigDecimal ambience) {
        this.ambience = ambience;
    }

    public BigDecimal getExhibit() {
        return exhibit;
    }

    public void setExhibit(BigDecimal exhibit) {
        this.exhibit = exhibit;
    }
}
