package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderPayDto implements Serializable {
	
	private String shopDetailId;//店铺id
	
	private String name;//品牌 或 店铺 名字
	
	private BigDecimal orderMoney;//订单总额
	
	private Integer number;//订单数
	
	private BigDecimal average;//平均金额
	
	private String marketPrize;//营销撬动率

    private Map<String, Object> brandOrderDto;

    private List<Map<String, Object>> shopOrderDtos;

    public Map<String, Object> getBrandOrderDto() {
        return brandOrderDto;
    }

    public void setBrandOrderDto(Map<String, Object> brandOrderDto) {
        this.brandOrderDto = brandOrderDto;
    }

    public List<Map<String, Object>> getShopOrderDtos() {
        return shopOrderDtos;
    }

    public void setShopOrderDtos(List<Map<String, Object>> shopOrderDtos) {
        this.shopOrderDtos = shopOrderDtos;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMarketPrize() {
		return marketPrize;
	}

	public void setMarketPrize(String marketPrize) {
		this.marketPrize = marketPrize;
	}

	public BigDecimal getAverage() {
		return average;
	}

	public void setAverage(BigDecimal average) {
		this.average = average;
	}

	public String getShopDetailId() {
		return shopDetailId;
	}

	public void setShopDetailId(String shopDetailId) {
		this.shopDetailId = shopDetailId;
	}


	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public OrderPayDto(String shopDetailId, String shopName, BigDecimal orderMoney, Integer number,
			BigDecimal average,String marketPrize) {
		super();
		this.shopDetailId = shopDetailId;
		this.name = shopName;
		this.orderMoney = orderMoney;
		this.number = number;
		this.average = average;
		this.marketPrize = marketPrize;
	}

	public OrderPayDto() {
		super();
	}
	
	
	public OrderPayDto(String brandName,BigDecimal orderMoney,Integer number,BigDecimal average,String marketPrize){
		super();
		this.name = brandName;
		this.orderMoney = orderMoney;
		this.number = number;
		this.average=average;
		this.marketPrize = marketPrize;
	}
	
	
	
}




