package com.resto.shop.web.dao;

import com.resto.shop.web.model.ArticleLibrary;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleLibraryMapper  extends GenericDao<ArticleLibrary,String> {
    int deleteByPrimaryKey(String id);

    int insert(ArticleLibrary record);

    int insertSelective(ArticleLibrary record);

    ArticleLibrary selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ArticleLibrary record);

    int updateByPrimaryKey(ArticleLibrary record);

    List<ArticleLibrary> selectAll(@Param("brandId") String brandId);

    List<ArticleLibrary> delCheckArticle(@Param("id")String id);

    List<ArticleLibrary> getProductsItem();

    void addArticleLikes(String id);
}
