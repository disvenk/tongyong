package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.OrderRemark;

import java.util.List;

public interface OrderRemarkService extends GenericService<OrderRemark, String> {

    public List<OrderRemark> selectOrderRemarks();
}
