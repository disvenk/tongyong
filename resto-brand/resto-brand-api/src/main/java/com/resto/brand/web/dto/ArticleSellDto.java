package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 菜品销售报表
 * @author lmx
 *
 */
public class ArticleSellDto implements Serializable {
	private String articleId;//菜品ID
	private String articleName;//菜品名称
	private String articleFamilyId;//菜品分类ID
	private String articleFamilyName;//菜品分类名称
	private Integer shopSellNum;//店铺销售数量
	private Integer brandSellNum;//品牌销售数量
	private String salesRatio;//销售占比
	private String numRatio;//销量占比
	private BigDecimal salles;//销售额
	private Integer likes;//点赞数
	private Integer  type;//餐品的类型  是为了计算套餐  3  是套餐
	private String shopId;//店铺ID
	private BigDecimal discountMoney;//折扣金额
    //新增菜品编号
    private  String numberCode;

    private  String typeName; //菜品类型名称 type=3是套餐 其他事单品  主要是下载报表数据好用
//
//    t.refund_count,
//    t.unit_price*t.refund_count as refundTotal,
    private  Integer refundCount;//退菜数量
    private  BigDecimal refundTotal;//退菜金额  unit_price * refund_count

	private  Integer grantCount;//退菜数量
	private  BigDecimal grantTotal;//退菜金额

    private Map<String, Object> brandReport;

    private List<Map<String, Object>> brandArticleUnit;

    private List<Map<String, Object>> brandArticleFamily;

    private List<Map<String, Object>> shopArticleUnit;

    private List<Map<String, Object>> shopArticleFamily;

    private List<Map<String, Object>> brandArticleType;

    private List<Map<String, Object>> shopArticleType;

	private List<Map<String, Object>> shopMealSeals;

	private List<Map<String, Object>> brandMealSeals;

	private BigDecimal weight;

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public List<Map<String, Object>> getBrandMealSeals() {
		return brandMealSeals;
	}

	public void setBrandMealSeals(List<Map<String, Object>> brandMealSeals) {
		this.brandMealSeals = brandMealSeals;
	}

	public List<Map<String, Object>> getShopMealSeals() {
		return shopMealSeals;
	}

	public void setShopMealSeals(List<Map<String, Object>> shopMealSeals) {
		this.shopMealSeals = shopMealSeals;
	}

	public List<Map<String, Object>> getBrandArticleType() {
        return brandArticleType;
    }

    public void setBrandArticleType(List<Map<String, Object>> brandArticleType) {
        this.brandArticleType = brandArticleType;
    }

    public List<Map<String, Object>> getShopArticleType() {
        return shopArticleType;
    }

    public void setShopArticleType(List<Map<String, Object>> shopArticleType) {
        this.shopArticleType = shopArticleType;
    }

    public BigDecimal getDiscountMoney() {
		return discountMoney;
	}

	public void setDiscountMoney(BigDecimal discountMoney) {
		this.discountMoney = discountMoney;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
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

	public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getNumRatio() {
		return numRatio;
	}
	public void setNumRatio(String numRatio) {
		this.numRatio = numRatio;
	}
	private Integer peference;
	
	private Integer sort;
	
	public Integer getPeference() {
		return peference;
	}
	public void setPeference(Integer peference) {
		this.peference = peference;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public BigDecimal getSalles() {
		return salles;
	}
	public void setSalles(BigDecimal salles) {
		this.salles = salles;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public String getArticleFamilyId() {
		return articleFamilyId;
	}
	public void setArticleFamilyId(String articleFamilyId) {
		this.articleFamilyId = articleFamilyId;
	}
	public String getArticleFamilyName() {
		return articleFamilyName;
	}
	public void setArticleFamilyName(String articleFamilyName) {
		this.articleFamilyName = articleFamilyName;
	}
	public Integer getShopSellNum() {
		return shopSellNum;
	}
	public void setShopSellNum(Integer shopSellNum) {
		this.shopSellNum = shopSellNum;
	}
	public Integer getBrandSellNum() {
		return brandSellNum;
	}
	public void setBrandSellNum(Integer brandSellNum) {
		this.brandSellNum = brandSellNum;
	}
	public String getSalesRatio() {
		return salesRatio;
	}
	public void setSalesRatio(String salesRatio) {
		this.salesRatio = salesRatio;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

    public Map<String, Object> getBrandReport() {
        return brandReport;
    }

    public void setBrandReport(Map<String, Object> brandReport) {
        this.brandReport = brandReport;
    }

    public List<Map<String, Object>> getBrandArticleUnit() {
        return brandArticleUnit;
    }

    public void setBrandArticleUnit(List<Map<String, Object>> brandArticleUnit) {
        this.brandArticleUnit = brandArticleUnit;
    }

    public List<Map<String, Object>> getBrandArticleFamily() {
        return brandArticleFamily;
    }

    public void setBrandArticleFamily(List<Map<String, Object>> brandArticleFamily) {
        this.brandArticleFamily = brandArticleFamily;
    }

    public List<Map<String, Object>> getShopArticleUnit() {
        return shopArticleUnit;
    }

    public void setShopArticleUnit(List<Map<String, Object>> shopArticleUnit) {
        this.shopArticleUnit = shopArticleUnit;
    }

    public List<Map<String, Object>> getShopArticleFamily() {
        return shopArticleFamily;
    }

    public void setShopArticleFamily(List<Map<String, Object>> shopArticleFamily) {
        this.shopArticleFamily = shopArticleFamily;
    }
}
