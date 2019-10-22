package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.MealTemp;

public interface MealTempService extends GenericService<MealTemp, Integer> {

	void create(MealTemp mealTemp);

	MealTemp selectFull(Integer id);

	List<MealTemp> selectList(String brandId);
    
}
