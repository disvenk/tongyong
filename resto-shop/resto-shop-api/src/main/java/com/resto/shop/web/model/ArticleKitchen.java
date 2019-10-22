package com.resto.shop.web.model;

import java.io.Serializable;

/**
 * Created by Lmx on 2017/6/15.
 */
public class ArticleKitchen implements Serializable {
    private String articleId;
    private String kitchenId;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(String kitchenId) {
        this.kitchenId = kitchenId;
    }
}
