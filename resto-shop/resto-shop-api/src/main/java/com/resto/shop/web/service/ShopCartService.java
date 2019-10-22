package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ShopCart;

public interface ShopCartService extends GenericService<ShopCart, Integer> {

    List<ShopCart> listUserAndShop(ShopCart shopcart);

    Integer updateShopCart(ShopCart shopCart);

	void clearShopCart(String customerId, Integer distributionModeId, String shopDetailId);

	void clearShopCart(String currentCustomerId, String currentShopId);

	void deleteByGroup(String groupId);

    void clearShopCartGeekPos(String userId, String shopId);

    List<ShopCart> listUserShopCart(String userId, String shopId, Integer distributionModeId);

    void delMealArticle(String id);

    void delMealItem(String articleId);

    ShopCart selectByUuId(String uuid);

    void clearGroupId(Integer id);

    void resetGroupId(String groupId);


    void deleteCustomerArticle(String customerId,String articleId);

    /**
     * 把用户在这家店铺的购物车同步给某个组
     * @param customerId
     * @param shopId
     * @param groupId
     */
    void updateGroupNew(String customerId,String shopId,String groupId);

    /**
     * 判断菜品是否重复
     */
    Integer checkRepeat(String articleId,String groupId,String customerId);

    Integer checkRepeatRecommendGroupId(String articleId,String groupId);

    Integer checkRepeatRecommendCustomerId(String articleId,String customerId);

    List<ShopCart> getListByGroupId(String groupId);

    /**
     * 去重该组下不同点餐的人员
     * @param groupId
     * @return
     */
    List<String>  getListByGroupIdDistinctCustomerId(String groupId);

    /**
     * 将购物车中groupId为空的更新groupid
     */
    void updateShopCartByGroupId(String groupId,String shopId,String customerId);

    /**
     * 更新推荐菜为单品
     * @param id
     */
    void  updateShopCartRecommend(Integer id);

    List<ShopCart> selectByArticleIdAndcustomerId(String articleId,String customerId);
}
