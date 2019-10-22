package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Wether;
import org.apache.ibatis.annotations.Param;

public interface WetherMapper  extends BaseDao<Wether,Integer> {
    int deleteByPrimaryKey(Long id);

    int insert(Wether record);

    int insertSelective(Wether record);

    Wether selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Wether record);

    int updateByPrimaryKey(Wether record);

    Wether selectDateAndShopId(@Param("shopId") String shopId, @Param("dateTime") String date);
}
