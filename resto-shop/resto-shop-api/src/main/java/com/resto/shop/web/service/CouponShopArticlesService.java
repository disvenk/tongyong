package com.resto.shop.web.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ShopDetail;
import com.resto.shop.web.dto.ProductShopDto;
import com.resto.shop.web.form.UnionArticleForm;
import com.resto.shop.web.model.ArticleFamily;
import com.resto.shop.web.model.CouponShopArticles;

import java.util.List;

public interface CouponShopArticlesService extends GenericService<CouponShopArticles, Integer> {

    public JSONArray selectUnionArticle(Long couponId,String brandId);

    public JSONArray selectShop(Long couponId, String brandId, String shopId);

    public JSONArray selectUnionedArticle(Long couponId, String brandId, String shopId);

    public void UnionArticle(UnionArticleForm unionArticleForm,String shopId,String brandId);

    public Result grantCouponId(String phone, Long couponId);

    /**
     * 某个产品券在某个门店下有包含的核销菜品信息
     * @param couponId
     * @param shopId
     * @return
     */
    List<CouponShopArticles> selectByCouponIdShopId(Long couponId, String shopId);

}
