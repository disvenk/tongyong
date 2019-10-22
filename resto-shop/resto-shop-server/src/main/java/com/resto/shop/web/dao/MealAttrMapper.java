package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.MealAttr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MealAttrMapper extends GenericDao<MealAttr, Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(MealAttr record);

    int insertSelective(MealAttr record);

    MealAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MealAttr record);

    int updateByPrimaryKey(MealAttr record);

    List<MealAttr> selectList(@Param("articleId") String article_id);

    void deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据 店铺ID 查询店铺下的所有  MealAttr  数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<MealAttr> selectMealAttrByShopId(@Param("shopId") String shopId);

}
