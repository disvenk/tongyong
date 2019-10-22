package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ArticlePrice;
import org.apache.ibatis.annotations.Param;

public interface ArticlePriceService extends GenericService<ArticlePrice, String> {


	void saveArticlePrices(String articleId, List<ArticlePrice> articlePrises,String brandId,String shopId);

	List<ArticlePrice> selectByArticleId(String articleId);

	List<ArticlePrice> selectList(String shopDetailId);

	ArticlePrice selectByArticle(String articleId,int unitId);

	Integer clearPriceStock(@Param("articleId") String articleId, @Param("emptyRemark") String emptyRemark);

	Integer clearPriceTotal(@Param("articleId") String articleId, @Param("emptyRemark") String emptyRemark);

	/**
	 * 更新该餐品库存 （-1）（有规格）
	 * @param articleId 餐品id
	 * @return
	 */
	Boolean updateArticlePriceStock(@Param("articleId") String articleId,@Param("type") String type,@Param("count") Integer count);

	/**
	 * 库存为0时设置沽清	---	tb_article_price
	 * @param articleId
	 * @return
	 */
	Boolean setArticlePriceEmpty(String articleId);

	/**
	 * 还原库存时重置售罄状态	---	tb_article_price
	 * @param articleId
	 * @return
	 */
	Boolean setArticlePriceEmptyFail(String articleId);
    
}
