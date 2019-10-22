package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.CustomerDetail;

/**
 * Created by carl on 2016/12/27.
 */
public interface CustomerDetailMapper extends GenericDao<CustomerDetail, String> {

    int deleteByPrimaryKey(String id);

    int insert(CustomerDetail record);

    int insertSelective(CustomerDetail record);

    CustomerDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CustomerDetail record);

    int updateByPrimaryKey(CustomerDetail record);

}
