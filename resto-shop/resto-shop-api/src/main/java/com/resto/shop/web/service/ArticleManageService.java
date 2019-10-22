package com.resto.shop.web.service;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.ArticleFamily;
import com.resto.shop.web.model.ArticleLibrary;

import java.util.List;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/11/5/0005 13:52
 * @Description:
 */
public interface ArticleManageService {

    Result createArticle(ArticleLibrary articlelibrary);

    List<ArticleLibrary> selectAll(String brandId);

    Result deleteArticle(String articleId);

    Result modifyArticle(ArticleLibrary articlelibrary);

    int insertSelective(ArticleLibrary record);

    List<ArticleLibrary> getProductsItem();

}
