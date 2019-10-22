package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ThirdCustomer;

/**
 * Created by carl on 2017/8/25.
 */
public interface ThirdCustomerService extends GenericService<ThirdCustomer, Long> {

    ThirdCustomer selectByTelephone(String tel);
}
