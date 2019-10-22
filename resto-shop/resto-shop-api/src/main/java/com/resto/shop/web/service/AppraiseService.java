package com.resto.shop.web.service;

import com.resto.api.appraise.entity.Appraise;
import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.AppraiseShopDto;
import com.resto.shop.web.exception.AppException;
import java.util.List;
import java.util.Map;


public interface AppraiseService extends GenericService<Appraise, String> {
 


	Appraise saveAppraise(Appraise appraise) throws AppException;

	/**
	 * 查询当前店铺 的 评论列表
	 * @param currentPage	当前分页
	 * @param showCount		显示数量
	 * @param maxLevel		最大级别
	 * @param minLevel		最小级别
	 * @return
	 */
	List<Appraise> updateAndListAppraise(String currentShopId, Integer currentPage, Integer showCount, Integer maxLevel,
			Integer minLevel);

	List<AppraiseShopDto> selectAppraiseShopDto(Map<String, Object> selectMap);
}
