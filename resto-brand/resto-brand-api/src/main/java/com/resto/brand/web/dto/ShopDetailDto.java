package com.resto.brand.web.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ShopDetailDto implements Serializable {

	private String shopDetailId;
	private String shopname;
	private BigDecimal chargeMoney;

	private BigDecimal rewardMoney;

	private Byte orderState;

	private Date finishTime;

	private Integer type;

	private  String typeString;//充值类型字符串显示

   //充值详细

	private String operationPhone;

	private String customerPhone;

	public ShopDetailDto() {

	}

	public String getShopDetailId() {
		return shopDetailId;
	}

	public void setShopDetailId(String shopDetailId) {
		this.shopDetailId = shopDetailId;
	}

	public ShopDetailDto(String shopDetailId, String shopname, BigDecimal chargeMoney, BigDecimal rewardMoney, Byte orderState, Date finishTime, Integer type, String operationPhone, String customerPhone) {
		this.shopDetailId = shopDetailId;
		this.shopname = shopname;
		this.chargeMoney = chargeMoney;
		this.rewardMoney = rewardMoney;
		this.orderState = orderState;
		this.finishTime = finishTime;
		this.type = type;
		this.operationPhone = operationPhone;
		this.customerPhone = customerPhone;
	}

	public void setType(Integer type) {
		if(type==0){
			this.typeString="pos端充值";
			this.type = type;
		}else{
			this.typeString="微信充值";
			this.type = type;
		}

	}

	public BigDecimal getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(BigDecimal chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public BigDecimal getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(BigDecimal rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	public Byte getOrderState() {
		return orderState;
	}

	public void setOrderState(Byte orderState) {
		this.orderState = orderState;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public Integer getType() {
		return type;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String getOperationPhone() {
		return operationPhone;
	}

	public void setOperationPhone(String operationPhone) {
		this.operationPhone = operationPhone;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public ShopDetailDto(BigDecimal chargeMoney, BigDecimal rewardMoney, Byte orderState, Date finishTime, String shopname, Integer type, String typeString, String operationPhone, String customerPhone) {
		this.chargeMoney = chargeMoney;
		this.rewardMoney = rewardMoney;
		this.orderState = orderState;
		this.finishTime = finishTime;
		this.shopname = shopname;
		this.type = type;
		this.typeString = typeString;
		this.operationPhone = operationPhone;
		this.customerPhone = customerPhone;
	}

	public ShopDetailDto(String shopname, BigDecimal chargeMoney, BigDecimal rewardMoney, Date finishTime, Integer type, String operationPhone, String customerPhone) {
		this.shopname = shopname;
		this.chargeMoney = chargeMoney;
		this.rewardMoney = rewardMoney;
		this.finishTime = finishTime;
		this.type = type;
		this.operationPhone = operationPhone;
		this.customerPhone = customerPhone;
	}
}




