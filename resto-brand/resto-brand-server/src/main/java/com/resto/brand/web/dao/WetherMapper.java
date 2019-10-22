package com.resto.brand.web.dao;

import com.resto.brand.web.model.Wether;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;


public interface WetherMapper  extends GenericDao<Wether,Integer> {
    int deleteByPrimaryKey(Long id);

    int insert(Wether record);

    int insertSelective(Wether record);

    Wether selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Wether record);

    int updateByPrimaryKey(Wether record);

    Wether selectDateAndShopId(@Param("shopId") String shopId, @Param("dateTime") String date);
}
