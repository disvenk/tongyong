package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.TableGroup;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
public interface TableGroupMapper extends GenericDao<TableGroup, Long>{
    List<TableGroup> getTableGroupByShopId(@Param("shopId") String shopId,@Param("tableNumber") String tableNumber);

    TableGroup getTableGroupByState(@Param("shopId") String shopId
            ,@Param("customerId") String customerId,@Param("tableNumber") String tableNumber,@Param("state") Integer state);



    TableGroup getTableGroupByCustomer(@Param("tableNumber") String tableNumber,@Param("customerId") String customerId
            ,@Param("shopId") String shopId);

    TableGroup selectByGroupId(String groupId);

    void releaseTableGroup (Date time);
}
