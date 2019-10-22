package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.OrderExceptionMapper;
import com.resto.brand.web.model.OrderException;
import com.resto.brand.web.service.OrderExceptionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *
 */
@Component
@Service
public class OrderExceptionServiceImpl extends GenericServiceImpl<OrderException, String> implements OrderExceptionService {

    @Resource
    private OrderExceptionMapper orderexceptionMapper;

    @Override
    public GenericDao<OrderException, String> getDao() {
        return orderexceptionMapper;
    } 

}
