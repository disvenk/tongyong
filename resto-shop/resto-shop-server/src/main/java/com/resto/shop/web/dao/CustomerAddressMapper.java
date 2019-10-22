package com.resto.shop.web.dao;


import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.CustomerAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerAddressMapper extends GenericDao<CustomerAddress, String> {
    int deleteByPrimaryKey(String id);

    int insert(CustomerAddress record);

    int insertSelective(CustomerAddress record);

    CustomerAddress selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CustomerAddress record);

    int updateByPrimaryKey(CustomerAddress record);

    List<CustomerAddress> selectOneList(@Param("customer_id") String customer_id);

    int updateState(@Param("customer_id") String customer_id);
}