package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class LaifushiOrderVo implements Serializable {

    private String username;

    private  String password;

    //项目编号
    private String mailId;
    //店铺号
    private  String storeId;
    //收银机号
    private String deviced;
    //收银员ID
    private String casherID;
    //订单号
    private String orderID;
    //订单金额
    private BigDecimal totalFree;
    //实际支付金额
    private BigDecimal settleFree;
    //订单日期时间，格式：2018-04-28 17:39:12
    private String orderDateTime;
    //货品信息
    private List<ProductVo> product;
    //支付信息
    private List<PaymentVo> payment;
    //订单类型，0：支付/ -1 ：退款
    private Integer orderType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getDeviced() {
        return deviced;
    }

    public void setDeviced(String deviced) {
        this.deviced = deviced;
    }

    public String getCasherID() {
        return casherID;
    }

    public void setCasherID(String casherID) {
        this.casherID = casherID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public BigDecimal getTotalFree() {
        return totalFree;
    }

    public void setTotalFree(BigDecimal totalFree) {
        this.totalFree = totalFree;
    }

    public BigDecimal getSettleFree() {
        return settleFree;
    }

    public void setSettleFree(BigDecimal settleFree) {
        this.settleFree = settleFree;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public List<ProductVo> getProduct() {
        return product;
    }

    public void setProduct(List<ProductVo> product) {
        this.product = product;
    }

    public List<PaymentVo> getPayment() {
        return payment;
    }

    public void setPayment(List<PaymentVo> payment) {
        this.payment = payment;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "LaifushiOrderVo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mailId='" + mailId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", deviced='" + deviced + '\'' +
                ", casherID='" + casherID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", totalFree=" + totalFree +
                ", settleFree=" + settleFree +
                ", orderDateTime='" + orderDateTime + '\'' +
                ", product=" + product +
                ", payment=" + payment +
                ", orderType=" + orderType +
                '}';
    }
}
