package com.resto.service.shop.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.RecommendCategory;

import java.util.List;

public interface RecommendCategoryMapper extends BaseDao<RecommendCategory,String> {

    int deleteByPrimaryKey(String id);

    int insert(RecommendCategory record);

    int insertSelective(RecommendCategory record);

    RecommendCategory selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RecommendCategory record);

    int updateByPrimaryKey(RecommendCategory record);

    List<RecommendCategory> getRecommendCategoryList(String shopId);

    List<RecommendCategory> selectListSortShopId(String shopId);
}