package com.resto.shop.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.MealTempAttr;

public interface MealTempAttrMapper  extends GenericDao<MealTempAttr,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(MealTempAttr record);

    int insertSelective(MealTempAttr record);

    MealTempAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MealTempAttr record);

    int updateByPrimaryKey(MealTempAttr record);

	void deleteByTempId(Integer tempId);

	void insertBatch(@Param("attrs") List<MealTempAttr> attrs, @Param("tempId")Integer mealTempId);

	List<MealTempAttr> selectByTempId(Integer id);
}
