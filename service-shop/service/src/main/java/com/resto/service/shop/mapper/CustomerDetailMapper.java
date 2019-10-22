package com.resto.service.shop.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.CustomerDetail;

/**
 * Created by carl on 2016/12/27.
 */
public interface CustomerDetailMapper extends BaseDao<CustomerDetail, String> {

    int deleteByPrimaryKey(String id);

    int insert(CustomerDetail record);

    int insertSelective(CustomerDetail record);

    CustomerDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CustomerDetail record);

    int updateByPrimaryKey(CustomerDetail record);

}
