package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.OrderPaymentItem;
import com.resto.service.shop.mapper.OrderPaymentItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderPaymentItemService extends BaseService<OrderPaymentItem, String> {

    @Autowired
    private OrderPaymentItemMapper orderpaymentitemMapper;

    @Override
    public BaseDao<OrderPaymentItem, String> getDao() {
        return orderpaymentitemMapper;
    }

    public List<OrderPaymentItem> selectByOrderId(String orderId) {
        return orderpaymentitemMapper.selectByOrderId(orderId);
    }

    public int deleteByOrderId(String orderId) {
        return orderpaymentitemMapper.deleteByOrderId(orderId);
    }

    public OrderPaymentItem insertByBeforePay(OrderPaymentItem orderPaymentItem) {
        return orderpaymentitemMapper.insertByBeforePay(orderPaymentItem);
    }

}
