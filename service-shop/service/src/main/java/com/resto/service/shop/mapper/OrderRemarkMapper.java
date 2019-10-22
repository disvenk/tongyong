package com.resto.service.shop.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.OrderRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface OrderRemarkMapper extends BaseDao<OrderRemark, String> {

    List<OrderRemark> selectOrderRemarkByShopId(@Param("shopId") String shopId);

    void deleteByBoOrderRemarkId(@Param("boOrderRemarkId") String boOrderRemarkId);
}
