package com.resto.shop.web.service;

import java.math.BigDecimal;
import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.RedConfig;

public interface RedConfigService extends GenericService<RedConfig, Long> {
    
	/**
	 * 根据店铺ID查询信息
	 * @return
	 */
	List<RedConfig> selectListByShopId(String shopId);

	BigDecimal nextRedAmount(Order order);
}
