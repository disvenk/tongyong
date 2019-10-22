package com.resto.brand.web.dao;

import com.resto.brand.web.model.OrderException;
import com.resto.brand.core.generic.GenericDao;

public interface OrderExceptionMapper  extends GenericDao<OrderException,String> {
    int deleteByPrimaryKey(String orderId);

    int insert(OrderException record);

    int insertSelective(OrderException record);

    OrderException selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderException record);

    int updateByPrimaryKey(OrderException record);
}
