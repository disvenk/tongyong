package com.resto.shop.web.dao;

import com.resto.shop.web.model.OrderRefundRemark;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderRefundRemarkMapper  extends GenericDao<OrderRefundRemark,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(OrderRefundRemark record);

    int insertSelective(OrderRefundRemark record);

    OrderRefundRemark selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderRefundRemark record);

    int updateByPrimaryKey(OrderRefundRemark record);

    void posSyncDeleteByOrderId(String orderId);

    List<OrderRefundRemark> posSyncListByOrderId(@Param("orderId") String orderId);
}
