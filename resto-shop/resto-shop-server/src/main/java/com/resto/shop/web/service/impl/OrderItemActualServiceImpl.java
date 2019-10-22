package com.resto.shop.web.service.impl;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.OrderItemActualMapper;
import com.resto.shop.web.model.OrderItemActual;
import com.resto.shop.web.service.OrderItemActualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;

@Component
@Service
public class OrderItemActualServiceImpl extends GenericServiceImpl<OrderItemActual, Long> implements OrderItemActualService {

    @Autowired
    private OrderItemActualMapper orderitemactualMapper;

    @Override
    public GenericDao<OrderItemActual, Long> getDao() {
        return orderitemactualMapper;
    } 

}
