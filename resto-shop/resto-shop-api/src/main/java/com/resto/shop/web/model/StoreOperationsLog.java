package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class StoreOperationsLog implements Serializable {
    private String id;

    private String brandId;

    private String shopId;

    private String shopName;

    private String storesCode;

    private String operationPeople;

    private Date operationTime;

    private Date dailyTime;

    private Integer cashAuditStatus;

    private Integer uploadJdeStatus;

    private Date messageTime;

    private Date printReportTime;

    private Integer logType;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getStoresCode() {
        return storesCode;
    }

    public void setStoresCode(String storesCode) {
        this.storesCode = storesCode == null ? null : storesCode.trim();
    }

    public String getOperationPeople() {
        return operationPeople;
    }

    public void setOperationPeople(String operationPeople) {
        this.operationPeople = operationPeople == null ? null : operationPeople.trim();
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Date getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(Date dailyTime) {
        this.dailyTime = dailyTime;
    }

    public Integer getCashAuditStatus() {
        return cashAuditStatus;
    }

    public void setCashAuditStatus(Integer cashAuditStatus) {
        this.cashAuditStatus = cashAuditStatus;
    }

    public Integer getUploadJdeStatus() {
        return uploadJdeStatus;
    }

    public void setUploadJdeStatus(Integer uploadJdeStatus) {
        this.uploadJdeStatus = uploadJdeStatus;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public Date getPrintReportTime() {
        return printReportTime;
    }

    public void setPrintReportTime(Date printReportTime) {
        this.printReportTime = printReportTime;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}