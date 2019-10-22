package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.RecommendCategoryArticle;

/**
 * Created by xielc on 2017/6/29.
 */
public interface RecommendCategoryArticleService extends GenericService<RecommendCategoryArticle, String> {
    /**
     * 根据菜品id查询信息
     * @param articleId
     * @return
     */
    RecommendCategoryArticle selectByArticleId(String articleId);

    void deleteAll();
}
