package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.ThirdCustomer;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.conf.mybatis.base.BaseService;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.customer.mapper.AccountMapper;
import com.resto.service.customer.mapper.ThirdCustomerMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by carl on 2017/8/25.
 */
@Service
public class ThirdCustomerService extends BaseService<ThirdCustomer, ThirdCustomerMapper> {

    @Resource
    private ThirdCustomerMapper thirdCustomerMapper;

    public ThirdCustomer selectByTelephone(String tel) {
        ThirdCustomer thirdCustomer = new ThirdCustomer();
        thirdCustomer.setTelephone(tel);
        return dbSelectOne(thirdCustomer);
    }
}
