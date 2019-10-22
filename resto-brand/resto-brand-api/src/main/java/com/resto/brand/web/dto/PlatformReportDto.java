package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by xielc on 2017/7/11.
 * 用于第三方外卖报表统计
 */
public class PlatformReportDto implements Serializable {

    private String shopId;//店铺id

    private String shopName;//店铺名称

    private Integer allCount;//第三方外卖订单数

    private BigDecimal allPrice;//第三方外卖订单额

    private Integer elmCount;//饿了吗外卖订单数

    private BigDecimal elmPrice;//饿了吗外卖订单额

    private Integer mtCount;//美团外卖订单数

    private BigDecimal mtPrice;//美团外卖订单额

    private Integer bdCount;//百度外卖订单数

    private BigDecimal bdPrice;//百度外卖订单额

    private BigDecimal totalPrice; //外卖实收金额

    private BigDecimal elmTotalPrice; //饿了么实收金额

    private BigDecimal mtTotalPrice; //美团外卖实收

    private Map<String, Object> brandAppraise;

    private List<Map<String, Object>> shopAppraises;

    public Map<String, Object> getBrandAppraise() {
        return brandAppraise;
    }

    public void setBrandAppraise(Map<String, Object> brandAppraise) {
        this.brandAppraise = brandAppraise;
    }

    public List<Map<String, Object>> getShopAppraises() {
        return shopAppraises;
    }

    public void setShopAppraises(List<Map<String, Object>> shopAppraises) {
        this.shopAppraises = shopAppraises;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public BigDecimal getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(BigDecimal allPrice) {
        this.allPrice = allPrice;
    }

    public Integer getElmCount() {
        return elmCount;
    }

    public void setElmCount(Integer elmCount) {
        this.elmCount = elmCount;
    }

    public BigDecimal getElmPrice() {
        return elmPrice;
    }

    public void setElmPrice(BigDecimal elmPrice) {
        this.elmPrice = elmPrice;
    }

    public Integer getMtCount() {
        return mtCount;
    }

    public void setMtCount(Integer mtCount) {
        this.mtCount = mtCount;
    }

    public BigDecimal getMtPrice() {
        return mtPrice;
    }

    public void setMtPrice(BigDecimal mtPrice) {
        this.mtPrice = mtPrice;
    }

    public Integer getBdCount() {
        return bdCount;
    }

    public void setBdCount(Integer bdCount) {
        this.bdCount = bdCount;
    }

    public BigDecimal getBdPrice() {
        return bdPrice;
    }

    public void setBdPrice(BigDecimal bdPrice) {
        this.bdPrice = bdPrice;
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
