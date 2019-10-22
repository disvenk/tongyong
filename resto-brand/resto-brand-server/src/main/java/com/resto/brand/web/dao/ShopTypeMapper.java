package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.ShopType;

/**
 * Created by carl on 2018/04/2018/4/4
 */
public interface ShopTypeMapper extends GenericDao<ShopType,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopType record);

    int insertSelective(ShopType record);

    ShopType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopType record);

    int updateByPrimaryKey(ShopType record);

}
