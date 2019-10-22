package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RecommendCategory implements Serializable {
    private String id;

    private Integer type;

    private String name;

    private Integer sort;

    private Integer state;

    private String shopDetailId;

    private Date createTime;

    private Date updateTime;

    private List<RecommendCategoryArticle> articles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
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

    public List<RecommendCategoryArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<RecommendCategoryArticle> articles) {
        this.articles = articles;
    }
}