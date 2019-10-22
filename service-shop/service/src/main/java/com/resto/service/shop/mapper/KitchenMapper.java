package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.Kitchen;
import com.resto.service.shop.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KitchenMapper extends BaseDao<Kitchen,Integer> {

    int insert(Kitchen record);

    int updateByPrimaryKey(Kitchen record);
    
    /**
     * 根据店铺ID查询信息
     * @return
     */
    List<Kitchen> selectListByShopId(@Param(value = "shopId") String currentShopId);
    
    /**
     * 删除 菜品和厨房的关联信息
     */
    void deleteArticleKitchen(@Param("articleId") String articleId);

    /**
     * 添加 菜品和厨房的关联信息
     * @param articleId
     * @param kitchenList
     */
    void insertArticleKitchen(@Param("articleId") String articleId, @Param("kitchenList") Integer[] kitchenList);

    /**
     * 根据 菜品Id 查询出和菜品关联的厨房Id
     * @param articleId
     * @return
     */
    List<Integer> selectIdsByArticleId(@Param("articleId") String articleId);


    /**
     * 根据 菜品Id 查询出和菜品相关厨房的信息
     * @param articleId
     * @return
     */
    List<Kitchen> selectInfoByArticleId(@Param("articleId") String articleId);

	Kitchen selectKitchenByMealsItemId(String id);

    Kitchen selectKitchenByOrderItem(@Param("item") OrderItem orderItem, @Param("mealAttrIds") List<Long> mealAttrId);

    List<Long> getMealAttrId(OrderItem item);

    Kitchen getItemKitchenId(OrderItem item);
}
