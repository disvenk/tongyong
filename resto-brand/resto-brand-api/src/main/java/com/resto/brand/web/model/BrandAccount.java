package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BrandAccount implements Serializable {

    //账户id
    private Integer id;

    //品牌id
    private String brandId;

    //品牌设置id

    private String brandSettingId;

    //账户余额
    private BigDecimal accountBalance;

    //已经使用
    private BigDecimal amountUsed;


    //发票总额
    private BigDecimal totalInvoiceAmount;

    //已开发票金额
    private BigDecimal usedInvoiceAmount;

    //剩余可开发票金额
    private BigDecimal remainedInvoiceAmount;

    //最开始开启品牌账户的时间
    private Date createTime;

    //更新品牌账户的时间

    private Date updateTime;

    private String brandName;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getAmountUsed() {
        return amountUsed;
    }

    public void setAmountUsed(BigDecimal amountUsed) {
        this.amountUsed = amountUsed;
    }


    public BigDecimal getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }

    public BigDecimal getUsedInvoiceAmount() {
        return usedInvoiceAmount;
    }

    public void setUsedInvoiceAmount(BigDecimal usedInvoiceAmount) {
        this.usedInvoiceAmount = usedInvoiceAmount;
    }

    public BigDecimal getRemainedInvoiceAmount() {
        return remainedInvoiceAmount;
    }

    public void setRemainedInvoiceAmount(BigDecimal remainedInvoiceAmount) {
        this.remainedInvoiceAmount = remainedInvoiceAmount;
    }

    public String getBrandSettingId() {
        return brandSettingId;
    }

    public void setBrandSettingId(String brandSettingId) {
        this.brandSettingId = brandSettingId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}