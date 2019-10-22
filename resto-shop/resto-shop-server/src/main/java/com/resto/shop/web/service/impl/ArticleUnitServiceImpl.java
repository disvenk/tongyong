package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ArticleUnitMapper;
import com.resto.shop.web.model.ArticleUnit;
import com.resto.shop.web.model.ArticleUnitDetail;
import com.resto.shop.web.model.ArticleUnitNew;
import com.resto.shop.web.service.ArticleUnitService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class ArticleUnitServiceImpl extends GenericServiceImpl<ArticleUnit, Integer> implements ArticleUnitService {

    @Resource
    private ArticleUnitMapper articleunitMapper;

    @Override
    public GenericDao<ArticleUnit, Integer> getDao() {
        return articleunitMapper;
    }

	@Override
	public List<ArticleUnit> selectListByAttrId(Integer attrId) {
		return articleunitMapper.selectListByAttrId(attrId);
	}

    @Override
    public int insertByAuto(ArticleUnit articleUnit) {
        return articleunitMapper.insertByAuto(articleUnit);
    }

    @Override
    public ArticleUnit selectSame(String name, String attrId) {
        return articleunitMapper.selectSame(name, attrId);
    }

    @Override
    public List<ArticleUnit> selectByShopId(String shopId) {
        return articleunitMapper.selectByShopId(shopId);
    }

    @Override
    public List<ArticleUnitDetail> selectArticleUnitDetailByShopId(String shopId) {
        return articleunitMapper.selectArticleUnitDetailByShopId(shopId);
    }

    @Override
    public List<ArticleUnitNew> selectArticleUnitNewByShopId(String shopId) {
        return articleunitMapper.selectArticleUnitNewByShopId(shopId);
    }
}
