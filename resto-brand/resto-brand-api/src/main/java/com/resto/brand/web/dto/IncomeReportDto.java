package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收入统计  （用于报表）
 * @author lmx
 *
 */
public class IncomeReportDto implements Serializable {
	private BigDecimal payValue;
	private Integer payMentModeId;
	private String shopDetailId;
	private String brandId;
	
	public Integer getPayMentModeId() {
		return payMentModeId;
	}
	public void setPayMentModeId(Integer payMentModeId) {
		this.payMentModeId = payMentModeId;
	}
	public String getShopDetailId() {
		return shopDetailId;
	}
	public void setShopDetailId(String shopDetailId) {
		this.shopDetailId = shopDetailId;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public Integer getPaymentModeId() {
		return payMentModeId;
	}
	public void setPaymentModeId(Integer payMentModeId) {
		this.payMentModeId = payMentModeId;
	}
	
	public BigDecimal getPayValue() {
		return payValue;
	}
	public void setPayValue(BigDecimal payValue) {
		this.payValue = payValue;
	}
}
