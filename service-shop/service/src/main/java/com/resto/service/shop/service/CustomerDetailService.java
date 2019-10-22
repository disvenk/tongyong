package com.resto.service.shop.service;


import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.CustomerDetail;
import com.resto.service.shop.mapper.CustomerDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by carl on 2016/12/27.
 */
@Service
public class CustomerDetailService extends BaseService<CustomerDetail, String> {

    @Autowired
    CustomerDetailMapper customerDetailMapper;

    public BaseDao<CustomerDetail, String> getDao() {
        return customerDetailMapper;
    }
}
