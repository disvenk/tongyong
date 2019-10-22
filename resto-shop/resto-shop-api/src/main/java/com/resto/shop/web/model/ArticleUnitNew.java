package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lmx on 2017/6/16.
 */
public class ArticleUnitNew implements Serializable {
    private String id;
    private String articleId;
    private String name;
    private String unitId;
    private Integer choiceType;
    private Integer isUsed;
    private List<ArticleUnitDetail> articleUnitDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
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

    public List<ArticleUnitDetail> getArticleUnitDetails() {
        return articleUnitDetails;
    }

    public void setArticleUnitDetails(List<ArticleUnitDetail> articleUnitDetails) {
        this.articleUnitDetails = articleUnitDetails;
    }
}
