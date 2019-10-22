package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ArticleRecommend;
import com.resto.shop.web.model.ArticleRecommendPrice;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by KONATA on 2016/9/8.
 */
public interface ArticleRecommendMapper extends GenericDao<ArticleRecommend, String> {

    List<ArticleRecommend> getRecommendList(String shopId);

    int deleteByPrimaryKey(String id);

    ArticleRecommend getRecommendById(String id);

    int insertRecommendArticle(@Param("recommendId") String recommendId, @Param("articleRecommendPrice")
                               ArticleRecommendPrice articleRecommendPrice);

    int deleteRecommendArticle(String recommendId);

    ArticleRecommend getRecommendByArticleId(@Param("articleId") String articleId,
                                                        @Param("shopId") String shopIds);

    int deleteRecommendByArticleId(String id);

    List<ArticleRecommendPrice> selectByRecommendArticleInfo(String ArticleId);

    void updatePriceById(@Param("price") BigDecimal price, @Param("id") String id);

    List<ArticleRecommend> getRecommendPackage();

    List<ArticleRecommend> getBrandRecommendList(@Param("shopId") String shopId);
}
