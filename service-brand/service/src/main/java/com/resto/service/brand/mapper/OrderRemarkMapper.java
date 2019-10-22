package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.OrderRemark;

import java.util.List;

public interface OrderRemarkMapper extends BaseDao<OrderRemark, String> {

    public List<OrderRemark> selectOrderRemarks();
}
