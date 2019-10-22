package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.OrderRemark;
import java.util.List;

public interface OrderRemarkMapper extends GenericDao<OrderRemark, String> {

    public List<OrderRemark> selectOrderRemarks();
}
