package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by carl on 2016/12/7.
 */
public class UnitArticle implements Serializable {

    private String id;

    private String ArticleId;

    private Integer choiceType;

    private Integer isUsed;

    private String name;

    private Integer sort;

    private String detailName;

    private String detailId;

    private Integer detailSort;

    private BigDecimal detailPrice;

    private Integer detailUsed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return ArticleId;
    }

    public void setArticleId(String articleId) {
        ArticleId = articleId;
    }

    public Integer getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(Integer choiceType) {
        this.choiceType = choiceType;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public Integer getDetailSort() {
        return detailSort;
    }

    public void setDetailSort(Integer detailSort) {
        this.detailSort = detailSort;
    }

    public BigDecimal getDetailPrice() {
        return detailPrice;
    }

    public void setDetailPrice(BigDecimal detailPrice) {
        this.detailPrice = detailPrice;
    }

    public Integer getDetailUsed() {
        return detailUsed;
    }

    public void setDetailUsed(Integer detailUsed) {
        this.detailUsed = detailUsed;
    }
}
