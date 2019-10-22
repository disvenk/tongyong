package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ArticleRecommend;
import org.apache.ibatis.annotations.Param;

public interface ArticleRecommendMapper extends BaseDao<ArticleRecommend, String> {

    ArticleRecommend getRecommendByArticleId(@Param("articleId") String articleId,
                                             @Param("shopId") String shopIds);
}
