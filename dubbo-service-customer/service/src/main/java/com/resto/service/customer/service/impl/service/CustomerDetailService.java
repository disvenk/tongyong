package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.CustomerDetailMapper;
import com.resto.shop.web.model.CustomerDetail;
import com.resto.shop.web.service.CustomerDetailService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by carl on 2016/12/27.
 */
@Component
@Service
public class CustomerDetailService extends GenericServiceImpl<CustomerDetail, String> implements CustomerDetailService {

    @Resource
    CustomerDetailMapper customerDetailMapper;

    @Override
    public GenericDao<CustomerDetail, String> getDao() {
        return customerDetailMapper;
    }
}
