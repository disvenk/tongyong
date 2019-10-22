package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.PictureSlider;

public interface PictureSliderService extends GenericService<PictureSlider, Integer> {
	/**
	 * 根据店铺ID查询信息
	 * @return
	 */
	List<PictureSlider> selectListByShopId(String shopId);

	
}
