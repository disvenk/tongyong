package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.MealItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MealItemMapper extends BaseDao<MealItem,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(MealItem record);

    int insertSelective(MealItem record);

    MealItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MealItem record);

    int updateByPrimaryKey(MealItem record);

	void deleteByMealAttrIds(@Param("mealAttrIds") List<Integer> mealAttrIds);

	void insertBatch(@Param("mealItems") List<MealItem> mealAttrs);

	List<MealItem> selectByAttrIds(@Param("mealAttrIds") List<Integer> mealAttrIds, @Param("show") String show);

	List<MealItem> selectByIds(@Param("ids") Integer[] mealItemIds);

    List<MealItem> selectByAttrId(Integer attrId);

    List<MealItem> selectByArticleId(String articleId);

    int updateArticleNameById(@Param("articleName") String articleName, @Param("id") Integer id);
}
