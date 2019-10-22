package com.resto.shop.web.dto;

import java.io.Serializable;

/**
 * Created by carl on 2017/7/29.
 */
public class ArticleSellCountDto implements Serializable {
    private String articleId;
    private String articleName;

    private int totalCount;
    private int mealFeeNumber;

    private String createTime;


    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }


    public int getMealFeeNumber() {
        return mealFeeNumber;
    }

    public void setMealFeeNumber(int mealFeeNumber) {
        this.mealFeeNumber = mealFeeNumber;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ArticleSellCountDto{" +
                "articleId='" + articleId + '\'' +
                ", articleName='" + articleName + '\'' +
                ", totalCount=" + totalCount +
                ", mealFeeNumber=" + mealFeeNumber +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
