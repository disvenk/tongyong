package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SmsChargeOrder implements Serializable {
	
	private String id;
	private String brandId;
	private Date createTime;
	private Date pushOrderTime;//确认时间
	private BigDecimal chargeMoney;//充值金额
	private BigDecimal smsUnitPrice;//短信单价
	private Integer number;//购买条数
	private String ticketId;//发票id
	private Integer payType;//支付方式
	private String tradeNo;//第三方交易号
	private Integer orderStatus;//订单状态，0未支付/1已支付
	private Integer status;//状态，判断是否被删除，0删除，1存在
	private String remark;//订单备注
	
	private String brandName;//品牌名称
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPushOrderTime() {
		return pushOrderTime;
	}
	public void setPushOrderTime(Date pushOrderTime) {
		this.pushOrderTime = pushOrderTime;
	}
	public BigDecimal getChargeMoney() {
		return chargeMoney;
	}
	public void setChargeMoney(BigDecimal chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getSmsUnitPrice() {
		return smsUnitPrice;
	}
	public void setSmsUnitPrice(BigDecimal smsUnitPrice) {
		this.smsUnitPrice = smsUnitPrice;
	}

	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
