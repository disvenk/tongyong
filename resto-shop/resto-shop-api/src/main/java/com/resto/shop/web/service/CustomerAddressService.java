package com.resto.shop.web.service;


import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.CustomerAddress;

import java.util.List;

/**
 * Created by yangwei on 2017/6/12.
 */
public interface CustomerAddressService  extends GenericService<CustomerAddress, String> {

    int deleteByPrimaryKey(String id);

    int insert(CustomerAddress record);

    int insertSelective(CustomerAddress record);

    CustomerAddress selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CustomerAddress record);

    int updateByPrimaryKey(CustomerAddress record);

    List<CustomerAddress> selectOneList(String customer_id);

}
