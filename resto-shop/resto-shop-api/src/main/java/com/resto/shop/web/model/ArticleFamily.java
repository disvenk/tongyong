package com.resto.shop.web.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public class ArticleFamily implements Serializable{
    private String id;

    @NotBlank(message="{类型名称   不能为空}")
    private String name;

    @NotNull(message="{序号   不能为空}")
    private Integer peference;

    private String parentId;

    private Integer level;

    private String shopDetailId;
    
    @NotNull(message="{就餐模式   不能为空}")
    private Integer distributionModeId;

    private Integer inventoryWarningNum;

    private List<Article> articleList;

    private Boolean openArticleLibrary;

    private String pictureUrl;

    private Integer openPictureSwitch;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getOpenPictureSwitch() {
        return openPictureSwitch;
    }

    public void setOpenPictureSwitch(Integer openPictureSwitch) {
        this.openPictureSwitch = openPictureSwitch;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPeference() {
        return peference;
    }

    public void setPeference(Integer peference) {
        this.peference = peference;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public Integer getDistributionModeId() {
        return distributionModeId;
    }

    public void setDistributionModeId(Integer distributionModeId) {
        this.distributionModeId = distributionModeId;
    }

    public Integer getInventoryWarningNum() {
        return inventoryWarningNum;
    }

    public void setInventoryWarningNum(Integer inventoryWarningNum) {
        this.inventoryWarningNum = inventoryWarningNum;
    }

    public Boolean getOpenArticleLibrary() {
        return openArticleLibrary;
    }

    public void setOpenArticleLibrary(Boolean openArticleLibrary) {
        this.openArticleLibrary = openArticleLibrary;
    }
}