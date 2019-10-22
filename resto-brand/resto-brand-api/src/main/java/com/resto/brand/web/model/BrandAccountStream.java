package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BrandAccountStream implements Serializable {

    private Long id;

    private Long accountId;

    private String brandId;

    private Date createTime;

    private String remark;

    private BigDecimal totalAccount;

    private String userName;

    private BigDecimal addAccount;

    private BigDecimal remainAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public BigDecimal getTotalAccount() {
        return totalAccount;
    }

    public void setTotalAccount(BigDecimal totalAccount) {
        this.totalAccount = totalAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public BigDecimal getAddAccount() {
        return addAccount;
    }

    public void setAddAccount(BigDecimal addAccount) {
        this.addAccount = addAccount;
    }

    public BigDecimal getRemainAccount() {
        return remainAccount;
    }

    public void setRemainAccount(BigDecimal remainAccount) {
        this.remainAccount = remainAccount;
    }
}