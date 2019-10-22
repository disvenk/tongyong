package com.resto.service.customer.mapper;

import com.resto.api.customer.entity.CustomerGroup;
import com.resto.conf.mybatis.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerGroupMapper extends MyMapper<CustomerGroup> {
    List<CustomerGroup> getGroupByGroupId(String groupId);
    CustomerGroup getGroupByCustomerId(@Param("customerId") String customerId, @Param("shopId") String shopId, @Param("tableNumber") String tableNumber);
    List<CustomerGroup> getGroupByShopCart(String groupId);
}