package com.resto.shop.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.MealTemp;

public interface MealTempMapper  extends GenericDao<MealTemp,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(MealTemp record);

    int insertSelective(MealTemp record);

    MealTemp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MealTemp record);

    int updateByPrimaryKey(MealTemp record);

	MealTemp selectFull(Integer id);

	List<MealTemp> selectByBrandId(String brandId);

	List<MealTemp> selectList(@Param("brandId")String brandId);
}
