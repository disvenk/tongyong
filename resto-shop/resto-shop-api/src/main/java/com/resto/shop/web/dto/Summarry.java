package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺 获取汇总数据
 * 本次给 鲁肉范 --- 田林路店用
 */
public class Summarry implements Serializable {

	private BigDecimal lineOffOrderMoney;//线下输入出订单

	private Integer customerOrderNum;//用户消费笔数
	private BigDecimal CustomerOrderMoney;//用户消费金额


	private Integer newCustomerOrderNum;//新用户消费笔数
	private BigDecimal newCustomerOrderMoney;//新用户消费金额

	private BigDecimal RestoTotalMoney;

	public BigDecimal getRestoTotalMoney() {
		return RestoTotalMoney;
	}

	public void setRestoTotalMoney(BigDecimal restoTotalMoney) {
		RestoTotalMoney = restoTotalMoney;
	}

	private Integer newShareCustomerOrderNum;//分享用户消费笔数

	private BigDecimal newShareCustomerMoney;//分享新用消费金额

	private Integer backCustomerOrder;//回头用户消费笔数

	private BigDecimal backCustomerMoney;//回头用户消费金额

	private String statisfaction;//折扣比率

	private Integer fiveStar ;//五星评论数

	private Integer fourStar ;//改进评论数

	private Integer oneToThree;//差评数

	private String shopName;//店铺名字

	public BigDecimal getLineOffOrderMoney() {
		return lineOffOrderMoney;
	}

	public void setLineOffOrderMoney(BigDecimal lineOffOrderMoney) {
		this.lineOffOrderMoney = lineOffOrderMoney;
	}

	public Integer getCustomerOrderNum() {
		return customerOrderNum;
	}

	public void setCustomerOrderNum(Integer customerOrderNum) {
		this.customerOrderNum = customerOrderNum;
	}

	public BigDecimal getCustomerOrderMoney() {
		return CustomerOrderMoney;
	}

	public void setCustomerOrderMoney(BigDecimal customerOrderMoney) {
		CustomerOrderMoney = customerOrderMoney;
	}

	public Integer getNewCustomerOrderNum() {
		return newCustomerOrderNum;
	}

	public void setNewCustomerOrderNum(Integer newCustomerOrderNum) {
		this.newCustomerOrderNum = newCustomerOrderNum;
	}

	public BigDecimal getNewCustomerOrderMoney() {
		return newCustomerOrderMoney;
	}

	public void setNewCustomerOrderMoney(BigDecimal newCustomerOrderMoney) {
		this.newCustomerOrderMoney = newCustomerOrderMoney;
	}

	public Integer getNewShareCustomerOrderNum() {
		return newShareCustomerOrderNum;
	}

	public void setNewShareCustomerOrderNum(Integer newShareCustomerOrderNum) {
		this.newShareCustomerOrderNum = newShareCustomerOrderNum;
	}

	public BigDecimal getNewShareCustomerMoney() {
		return newShareCustomerMoney;
	}

	public void setNewShareCustomerMoney(BigDecimal newShareCustomerMoney) {
		this.newShareCustomerMoney = newShareCustomerMoney;
	}

	public BigDecimal getBackCustomerMoney() {
		return backCustomerMoney;
	}

	public void setBackCustomerMoney(BigDecimal backCustomerMoney) {
		this.backCustomerMoney = backCustomerMoney;
	}

	public String getStatisfaction() {
		return statisfaction;
	}

	public void setStatisfaction(String statisfaction) {
		this.statisfaction = statisfaction;
	}

	public Integer getFiveStar() {
		return fiveStar;
	}

	public void setFiveStar(Integer fiveStar) {
		this.fiveStar = fiveStar;
	}

	public Integer getFourStar() {
		return fourStar;
	}

	public void setFourStar(Integer fourStar) {
		this.fourStar = fourStar;
	}

	public Integer getOneToThree() {
		return oneToThree;
	}

	public void setOneToThree(Integer oneToThree) {
		this.oneToThree = oneToThree;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getBackCustomerOrder() {
		return backCustomerOrder;
	}

	public void setBackCustomerOrder(Integer backCustomerOrder) {
		this.backCustomerOrder = backCustomerOrder;
	}
}
