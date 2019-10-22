package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class OrderPayDto implements Serializable {

	private static final long serialVersionUID = -856689537282739802L;

	private String shopDetailId;//店铺id
	
	private String name;//品牌 或 店铺 名字
	
	private BigDecimal orderMoney;//订单总额
	
	private Integer number;//订单数
	
	private BigDecimal average;//平均金额
	
	private String marketPrize;//营销撬动率

    private Map<String, Object> brandOrderDto;

    private List<Map<String, Object>> shopOrderDtos;

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




