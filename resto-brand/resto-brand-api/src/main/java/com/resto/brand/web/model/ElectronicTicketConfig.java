package com.resto.brand.web.model;

import java.io.Serializable;

public class ElectronicTicketConfig implements Serializable {
    private Long id;

    private String brandId;

    private String appkey;

    private String appsecret;

    private String address;

    private String name;

    private String operator;

    private String payeeRegisterNo;

    private String payeeChecker;

    private String payeeReceiver;

    private String payeePhone;

    private String email;

    private String authorizationKey;

    private String bank;

    private String bankNum;

    private String wechatPayNum;

    public String getWechatPayNum() {
        return wechatPayNum;
    }

    public void setWechatPayNum(String wechatPayNum) {
        this.wechatPayNum = wechatPayNum;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public void setAuthorizationKey(String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey == null ? null : appkey.trim();
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret == null ? null : appsecret.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getPayeeRegisterNo() {
        return payeeRegisterNo;
    }

    public void setPayeeRegisterNo(String payeeRegisterNo) {
        this.payeeRegisterNo = payeeRegisterNo == null ? null : payeeRegisterNo.trim();
    }

    public String getPayeeChecker() {
        return payeeChecker;
    }

    public void setPayeeChecker(String payeeChecker) {
        this.payeeChecker = payeeChecker == null ? null : payeeChecker.trim();
    }

    public String getPayeeReceiver() {
        return payeeReceiver;
    }

    public void setPayeeReceiver(String payeeReceiver) {
        this.payeeReceiver = payeeReceiver == null ? null : payeeReceiver.trim();
    }

    public String getPayeePhone() {
        return payeePhone;
    }

    public void setPayeePhone(String payeePhone) {
        this.payeePhone = payeePhone == null ? null : payeePhone.trim();
    }
}