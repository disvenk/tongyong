package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ThirdCustomerMapper;
import com.resto.shop.web.model.ThirdCustomer;
import com.resto.shop.web.service.ThirdCustomerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by carl on 2017/8/25.
 */
@Component
@Service
public class ThirdCustomerServiceImpl extends GenericServiceImpl<ThirdCustomer, Long> implements ThirdCustomerService {

    @Resource
    private ThirdCustomerMapper thirdCustomerMapper;

    @Override
    public GenericDao<ThirdCustomer, Long> getDao() {
        return thirdCustomerMapper;
    }

    @Override
    public ThirdCustomer selectByTelephone(String tel) {
        return thirdCustomerMapper.selectByTelephone(tel);
    }
}
