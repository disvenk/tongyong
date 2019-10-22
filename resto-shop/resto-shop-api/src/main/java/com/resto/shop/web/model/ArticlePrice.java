package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ArticlePrice implements Serializable {
    private String id;

    private String unitIds;
    
    private BigDecimal price;

    private BigDecimal fansPrice;

    private String name;

    private String peference;

    private Integer sort;

    private String articleId;

    private Integer stockWorkingDay;

    private Integer stockWeekend;

    private Integer currentWorkingStock;

    final public Integer getCurrentWorkingStock() {
        return currentWorkingStock;
    }

    final public void setCurrentWorkingStock(Integer currentWorkingStock) {
        this.currentWorkingStock = currentWorkingStock;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFansPrice() {
        return fansPrice;
    }

    public void setFansPrice(BigDecimal fansPrice) {
        this.fansPrice = fansPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPeference() {
        return peference;
    }

    public void setPeference(String peference) {
        this.peference = peference == null ? null : peference.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

	public String getUnitIds() {
		return unitIds;
	}

	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds==null?null : unitIds.trim();
	}
}