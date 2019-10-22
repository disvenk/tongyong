package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.Advert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdvertMapper extends BaseDao<Advert,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(Advert record);

    int insertSelective(Advert record);

    Advert selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Advert record);

    int updateByPrimaryKeyWithBLOBs(Advert record);

    int updateByPrimaryKey(Advert record);

    List<Advert> selectListByShopId(@Param(value = "shopId") String currentShopId);
}
