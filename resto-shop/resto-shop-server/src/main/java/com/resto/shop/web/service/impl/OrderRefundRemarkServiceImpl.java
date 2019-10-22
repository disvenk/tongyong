package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.OrderRefundRemarkMapper;
import com.resto.shop.web.model.OrderRefundRemark;
import com.resto.shop.web.service.OrderRefundRemarkService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class OrderRefundRemarkServiceImpl extends GenericServiceImpl<OrderRefundRemark, Long> implements OrderRefundRemarkService {

    @Resource
    private OrderRefundRemarkMapper orderrefundremarkMapper;

    @Override
    public GenericDao<OrderRefundRemark, Long> getDao() {
        return orderrefundremarkMapper;
    }

    @Override
    public void posSyncDeleteByOrderId(String orderId) {
        orderrefundremarkMapper.posSyncDeleteByOrderId(orderId);
    }

    @Override
    public void posSyncInsertList(List<OrderRefundRemark> orderRefundRemarks) {
        if(orderRefundRemarks != null && orderRefundRemarks.size() > 0){
            for(OrderRefundRemark orderRefundRemark : orderRefundRemarks){
                orderrefundremarkMapper.insertSelective(orderRefundRemark);
            }
        }
    }

    @Override
    public List<OrderRefundRemark> posSyncListByOrderId(String orderId) {
        return orderrefundremarkMapper.posSyncListByOrderId(orderId);
    }
}
