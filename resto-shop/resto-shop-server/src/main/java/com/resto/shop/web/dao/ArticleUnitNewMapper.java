package com.resto.shop.web.dao;

import com.resto.shop.web.model.ArticleUnitNew;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleUnitNewMapper {
    int deleteByPrimaryKey(String id);

    int insert(ArticleUnitNew record);

    int insertSelective(ArticleUnitNew record);

    ArticleUnitNew selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ArticleUnitNew record);

    int updateByPrimaryKey(ArticleUnitNew record);

    List<ArticleUnitNew> selectArticleUnitNewByArticleId(@Param("articleId") String articleId);

    int deleteArticleUnitByArticleId(@Param("articleId") String articleId);
}