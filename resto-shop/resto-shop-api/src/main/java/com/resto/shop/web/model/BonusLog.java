package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class BonusLog implements Serializable {
    private String id;

    private String chargeOrderId;

    private String bonusSettingId;

    private Integer bonusAmount;

    private Integer state;

    private Integer shopownerBonusAmount;

    private Integer employeeBonusAmount;

    private String shopownerId;

    private String employeeId;

    private Date createTime;

    private String wishing;

    private Integer shopownerIssuingState;

    private Integer employeeIssuingState;

    private Integer amountDisbursed;

    private Integer employeeAmountDisbursed;

    private Integer shopownerAmountDisbursed;

    public Integer getAmountDisbursed() {
        return amountDisbursed;
    }

    public void setAmountDisbursed(Integer amountDisbursed) {
        this.amountDisbursed = amountDisbursed;
    }

    public Integer getEmployeeAmountDisbursed() {
        return employeeAmountDisbursed;
    }

    public void setEmployeeAmountDisbursed(Integer employeeAmountDisbursed) {
        this.employeeAmountDisbursed = employeeAmountDisbursed;
    }

    public Integer getShopownerAmountDisbursed() {
        return shopownerAmountDisbursed;
    }

    public void setShopownerAmountDisbursed(Integer shopownerAmountDisbursed) {
        this.shopownerAmountDisbursed = shopownerAmountDisbursed;
    }

    public Integer getEmployeeIssuingState() {
        return employeeIssuingState;
    }

    public void setEmployeeIssuingState(Integer employeeIssuingState) {
        this.employeeIssuingState = employeeIssuingState;
    }

    public Integer getShopownerIssuingState() {
        return shopownerIssuingState;
    }

    public void setShopownerIssuingState(Integer shopownerIssuingState) {
        this.shopownerIssuingState = shopownerIssuingState;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getChargeOrderId() {
        return chargeOrderId;
    }

    public void setChargeOrderId(String chargeOrderId) {
        this.chargeOrderId = chargeOrderId == null ? null : chargeOrderId.trim();
    }

    public String getBonusSettingId() {
        return bonusSettingId;
    }

    public void setBonusSettingId(String bonusSettingId) {
        this.bonusSettingId = bonusSettingId == null ? null : bonusSettingId.trim();
    }

    public Integer getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Integer bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getShopownerBonusAmount() {
        return shopownerBonusAmount;
    }

    public void setShopownerBonusAmount(Integer shopownerBonusAmount) {
        this.shopownerBonusAmount = shopownerBonusAmount;
    }

    public Integer getEmployeeBonusAmount() {
        return employeeBonusAmount;
    }

    public void setEmployeeBonusAmount(Integer employeeBonusAmount) {
        this.employeeBonusAmount = employeeBonusAmount;
    }

    public String getShopownerId() {
        return shopownerId;
    }

    public void setShopownerId(String shopownerId) {
        this.shopownerId = shopownerId == null ? null : shopownerId.trim();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId == null ? null : employeeId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}