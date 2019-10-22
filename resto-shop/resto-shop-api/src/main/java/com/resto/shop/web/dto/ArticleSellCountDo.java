package com.resto.shop.web.dto;

import java.io.Serializable;
import java.util.List;

public class ArticleSellCountDo implements  Serializable  {



    //菜品消耗总量
    private int totalCount;
    //餐盒数量
    private int mealFeeNumber;
    //菜品id
    private String articleId;

    private String createTime;



    private List<ArticleBomDo> articleBomDos;


    public List<ArticleBomDo> getArticleBomDos() {
        return articleBomDos;
    }

    public void setArticleBomDos(List<ArticleBomDo> articleBomDos) {
        this.articleBomDos = articleBomDos;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getMealFeeNumber() {
        return mealFeeNumber;
    }

    public void setMealFeeNumber(int mealFeeNumber) {
        this.mealFeeNumber = mealFeeNumber;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "ArticleSellCountDo{" +
                "totalCount=" + totalCount +
                ", mealFeeNumber=" + mealFeeNumber +
                ", articleId='" + articleId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", articleBomDos=" + articleBomDos +
                '}';
    }
}
