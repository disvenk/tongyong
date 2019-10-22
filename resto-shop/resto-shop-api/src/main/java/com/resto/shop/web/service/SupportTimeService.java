package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.SupportTime;

public interface SupportTimeService extends GenericService<SupportTime, Integer> {

    List<SupportTime> selectList(String shopDetailId);

	void saveSupportTimes(String articleId, Integer[] supportTimes,String brandId,String shopId);

	List<Integer> selectByIdsArticleId(String articleId);

	/**
	 * 获取当前符合条件的供应时间
	 * @param currentShopId
	 * @return
	 */
	List<SupportTime> selectNowSopport(String currentShopId);

	Integer insertSupportTime(SupportTime supportTime);

	List<SupportTime> getSupportTimePackage();

    List<SupportTime> selectBrandList(String currentShopId);
}
