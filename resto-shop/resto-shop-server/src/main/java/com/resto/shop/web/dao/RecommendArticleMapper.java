package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.RecommendArticle;

public interface RecommendArticleMapper extends GenericDao<RecommendArticle,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(RecommendArticle record);

    int insertSelective(RecommendArticle record);

    RecommendArticle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RecommendArticle record);

    int updateByPrimaryKey(RecommendArticle record);
}