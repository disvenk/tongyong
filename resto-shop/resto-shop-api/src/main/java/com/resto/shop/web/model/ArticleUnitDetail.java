package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Lmx on 2017/6/15.
 */
public class ArticleUnitDetail implements Serializable {
    private String id;
    private String unitDetailId;
    private String articleUnitId;
    private BigDecimal price;
    private Integer isUsed;
    private Integer sort;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitDetailId() {
        return unitDetailId;
    }

    public void setUnitDetailId(String unitDetailId) {
        this.unitDetailId = unitDetailId;
    }

    public String getArticleUnitId() {
        return articleUnitId;
    }

    public void setArticleUnitId(String articleUnitId) {
        this.articleUnitId = articleUnitId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
