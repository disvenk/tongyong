package com.resto.shop.web.dao;


import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.RecommendCategoryArticle;

public interface RecommendCategoryArticleMapper extends GenericDao<RecommendCategoryArticle,String> {
    int deleteByPrimaryKey(String id);

    int insert(RecommendCategoryArticle record);

    int insertSelective(RecommendCategoryArticle record);

    RecommendCategoryArticle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RecommendCategoryArticle record);

    int updateByPrimaryKey(RecommendCategoryArticle record);

    int deleteRecommendCategoryId(String id);

    RecommendCategoryArticle selectByArticleId(String articleId);

    void deleteAll();
}