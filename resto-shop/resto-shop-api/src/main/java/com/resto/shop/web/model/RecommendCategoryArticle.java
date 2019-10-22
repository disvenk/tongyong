package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class RecommendCategoryArticle implements Serializable {
    private String id;

    private String articleName;

    private Integer recommendSort;

    private String recommendCategoryId;

    private String articleId;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName == null ? null : articleName.trim();
    }

    public Integer getRecommendSort() {
        return recommendSort;
    }

    public void setRecommendSort(Integer recommendSort) {
        this.recommendSort = recommendSort;
    }

    public String getRecommendCategoryId() {
        return recommendCategoryId;
    }

    public void setRecommendCategoryId(String recommendCategoryId) {
        this.recommendCategoryId = recommendCategoryId == null ? null : recommendCategoryId.trim();
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}