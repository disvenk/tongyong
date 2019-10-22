package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ThirdCustomer;

/**
 * Created by carl on 2017/8/25.
 */
public interface ThirdCustomerMapper  extends GenericDao<ThirdCustomer,Long> {

    ThirdCustomer selectByTelephone(String tel);

}
