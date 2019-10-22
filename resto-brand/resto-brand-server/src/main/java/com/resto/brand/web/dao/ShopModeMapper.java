package com.resto.brand.web.dao;

import com.resto.brand.web.model.ShopMode;
import com.resto.brand.core.generic.GenericDao;

public interface ShopModeMapper  extends GenericDao<ShopMode,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopMode record);

    int insertSelective(ShopMode record);

    ShopMode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopMode record);

    int updateByPrimaryKey(ShopMode record);

}
