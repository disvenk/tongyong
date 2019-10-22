package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2017/7/11.
 * 通过品牌每个店铺数据累加的数据
 */
public class AllPlatformReportDto implements Serializable{

    private String  brandName;//品牌名称

    private Integer count;//品牌外卖订单数

    private BigDecimal price;//品牌外卖订单额

    private Integer allElmCount;//品牌饿了吗外卖订单数

    private BigDecimal allElmPrice;//品牌饿了吗外卖订单额

    private Integer allMtCount;//品牌美团外卖订单数

    private BigDecimal allMtPrice;//品牌美团外卖订单额

    private Integer allBdCount;//品牌百度外卖订单数

    private BigDecimal allBdPrice;//品牌百度外卖订单额

    private BigDecimal totalPrice; //外卖实收金额

    private BigDecimal elmTotalPrice; //饿了么实收金额

    private BigDecimal mtTotalPrice; //美团外卖实收

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAllElmCount() {
        return allElmCount;
    }

    public void setAllElmCount(Integer allElmCount) {
        this.allElmCount = allElmCount;
    }

    public BigDecimal getAllElmPrice() {
        return allElmPrice;
    }

    public void setAllElmPrice(BigDecimal allElmPrice) {
        this.allElmPrice = allElmPrice;
    }

    public Integer getAllMtCount() {
        return allMtCount;
    }

    public void setAllMtCount(Integer allMtCount) {
        this.allMtCount = allMtCount;
    }

    public BigDecimal getAllMtPrice() {
        return allMtPrice;
    }

    public void setAllMtPrice(BigDecimal allMtPrice) {
        this.allMtPrice = allMtPrice;
    }

    public Integer getAllBdCount() {
        return allBdCount;
    }

    public void setAllBdCount(Integer allBdCount) {
        this.allBdCount = allBdCount;
    }

    public BigDecimal getAllBdPrice() {
        return allBdPrice;
    }

    public void setAllBdPrice(BigDecimal allBdPrice) {
        this.allBdPrice = allBdPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getElmTotalPrice() {
        return elmTotalPrice;
    }

    public void setElmTotalPrice(BigDecimal elmTotalPrice) {
        this.elmTotalPrice = elmTotalPrice;
    }

    public BigDecimal getMtTotalPrice() {
        return mtTotalPrice;
    }

    public void setMtTotalPrice(BigDecimal mtTotalPrice) {
        this.mtTotalPrice = mtTotalPrice;
    }
}
