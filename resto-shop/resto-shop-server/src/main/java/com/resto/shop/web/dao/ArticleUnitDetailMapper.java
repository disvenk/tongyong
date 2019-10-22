package com.resto.shop.web.dao;

import com.resto.shop.web.model.ArticleUnitDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleUnitDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(ArticleUnitDetail record);

    int insertSelective(ArticleUnitDetail record);

    ArticleUnitDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ArticleUnitDetail record);

    int updateByPrimaryKey(ArticleUnitDetail record);

    List<ArticleUnitDetail> selectArticleUnitDetailByunitId(@Param("unitId") String unitId);

    int deleteUnitByUnitId(@Param("unitId") String unitId);
}