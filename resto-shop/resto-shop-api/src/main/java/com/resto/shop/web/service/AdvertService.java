package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.Advert;

public interface AdvertService extends GenericService<Advert, Integer> {
	/**
	 * 根据店铺ID查询信息
	 * @return
	 */
	List<Advert> selectListByShopId(String shopId);

}
