package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PaymentReview implements Serializable {
    private String id;

    private String shopId;

    private String operator;

    private Integer paymentModeId;

    private BigDecimal reportMoney;

    private String payModeName;

    private BigDecimal auditMoney;

    private String dailyLogId;

    private Date closeShopTime;

    private Date createTime;

    private Integer serialNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Integer getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(Integer paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public BigDecimal getReportMoney() {
        return reportMoney;
    }

    public void setReportMoney(BigDecimal reportMoney) {
        this.reportMoney = reportMoney;
    }

    public BigDecimal getAuditMoney() {
        return auditMoney;
    }

    public void setAuditMoney(BigDecimal auditMoney) {
        this.auditMoney = auditMoney;
    }

    public String getDailyLogId() {
        return dailyLogId;
    }

    public void setDailyLogId(String dailyLogId) {
        this.dailyLogId = dailyLogId == null ? null : dailyLogId.trim();
    }

    public Date getCloseShopTime() {
        return closeShopTime;
    }

    public void setCloseShopTime(Date closeShopTime) {
        this.closeShopTime = closeShopTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }
}