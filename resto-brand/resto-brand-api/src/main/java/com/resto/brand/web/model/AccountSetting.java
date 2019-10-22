package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AccountSetting implements Serializable {
    private static final long serialVersionUID = 3405160134754147042L;
    private Long id;

    private Integer accountId;

    private String brandSettingId;
    //新用户注册 0false 1 true
    private Byte openNewCustomerRegister;

    //每个用户单价
    private BigDecimal newCustomerValue;

    //短信发送
    private Byte openSendSms;

    //短信发送单价
    private BigDecimal sendSmsValue;

    //所有订单
    private Byte openAllOrder;

    //所有订单百分比
    private Double allOrderValue;

    //回头消费订单
    private Byte openBackCustomerOrder;

    //回头消费订单百分比
    private Double backCustomerOrderValue;

    //resto外卖订单
    private Byte openOutFoodOrder;
    //resto外卖订单百分比
    private Double outFoodOrderValue;

    //第三方外卖订单
    private Byte openThirdFoodOrder;

    //第三方外卖订单百分比
    private Double thirdFoodOrderValue;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //提醒手机号
    private String telephone;

    //余额提醒设置
    private String remainAccount;//这个参数只是用来做接受参数用的不保存在数据库中

    //是否需要发短信提醒
	private int type;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getBrandSettingId() {
        return brandSettingId;
    }

    public void setBrandSettingId(String brandSettingId) {
        this.brandSettingId = brandSettingId;
    }

    public Byte getOpenNewCustomerRegister() {
        return openNewCustomerRegister;
    }

    public void setOpenNewCustomerRegister(Byte openNewCustomerRegister) {
        this.openNewCustomerRegister = openNewCustomerRegister;
    }

    public BigDecimal getNewCustomerValue() {
        return newCustomerValue;
    }

    public void setNewCustomerValue(BigDecimal newCustomerValue) {
        this.newCustomerValue = newCustomerValue;
    }

    public Byte getOpenSendSms() {
        return openSendSms;
    }

    public void setOpenSendSms(Byte openSendSms) {
        this.openSendSms = openSendSms;
    }

    public BigDecimal getSendSmsValue() {
        return sendSmsValue;
    }

    public void setSendSmsValue(BigDecimal sendSmsValue) {
        this.sendSmsValue = sendSmsValue;
    }

    public Byte getOpenAllOrder() {
        return openAllOrder;
    }

    public void setOpenAllOrder(Byte openAllOrder) {
        this.openAllOrder = openAllOrder;
    }

    public Double getAllOrderValue() {
        return allOrderValue;
    }

    public void setAllOrderValue(Double allOrderValue) {
        this.allOrderValue = allOrderValue;
    }

    public Byte getOpenBackCustomerOrder() {
        return openBackCustomerOrder;
    }

    public void setOpenBackCustomerOrder(Byte openBackCustomerOrder) {
        this.openBackCustomerOrder = openBackCustomerOrder;
    }

    public Double getBackCustomerOrderValue() {
        return backCustomerOrderValue;
    }

    public void setBackCustomerOrderValue(Double backCustomerOrderValue) {
        this.backCustomerOrderValue = backCustomerOrderValue;
    }

    public Byte getOpenOutFoodOrder() {
        return openOutFoodOrder;
    }

    public void setOpenOutFoodOrder(Byte openOutFoodOrder) {
        this.openOutFoodOrder = openOutFoodOrder;
    }

    public Double getOutFoodOrderValue() {
        return outFoodOrderValue;
    }

    public void setOutFoodOrderValue(Double outFoodOrderValue) {
        this.outFoodOrderValue = outFoodOrderValue;
    }

    public Byte getOpenThirdFoodOrder() {
        return openThirdFoodOrder;
    }

    public void setOpenThirdFoodOrder(Byte openThirdFoodOrder) {
        this.openThirdFoodOrder = openThirdFoodOrder;
    }

    public Double getThirdFoodOrderValue() {
        return thirdFoodOrderValue;
    }

    public void setThirdFoodOrderValue(Double thirdFoodOrderValue) {
        this.thirdFoodOrderValue = thirdFoodOrderValue;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getRemainAccount() {
        return remainAccount;
    }

    public void setRemainAccount(String remainAccount) {
        this.remainAccount = remainAccount == null ? null : remainAccount.trim();
    }
}