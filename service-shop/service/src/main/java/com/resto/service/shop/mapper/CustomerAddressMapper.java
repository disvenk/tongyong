package com.resto.service.shop.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.CustomerAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerAddressMapper extends BaseDao<CustomerAddress, String> {
    int deleteByPrimaryKey(String id);

    int insert(CustomerAddress record);

    int insertSelective(CustomerAddress record);

    CustomerAddress selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CustomerAddress record);

    int updateByPrimaryKey(CustomerAddress record);

    List<CustomerAddress> selectOneList(@Param("customer_id") String customer_id);

    int updateState(@Param("customer_id") String customer_id);
}