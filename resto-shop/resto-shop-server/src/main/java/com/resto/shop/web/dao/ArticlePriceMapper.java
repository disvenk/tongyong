package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ArticlePrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticlePriceMapper extends GenericDao<ArticlePrice, String> {
    int deleteByPrimaryKey(String id);

    int insert(ArticlePrice record);

    int insertSelective(ArticlePrice record);

    ArticlePrice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ArticlePrice record);

    int updateByPrimaryKey(ArticlePrice record);

    void deleteArticlePrices(String articleId);

    List<ArticlePrice> selectByArticleId(String articleId);

    List<ArticlePrice> selectList(@Param("shopId") String shopDetailId);

    ArticlePrice selectByArticle(@Param("articleId") String articleId, @Param("unitId") int unitId);

    Integer clearPriceStock(@Param("articleId") String articleId, @Param("emptyRemark") String emptyRemark);

    Integer clearPriceTotal(@Param("articleId") String articleId, @Param("emptyRemark") String emptyRemark);

    /**
     * 还原库存时重置售罄状态	---	tb_article_price
     * @param articleId
     * @return
     */
    void setArticlePriceEmptyFail(String articleId);

    /**
     * 更新该餐品库存 （-1）（有规格）
     * @param articleId 餐品id
     * @return
     */
    void updateArticlePriceStock(@Param("articleId") String articleId,@Param("type") String type,@Param("count") Integer count);


    /**
     * 库存为0时设置沽清	---	tb_article_price
     * @param articleId
     * @return
     */
    void setArticlePriceEmpty(String articleId);



}
