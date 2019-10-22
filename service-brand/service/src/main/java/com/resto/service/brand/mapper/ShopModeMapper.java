package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.ShopMode;

public interface ShopModeMapper  extends BaseDao<ShopMode,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopMode record);

    int insertSelective(ShopMode record);

    ShopMode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopMode record);

    int updateByPrimaryKey(ShopMode record);

}
