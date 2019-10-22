package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.ArticleSellDto;
import com.resto.shop.web.model.ArticleFamily;

import java.util.List;

public interface ArticleFamilyService extends GenericService<ArticleFamily, String>{

	List<ArticleFamily> selectBrandArticleFamilyList();

	List<ArticleFamily> selectList(String currentShopId);

	List<ArticleFamily> selectListBySort(String currentShopId, Integer currentPage, Integer showCount);
    
	List<ArticleFamily> selectListByDistributionModeId(String currentShopId,Integer distributionModeId);

	String selectByName(String Name);

	/**
	 * 将品牌菜品分类授权给店铺
	 * @Param articleFamily 菜品分类
     */
	void copyBrandArticleFamily(ArticleFamily articleFamily);

	/**
	 * 判断该店铺是否拥有指定分类名称
	 * @param shopId
	 * @param name
     * @return
     */
	ArticleFamily checkSame(String shopId,String name);

    List<ArticleSellDto> selectByShopId(String shopId);

    /**
     * 新版pos 查询菜品分类 分页
     * @param shopDetailId
     * @param page
     * @param size
     * @param distributionModeId
     * @return
     */
    List<ArticleFamily> selectnewPosListPage(String shopDetailId,Integer page,Integer size,Integer distributionModeId);

	ArticleFamily selectByPrimaryKey(String articleFamilyId);

	List<ArticleFamily> articleCategory(String brandId);

    List<ArticleSellDto> selectByShopIdNew(String shopId);
}
