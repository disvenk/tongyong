package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.OrderException;

public interface OrderExceptionMapper  extends BaseDao<OrderException,String> {
    int deleteByPrimaryKey(String orderId);

    int insert(OrderException record);

    int insertSelective(OrderException record);

    OrderException selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderException record);

    int updateByPrimaryKey(OrderException record);
}
