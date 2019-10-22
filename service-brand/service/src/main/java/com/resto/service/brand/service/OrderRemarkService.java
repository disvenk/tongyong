package com.resto.service.brand.service;


import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.OrderRemark;
import com.resto.service.brand.mapper.OrderRemarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderRemarkService extends BaseService<OrderRemark, String> {

	@Autowired
	private OrderRemarkMapper orderRemarkMapper;

	public BaseDao<OrderRemark, String> getDao() {
		return orderRemarkMapper;
	}

    public List<OrderRemark> selectOrderRemarks() {
        return orderRemarkMapper.selectOrderRemarks();
    }
}
