package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WeReturnCustomer;

import java.util.List;

public interface WeReturnCustomerService extends GenericService<WeReturnCustomer, Long> {

    List<WeReturnCustomer> selectByShopIdAndTime(String zuoriDay, String id);

    void deleteByIds(List<Long> ids);
}
