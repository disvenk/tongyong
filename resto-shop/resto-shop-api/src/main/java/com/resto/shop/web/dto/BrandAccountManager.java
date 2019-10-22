package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BrandAccountManager implements Serializable {

	private Integer brandAccountId; //账户id

	private String brandId; //品牌id

	private String shopId;//店铺id

	private BigDecimal accountBalance;//账户余额

	private Integer registerCustoemrNum;//注册用户

	private Integer smsNum;//发送短信

	private Integer orderNum;//订单数

	private BigDecimal orderMoney;//订单额

	private BigDecimal registerCustomerMoney;

	private BigDecimal smsMoney;

	private BigDecimal orderOutMoney;

	private BigDecimal brandAccountCharge;

	public Integer getBrandAccountId() {
		return brandAccountId;
	}

	public void setBrandAccountId(Integer brandAccountId) {
		this.brandAccountId = brandAccountId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Integer getRegisterCustoemrNum() {
		return registerCustoemrNum;
	}

	public void setRegisterCustoemrNum(Integer registerCustoemrNum) {
		this.registerCustoemrNum = registerCustoemrNum;
	}

	public Integer getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(Integer smsNum) {
		this.smsNum = smsNum;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getRegisterCustomerMoney() {
		return registerCustomerMoney;
	}

	public void setRegisterCustomerMoney(BigDecimal registerCustomerMoney) {
		this.registerCustomerMoney = registerCustomerMoney;
	}

	public BigDecimal getSmsMoney() {
		return smsMoney;
	}

	public void setSmsMoney(BigDecimal smsMoney) {
		this.smsMoney = smsMoney;
	}

	public BigDecimal getOrderOutMoney() {
		return orderOutMoney;
	}

	public void setOrderOutMoney(BigDecimal orderOutMoney) {
		this.orderOutMoney = orderOutMoney;
	}

	public BigDecimal getBrandAccountCharge() {
		return brandAccountCharge;
	}

	public void setBrandAccountCharge(BigDecimal brandAccountCharge) {
		this.brandAccountCharge = brandAccountCharge;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}



}
