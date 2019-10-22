package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KONATA on 2016/9/8.
 */
public class ArticleRecommend implements Serializable {

    private String id;

    private String name;

    private int count;

    private int isUsed;

    private int sort;

    private String shopId;

    private Integer printType;

    private String kitchenId;

    public Boolean getOpenArticleLibrary() {
        return openArticleLibrary;
    }

    public void setOpenArticleLibrary(Boolean openArticleLibrary) {
        this.openArticleLibrary = openArticleLibrary;
    }

    private String imageUrl;

    private Integer choiceType;

    private Boolean openArticleLibrary;

    public Integer getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(Integer choiceType) {
        this.choiceType = choiceType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private List<ArticleRecommendPrice> articles;

    public String getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(String kitchenId) {
        this.kitchenId = kitchenId;
    }

    public Integer getPrintType() {
        return printType;
    }

    public void setPrintType(Integer printType) {
        this.printType = printType;
    }

    final public String getShopId() {
        return shopId;
    }

    final public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    final public List<ArticleRecommendPrice> getArticles() {
        return articles;
    }

    final public void setArticles(List<ArticleRecommendPrice> articles) {
        this.articles = articles;
    }

    final public String getId() {
        return id;
    }

    final public void setId(String id) {
        this.id = id;
    }

    final public String getName() {
        return name;
    }

    final public void setName(String name) {
        this.name = name;
    }

    final public int getCount() {
        return count;
    }

    final public void setCount(int count) {
        this.count = count;
    }

    final public int getIsUsed() {
        return isUsed;
    }

    final public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    final public int getSort() {
        return sort;
    }

    final public void setSort(int sort) {
        this.sort = sort;
    }
}
