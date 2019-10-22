package com.resto.shop.web.dao;

import com.resto.shop.web.model.OrderItemActual;
import com.resto.brand.core.generic.GenericDao;

public interface OrderItemActualMapper  extends GenericDao<OrderItemActual,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(OrderItemActual record);

    int insertSelective(OrderItemActual record);

    OrderItemActual selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderItemActual record);

    int updateByPrimaryKey(OrderItemActual record);
}
