package com.resto.shop.web.dao;

import java.util.List;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.DeliveryPoint;

public interface DeliveryPointMapper  extends GenericDao<DeliveryPoint,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(DeliveryPoint record);

    int insertSelective(DeliveryPoint record);

    DeliveryPoint selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeliveryPoint record);

    int updateByPrimaryKey(DeliveryPoint record);

    List<DeliveryPoint> selectListById(String currentShopId);
}
