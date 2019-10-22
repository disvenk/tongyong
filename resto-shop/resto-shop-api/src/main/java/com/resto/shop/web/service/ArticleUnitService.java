package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ArticleUnit;
import com.resto.shop.web.model.ArticleUnitDetail;
import com.resto.shop.web.model.ArticleUnitNew;

public interface ArticleUnitService extends GenericService<ArticleUnit, Integer> {
	
	List<ArticleUnit> selectListByAttrId(Integer attrId);

	int insertByAuto(ArticleUnit articleUnit);

	ArticleUnit selectSame(String name,String attrId);

	/**
	 * 根据 店铺ID 查询店铺下的所有     ArticleUnit     数据
	 * Pos2.0 数据拉取接口			By___lmx
	 * @param shopId
	 * @return
	 */
	List<ArticleUnit> selectByShopId(String shopId);

	/**
	 * 根据 店铺ID 查询店铺下的所有     ArticleUnitDetail     数据
	 * Pos2.0 数据拉取接口			By___lmx
	 * @param shopId
	 * @return
	 */
	List<ArticleUnitDetail> selectArticleUnitDetailByShopId(String shopId);

	/**
	 * 根据 店铺ID 查询店铺下的所有     ArticleUnitNew     数据
	 * Pos2.0 数据拉取接口			By___lmx
	 * @param shopId
	 * @return
	 */
	List<ArticleUnitNew> selectArticleUnitNewByShopId(String shopId);
}
