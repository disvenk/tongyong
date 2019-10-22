package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ThirdCustomer;
import com.resto.service.shop.mapper.ThirdCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdCustomerService extends BaseService<ThirdCustomer, Long> {

    @Autowired
    private ThirdCustomerMapper thirdCustomerMapper;

    @Override
    public BaseDao<ThirdCustomer, Long> getDao() {
        return thirdCustomerMapper;
    }

    public ThirdCustomer selectByTelephone(String tel) {
        return thirdCustomerMapper.selectByTelephone(tel);
    }
}
