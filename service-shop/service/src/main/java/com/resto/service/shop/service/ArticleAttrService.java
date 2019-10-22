package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ArticleAttr;
import com.resto.service.shop.entity.ArticleUnit;
import com.resto.service.shop.mapper.ArticleAttrMapper;
import com.resto.service.shop.mapper.ArticleUnitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleAttrService extends BaseService<ArticleAttr, Integer> {

    @Autowired
    private ArticleAttrMapper articleattrMapper;
    @Autowired
	private ArticleUnitMapper articleUnitMapper;

    @Override
    public BaseDao<ArticleAttr, Integer> getDao() {
        return articleattrMapper;
    }

    /**
     * 根据店铺ID查询信息
     */
	public List<ArticleAttr> selectListByShopId(String shopId) {
		List<ArticleAttr> articleAttrs = articleattrMapper.selectListByShopId(shopId);
		for(ArticleAttr articleAttr : articleAttrs){
			List<ArticleUnit> articleUnit = articleUnitMapper.selectListByAttrId(articleAttr.getId());
			articleAttr.setArticleUnits(articleUnit);
		}
		return articleAttrs;
	}

}
