package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 店铺菜品销售  （用于报表）
 * @author lmx
 *
 */
public class ShopArticleReportDto implements Serializable {
	
	private String shopId;//
	
	private String shopName;
	
	private int totalNum;//菜品总销量
	
	private BigDecimal sellIncome;//菜品销售额
	
	private String occupy;//销售占比

    private  Integer   refundCount;        //退菜总数

    private  BigDecimal refundTotal;//退菜总数
    
    private BigDecimal discountTotal;//折扣总额

	private  Integer grantCount;//退菜总数

	private  BigDecimal grantTotal;//退菜金额

    private List<Map<String, Object>> shopArticleReportDtos;

    public BigDecimal getDiscountTotal() {
		return discountTotal;
	}

	public void setDiscountTotal(BigDecimal discountTotal) {
		this.discountTotal = discountTotal;
	}

	public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    public BigDecimal getRefundTotal() {
        return refundTotal;
    }

    public void setRefundTotal(BigDecimal refundTotal) {
        this.refundTotal = refundTotal;
    }

    public String getOccupy() {
		return occupy;
	}

	public void setOccupy(String occupy) {
		this.occupy = occupy;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public BigDecimal getSellIncome() {
		return sellIncome;
	}

	public void setSellIncome(BigDecimal sellIncome) {
		this.sellIncome = sellIncome;
	}

    public List<Map<String, Object>> getShopArticleReportDtos() {
        return shopArticleReportDtos;
    }

    public void setShopArticleReportDtos(List<Map<String, Object>> shopArticleReportDtos) {
        this.shopArticleReportDtos = shopArticleReportDtos;
    }

	public Integer getGrantCount() {
		return grantCount;
	}

	public void setGrantCount(Integer grantCount) {
		this.grantCount = grantCount;
	}

	public BigDecimal getGrantTotal() {
		return grantTotal;
	}

	public void setGrantTotal(BigDecimal grantTotal) {
		this.grantTotal = grantTotal;
	}

	public ShopArticleReportDto(){}

	public ShopArticleReportDto(String shopId, String shopName, int totalNum, BigDecimal sellIncome, String occupy,
			Integer refundCount, BigDecimal refundTotal, BigDecimal discountTotal, Integer grantCount, BigDecimal grantTotal) {
		super();
		this.shopId = shopId;
		this.shopName = shopName;
		this.totalNum = totalNum;
		this.sellIncome = sellIncome;
		this.occupy = occupy;
		this.refundCount = refundCount;
		this.refundTotal = refundTotal;
		this.discountTotal = discountTotal;
		this.grantCount = grantCount;
		this.grantTotal = grantTotal;
	}

}
