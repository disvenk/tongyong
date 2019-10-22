package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ArticlePriceMapper;
import com.resto.shop.web.model.ArticlePrice;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.ArticlePriceService;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class ArticlePriceServiceImpl extends GenericServiceImpl<ArticlePrice, String> implements ArticlePriceService {

    @Resource
    private ArticlePriceMapper articlepriceMapper;

	@Autowired
	private PosService posService;

    @Override
    public GenericDao<ArticlePrice, String> getDao() {
        return articlepriceMapper;
    }

	@Override
	public void saveArticlePrices(String articleId,List<ArticlePrice> articlePrises,String brandId,String shopId) {
		articlepriceMapper.deleteArticlePrices(articleId);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(brandId);
		shopMsgChangeDto.setShopId(shopId);
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEPRICE);
        shopMsgChangeDto.setType("delete");
		shopMsgChangeDto.setId(articleId);
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		for(ArticlePrice price:articlePrises){
			price.setId(articleId+"@"+price.getUnitIds());
			price.setArticleId(articleId);
			articlepriceMapper.insertSelective(price);
			//消息通知newpos后台发生变化
			ShopMsgChangeDto yshopMsgChangeDto=new ShopMsgChangeDto();
			yshopMsgChangeDto.setBrandId(brandId);
			yshopMsgChangeDto.setShopId(shopId);
			yshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEPRICE);
			yshopMsgChangeDto.setType("add");
			yshopMsgChangeDto.setId(price.getId());
			try{
				posService.shopMsgChange(yshopMsgChangeDto);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<ArticlePrice> selectByArticleId(String articleId) {
		return articlepriceMapper.selectByArticleId(articleId);
	}

	@Override
	public List<ArticlePrice> selectList(String shopDetailId) {
		return articlepriceMapper.selectList(shopDetailId);
	}


	@Override
	public ArticlePrice selectByArticle(String articleId, int unitId) {
		return articlepriceMapper.selectByArticle(articleId, unitId);
	}

	@Override
	public Integer clearPriceStock(String articleId, String emptyRemark) {
		return articlepriceMapper.clearPriceStock(articleId, emptyRemark);
	}

	@Override
	public Integer clearPriceTotal(String articleId, String emptyRemark) {
		return articlepriceMapper.clearPriceTotal(articleId, emptyRemark);
	}

	@Override
	public Boolean updateArticlePriceStock(String articleId, String type, Integer count) {
		articlepriceMapper.updateArticlePriceStock(articleId, type,count);
		return true;
	}

	@Override
	public Boolean setArticlePriceEmpty(String articleId) {
		 articlepriceMapper.setArticlePriceEmpty(articleId);
		 return true;
	}

	@Override
	public Boolean setArticlePriceEmptyFail(String articleId) {
		 articlepriceMapper.setArticlePriceEmptyFail(articleId);
		 return true;
	}
}
