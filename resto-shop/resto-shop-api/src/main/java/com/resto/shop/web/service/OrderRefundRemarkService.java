package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.OrderRefundRemark;

import java.util.List;

public interface OrderRefundRemarkService extends GenericService<OrderRefundRemark, Long> {
    /**
     * 根据 订单ID 删除
     * Pos 2.0 同步数据使用
     * @param orderId
     */
    void posSyncDeleteByOrderId(String orderId);

    /**
     *
     * @param orderRefundRemarks
     */
    void posSyncInsertList(List<OrderRefundRemark> orderRefundRemarks);

    /**
     * Pos 2.0 数据同步方法，根据 orderId 查询订单  退款项
     * @param orderId
     * @return
     */
    List<OrderRefundRemark> posSyncListByOrderId(String orderId);
}
