package com.resto.shop.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ArticleAttr;

public interface ArticleAttrMapper extends GenericDao<ArticleAttr, Integer>{
    int deleteByPrimaryKey(Integer id);

    int insert(ArticleAttr record);

    int insertSelective(ArticleAttr record);

    ArticleAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticleAttr record);

    int updateByPrimaryKey(ArticleAttr record);
    
    /**
     * 根据店铺ID查询信息
     * @return
     */
    List<ArticleAttr> selectListByShopId(@Param(value = "shopId") String currentShopId);
    
    /**
     * 添加 信息 ，并设置此数据数据的 ID
     * @param record
     * @return
     */
    void insertInfo(ArticleAttr record);

    List<ArticleAttr> selectListByArticleId(String articleId);

    int insertByAuto(ArticleAttr articleAttr);

    ArticleAttr selectSame(@Param("name") String name,@Param("shopId") String shopId);
    
}