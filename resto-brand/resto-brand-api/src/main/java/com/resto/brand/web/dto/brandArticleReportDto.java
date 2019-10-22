package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 品牌菜品销售  （用于报表）
 * @author lmx
 *
 */
public class brandArticleReportDto implements Serializable {
	
	private String brandName;//品牌名称
	
	private int totalNum;//菜品总销量

    private BigDecimal sellIncome;//菜品总销售额

    private  Integer refundCount;//退菜总数

    private  BigDecimal refundTotal;//退菜金额
    
    private BigDecimal discountTotal;//折扣总额

    private  Integer grantCount;//退菜总数

    private  BigDecimal grantTotal;//退菜金额

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

    public BigDecimal getSellIncome() {
        return sellIncome;
    }

    public void setSellIncome(BigDecimal sellIncome) {
        this.sellIncome = sellIncome;
    }

    public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
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

    public brandArticleReportDto(String brandName, int totalNum, BigDecimal sellIncome, int refundCount, BigDecimal refundTotal, BigDecimal discountTotal, Integer grantCount, BigDecimal grantTotal) {
		super();
		this.brandName = brandName;
		this.totalNum = totalNum;
        this.sellIncome=sellIncome;
        this.refundCount=refundCount;
        this.refundTotal=refundTotal;
        this.discountTotal=discountTotal;
        this.grantCount=grantCount;
        this.grantTotal=grantTotal;
	}
	
	public brandArticleReportDto(){}
	
}
