package com.resto.shop.web.dao;

import com.resto.shop.web.model.Article;
import com.resto.shop.web.model.ArticleFamily;
import com.resto.shop.web.model.CouponShopArticles;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponShopArticlesMapper  extends GenericDao<CouponShopArticles,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponShopArticles record);

    int insertSelective(CouponShopArticles record);

    CouponShopArticles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponShopArticles record);

    int updateByPrimaryKey(CouponShopArticles record);

    List<ArticleFamily> selectArticleFamily(@Param("shopId")String shopId);

    List<Article> selectArticle(@Param("articleFamilyId")String articleFamilyId);

    List<Article> selectArticleByShopId(@Param("shopId")String shopId);

    List<CouponShopArticles> selectByCouponId(@Param("couponId")Long couponId);

    List<CouponShopArticles> selectByShopId(@Param("shopId")String shopId,@Param("couponId")Long couponId);

    int delectByCouponId(@Param("couponId")Long couponId);

    List<CouponShopArticles> selectByCouponIdShopId(@Param("couponId") Long couponId, @Param("shopId") String shopId);
}
