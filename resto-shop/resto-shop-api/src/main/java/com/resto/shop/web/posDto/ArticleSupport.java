package com.resto.shop.web.posDto;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/9/6.
 */
public class ArticleSupport implements Serializable {
    private static final long serialVersionUID = 2246580438652374612L;
    private String articleId;

    private String supportTimeId;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getSupportTimeId() {
        return supportTimeId;
    }

    public void setSupportTimeId(String supportTimeId) {
        this.supportTimeId = supportTimeId;
    }
}
