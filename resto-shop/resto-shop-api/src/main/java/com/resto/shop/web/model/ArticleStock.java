package com.resto.shop.web.model;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/8/1.
 */
public class ArticleStock implements Serializable {

    private String name;

    private String familyName;

    private Integer currentStock;

    private String id;

    private Integer defaultStock;

    private Integer stockWeekend;

    private Integer stockWorkingDay;
    
    private Integer articleType;
    
    private String emptyRemark;
    
    private Integer activated;

    private Integer inventoryWarning;

    private String articleFamilyId;

    private Integer redisCount;


    public Integer getRedisCount() {
        return redisCount;
    }

    public void setRedisCount(Integer redisCount) {
        this.redisCount = redisCount;
    }

    final public Integer getStockWorkingDay() {
        return stockWorkingDay;
    }

    final public void setStockWorkingDay(Integer stockWorkingDay) {
        this.stockWorkingDay = stockWorkingDay;
    }

    final public Integer getStockWeekend() {
        return stockWeekend;
    }

    final public void setStockWeekend(Integer stockWeekend) {
        this.stockWeekend = stockWeekend;
    }

    final public Integer getDefaultStock() {
        return defaultStock;
    }

    final public void setDefaultStock(Integer defaultStock) {
        this.defaultStock = defaultStock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

	public Integer getArticleType() {
		return articleType;
	}

	public void setArticleType(Integer articleType) {
		this.articleType = articleType;
	}

	public String getEmptyRemark() {
		return emptyRemark;
	}

	public void setEmptyRemark(String emptyRemark) {
		this.emptyRemark = emptyRemark;
	}

	public Integer getActivated() {
		return activated;
	}

	public void setActivated(Integer activated) {
		this.activated = activated;
	}

    public Integer getInventoryWarning() {
        return inventoryWarning;
    }

    public void setInventoryWarning(Integer inventoryWarning) {
        this.inventoryWarning = inventoryWarning;
    }

    public String getArticleFamilyId() {
        return articleFamilyId;
    }

    public void setArticleFamilyId(String articleFamilyId) {
        this.articleFamilyId = articleFamilyId;
    }
}
