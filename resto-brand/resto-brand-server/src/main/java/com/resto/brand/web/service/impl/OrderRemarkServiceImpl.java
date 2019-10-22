package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.OrderRemarkMapper;
import com.resto.brand.web.model.OrderRemark;
import com.resto.brand.web.service.OrderRemarkService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Service
public class OrderRemarkServiceImpl extends GenericServiceImpl<OrderRemark, String> implements OrderRemarkService {

	@Resource
	private OrderRemarkMapper orderRemarkMapper;
	
	@Override
	public GenericDao<OrderRemark, String> getDao() {
		return orderRemarkMapper;
	}

    @Override
    public List<OrderRemark> selectOrderRemarks() {
        return orderRemarkMapper.selectOrderRemarks();
    }
}
