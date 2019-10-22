package com.resto.service.customer.mapper;

import com.resto.api.customer.entity.CustomerAddress;
import com.resto.conf.mybatis.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerAddressMapper extends MyMapper<CustomerAddress> {
    int updateState(@Param("customer_id") String customer_id);
}