package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dto.ArticleSellDto;
import com.resto.shop.web.dao.ArticleFamilyMapper;
import com.resto.shop.web.model.ArticleFamily;
import com.resto.shop.web.report.ArticleFamilyMapperReport;
import com.resto.shop.web.service.ArticleFamilyService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class ArticleFamilyServiceImpl extends GenericServiceImpl<ArticleFamily, String> implements ArticleFamilyService {

    @Resource
    private ArticleFamilyMapper articlefamilyMapper;
    @Resource
	private ArticleFamilyMapperReport articleFamilyMapperReport;

    @Override
    public GenericDao<ArticleFamily, String> getDao() {
        return articlefamilyMapper;
    }

    @Override
	public List<ArticleFamily> selectBrandArticleFamilyList(){
		return articlefamilyMapper.selectBrandArticleFamilyList();
	}

	@Override
	public List<ArticleFamily> selectList(String currentShopId) {
		return articlefamilyMapper.selectList(currentShopId);
	}

	@Override
	public List<ArticleFamily> selectListBySort(String currentShopId, Integer currentPage, Integer showCount) {
		return articlefamilyMapper.selectListBySort(currentShopId, currentPage, showCount);
	}

	@Override
	public List<ArticleFamily> selectListByDistributionModeId(String currentShopId, Integer distributionModeId) {
		return articlefamilyMapper.selectListByDistributionModeId(currentShopId, distributionModeId);
	}

	@Override
	public String selectByName(String name) {
		return articlefamilyMapper.selectByName(name);
	}

	@Override
	public void copyBrandArticleFamily(ArticleFamily articleFamily) {
		articlefamilyMapper.copyBrandArticleFamily(articleFamily);
	}

	@Override
	public ArticleFamily checkSame(String shopId, String name) {
		return articlefamilyMapper.checkSame(shopId, name);
	}

    @Override
    public List<ArticleSellDto> selectByShopId(String shopId) {
        return articleFamilyMapperReport.selectByShopId(shopId);
    }

    @Override
    public List<ArticleFamily> selectnewPosListPage(String shopDetailId, Integer page, Integer size, Integer distributionModeId) {
        PageHelper.startPage(page,size);
        List<ArticleFamily> articleFamilyList = articlefamilyMapper.selectnewPosListPage(shopDetailId,distributionModeId);
        PageInfo<ArticleFamily> pageInfo = new PageInfo<>(articleFamilyList);
        return pageInfo.getList();
    }

	@Override
	public ArticleFamily selectByPrimaryKey(String articleFamilyId) {
		return articlefamilyMapper.selectByPrimaryKey(articleFamilyId);
	}

	@Override
	public List<ArticleFamily> articleCategory(String brandId) {

		return articlefamilyMapper.articleCategory(brandId);
	}

	@Override
	public List<ArticleSellDto> selectByShopIdNew(String shopId) {
		return articleFamilyMapperReport.selectByShopIdNew(shopId);
	}
}
