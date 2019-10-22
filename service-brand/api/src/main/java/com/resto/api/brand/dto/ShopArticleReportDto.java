package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 店铺菜品销售  （用于报表）
 * @author lmx
 *
 */
@Data
public class ShopArticleReportDto implements Serializable {

	private static final long serialVersionUID = -7638728612543041757L;

	private String shopId;
	
	private String shopName;
	
	private int totalNum;//菜品总销量
	
	private BigDecimal sellIncome;//菜品销售额
	
	private String occupy;//销售占比

    private  Integer   refundCount;        //退菜总数

    private  BigDecimal refundTotal;//退菜总数
    
    private BigDecimal discountTotal;//折扣总额

    private List<Map<String, Object>> shopArticleReportDtos;

	public ShopArticleReportDto(String shopId, String shopName, int totalNum, BigDecimal sellIncome, String occupy,
			Integer refundCount, BigDecimal refundTotal, BigDecimal discountTotal) {
		super();
		this.shopId = shopId;
		this.shopName = shopName;
		this.totalNum = totalNum;
		this.sellIncome = sellIncome;
		this.occupy = occupy;
		this.refundCount = refundCount;
		this.refundTotal = refundTotal;
		this.discountTotal = discountTotal;
	}

}
