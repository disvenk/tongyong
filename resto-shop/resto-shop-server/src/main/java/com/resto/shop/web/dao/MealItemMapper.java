package com.resto.shop.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.MealItem;

public interface MealItemMapper  extends GenericDao<MealItem,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(MealItem record);

    int insertSelective(MealItem record);

    MealItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MealItem record);

    int updateByPrimaryKey(MealItem record);

	void deleteByMealAttrIds(@Param("mealAttrIds")List<Integer> mealAttrIds);

	void insertBatch(@Param("mealItems") List<MealItem> mealAttrs);

	List<MealItem> selectByAttrIds(@Param("mealAttrIds") List<Integer> mealAttrIds,@Param("show") String show);

	List<MealItem> selectByIds(@Param("ids") Integer[] mealItemIds);

    List<MealItem> selectByAttrId(Integer attrId);

    /**
     * 根据 店铺ID 查询店铺下的所有  MealItem  数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<MealItem> selectMealItemByShopId(@Param("shopId") String shopId);

    List<MealItem> selectByArticleId(String articleId);

    int updateArticleNameById(@Param("articleName") String articleName, @Param("id") Integer id);
}
