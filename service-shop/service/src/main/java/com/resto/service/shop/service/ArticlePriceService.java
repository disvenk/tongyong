package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ArticlePrice;
import com.resto.service.shop.mapper.ArticlePriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticlePriceService extends BaseService<ArticlePrice, String>{

    @Autowired
    private ArticlePriceMapper articlepriceMapper;

    @Override
    public BaseDao<ArticlePrice, String> getDao() {
        return articlepriceMapper;
    }

	public void saveArticlePrices(String articleId,List<ArticlePrice> articlePrises) {
		articlepriceMapper.deleteArticlePrices(articleId);
		for(ArticlePrice price:articlePrises){
			price.setId(articleId+"@"+price.getUnitIds());
			price.setArticleId(articleId);
			articlepriceMapper.insertSelective(price);
		}
	}

	public List<ArticlePrice> selectByArticleId(String articleId) {
		return articlepriceMapper.selectByArticleId(articleId);
	}

	public List<ArticlePrice> selectList(String shopDetailId) {
		return articlepriceMapper.selectList(shopDetailId);
	}

	public ArticlePrice selectByArticle(String articleId, int unitId) {
		return articlepriceMapper.selectByArticle(articleId, unitId);
	}

	public Integer clearPriceStock(String articleId, String emptyRemark) {
		return articlepriceMapper.clearPriceStock(articleId, emptyRemark);
	}

	public Integer clearPriceTotal(String articleId, String emptyRemark) {
		return articlepriceMapper.clearPriceTotal(articleId, emptyRemark);
	}

	public Boolean updateArticlePriceStock(String articleId, String type, Integer count) {
		articlepriceMapper.updateArticlePriceStock(articleId, type,count);
		return true;
	}

	public Boolean setArticlePriceEmpty(String articleId) {
		 articlepriceMapper.setArticlePriceEmpty(articleId);
		 return true;
	}

	public Boolean setArticlePriceEmptyFail(String articleId) {
		 articlepriceMapper.setArticlePriceEmptyFail(articleId);
		 return true;
	}
}
