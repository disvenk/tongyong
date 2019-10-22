package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.MealTempAttr;

public interface MealTempAttrService extends GenericService<MealTempAttr, Integer> {

	void insertBatch(List<MealTempAttr> attrs, Integer mealTempId);

	List<MealTempAttr> selectByTempId(Integer id);

	void deleteByTempId(Integer tempId);

}
