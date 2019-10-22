package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ThirdCustomer;

public interface ThirdCustomerMapper extends BaseDao<ThirdCustomer,Long> {

    ThirdCustomer selectByTelephone(String tel);

}
