package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 退菜报表
 */
public class RefundArticleOrder implements Serializable {

    private String shopId;

    private String shopName;

    private String orderId;

    private String tableNumber;

    private String telephone;

    private String nickName;

    private Integer refundCount;

    private BigDecimal refundMoney;

    private String pushOrderTime;

    private List<Map<String, String>> refundArticleOrderList;

    public List<Map<String, String>> getRefundArticleOrderList() {
        return refundArticleOrderList;
    }

    public void setRefundArticleOrderList(List<Map<String, String>> refundArticleOrderList) {
        this.refundArticleOrderList = refundArticleOrderList;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    public BigDecimal getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getPushOrderTime() {
        return pushOrderTime;
    }

    public void setPushOrderTime(String pushOrderTime) {
        this.pushOrderTime = pushOrderTime;
    }

    public RefundArticleOrder(String shopId, String shopName, String orderId, String tableNumber, String telephone, String nickName, Integer refundCount, BigDecimal refundMoney) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.telephone = telephone;
        this.nickName = nickName;
        this.refundCount = refundCount;
        this.refundMoney = refundMoney;
    }

    public RefundArticleOrder() {
    }
}