package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.OrderPaymentItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderPaymentItemMapper extends BaseDao<OrderPaymentItem,String> {

    List<OrderPaymentItem> selectByOrderId(String orderId);

    int deleteByOrderId(String orderId);

    OrderPaymentItem insertByBeforePay(OrderPaymentItem record);

}
