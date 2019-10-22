package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.CustomerGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
public interface CustomerGroupMapper extends GenericDao<CustomerGroup, Long> {

    void removeGroupByCustomerId(@Param("shopId") String shopId,@Param("tableNumber") String tableNumber,@Param("customerId") String customerId);

    List<CustomerGroup> getGroupByGroupId(String groupId);

    CustomerGroup getGroupByCustomerId(@Param("customerId") String customerId,@Param("shopId") String shopId,@Param("tableNumber") String tableNumber);

    List<CustomerGroup> getGroupByShopCart(String groupId);

    void removeByGroupId(String groupId);
}
