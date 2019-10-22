package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BrandOperationDto implements Serializable {
    private String brandName;
    private String shopName;
    private Integer orderCount;
    private BigDecimal orderMoney;
    private BigDecimal weChatPay;
    private BigDecimal aliPay;
    private Integer customerCount;
    private Integer newCustomerCount;
    private Integer newRegiestCustomerCount;
    private Integer chargeCount;
    private BigDecimal chargeMoney;
    private Map<String, Object> brandOperationCount;
    private List<Map<String, Object>> brandOperationDtos;
    private List<Map<String, Object>> shopOperationDtos;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public BigDecimal getWeChatPay() {
        return weChatPay;
    }

    public void setWeChatPay(BigDecimal weChatPay) {
        this.weChatPay = weChatPay;
    }

    public BigDecimal getAliPay() {
        return aliPay;
    }

    public void setAliPay(BigDecimal aliPay) {
        this.aliPay = aliPay;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public Integer getNewCustomerCount() {
        return newCustomerCount;
    }

    public void setNewCustomerCount(Integer newCustomerCount) {
        this.newCustomerCount = newCustomerCount;
    }

    public Integer getNewRegiestCustomerCount() {
        return newRegiestCustomerCount;
    }

    public void setNewRegiestCustomerCount(Integer newRegiestCustomerCount) {
        this.newRegiestCustomerCount = newRegiestCustomerCount;
    }

    public Integer getChargeCount() {
        return chargeCount;
    }

    public void setChargeCount(Integer chargeCount) {
        this.chargeCount = chargeCount;
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public Map<String, Object> getBrandOperationCount() {
        return brandOperationCount;
    }

    public void setBrandOperationCount(Map<String, Object> brandOperationCount) {
        this.brandOperationCount = brandOperationCount;
    }

    public List<Map<String, Object>> getBrandOperationDtos() {
        return brandOperationDtos;
    }

    public void setBrandOperationDtos(List<Map<String, Object>> brandOperationDtos) {
        this.brandOperationDtos = brandOperationDtos;
    }

    public List<Map<String, Object>> getShopOperationDtos() {
        return shopOperationDtos;
    }

    public void setShopOperationDtos(List<Map<String, Object>> shopOperationDtos) {
        this.shopOperationDtos = shopOperationDtos;
    }
}
