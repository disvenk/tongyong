package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ArticleFamily;
import com.resto.service.shop.mapper.ArticleFamilyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleFamilyService extends BaseService<ArticleFamily, String> {

    @Autowired
    private ArticleFamilyMapper articlefamilyMapper;

    @Override
    public BaseDao<ArticleFamily, String> getDao() {
        return articlefamilyMapper;
    }

	public List<ArticleFamily> selectListByDistributionModeId(String currentShopId, Integer distributionModeId) {
		return articlefamilyMapper.selectListByDistributionModeId(currentShopId, distributionModeId);
	}

}
