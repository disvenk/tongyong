package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ShopCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCartMapper extends BaseDao<ShopCart,Integer> {

    int insert(ShopCart record);

    int updateByPrimaryKey(ShopCart record);

    List<ShopCart> listUserAndShop(ShopCart shopcart);

    ShopCart selectShopCartItem(ShopCart shopCart);

    void updateShopCartItem(ShopCart shopCartItem);

	void clearShopCart(String customerId, Integer distributionModeId, String shopDetailId);

	void clearAllShopCart(String customerId, String shopDetailId);

    void clearShopCartGeekPos(String userId, String shopId);

    List<ShopCart> listUserShopCart(String userId, String shopId, Integer distributionModeId);

    void delMealArticle(String id);

    void delMealItem(String articleId);

    ShopCart selectByUuId(String uuid);

    void deleteCustomerArticle(@Param("customerId") String customerId, @Param("articleId") String articleId);
}
