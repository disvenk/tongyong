package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.MealItem;

public interface MealItemService extends GenericService<MealItem, Integer> {

	void deleteByMealAttrIds(List<Integer> mealAttrIds);

	void insertBatch(List<MealItem> mealItems);

	List<MealItem> selectByAttrIds(List<Integer> ids,String show);

	List<MealItem> selectByIds(Integer[] mealItemIds);

	List<MealItem> selectByAttrId(Integer attrId);

	/**
	 * 根据 店铺ID 查询店铺下的所有  MealItem  数据
	 * Pos2.0 数据拉取接口			By___lmx
	 * @param shopId
	 * @return
	 */
	List<MealItem> selectMealItemByShopId(String shopId);

	List<MealItem> selectByArticleId(String articleId);

	int updateArticleNameById(String articleName, Integer id);

}
