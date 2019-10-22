package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ArticleRecommendMapper;
import com.resto.shop.web.model.ArticleRecommend;
import com.resto.shop.web.model.ArticleRecommendPrice;
import com.resto.shop.web.service.ArticleRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by KONATA on 2016/9/8.
 */
@Component
@Service
public class ArticleRecommendServiceImpl extends GenericServiceImpl<ArticleRecommend, String>
        implements ArticleRecommendService {


    @Autowired
    private ArticleRecommendMapper articleRecommendMapper;



    @Override
    public List<ArticleRecommend> getRecommendList(String shopId) {
        List<ArticleRecommend> articleRecommends = articleRecommendMapper.getRecommendList(shopId);
        return  articleRecommends;
    }

    @Override
    public GenericDao<ArticleRecommend, String> getDao() {
        return articleRecommendMapper;
    }

    @Override
    public void insertRecommendArticle(String recommendId, List<ArticleRecommendPrice> articleRecommendPrices) {
        if(StringUtils.isEmpty(recommendId)){
            return ;
        }

        for(ArticleRecommendPrice articleRecommendPrice : articleRecommendPrices){
            articleRecommendMapper.insertRecommendArticle(recommendId, articleRecommendPrice);
        }


    }

    @Override
    public ArticleRecommend getRecommendById(String id) {
        return articleRecommendMapper.getRecommendById(id);
    }

    @Override
    public void updateRecommendArticle(String id,List<ArticleRecommendPrice> articleRecommendPrices) {

        articleRecommendMapper.deleteRecommendArticle(id);

        for(ArticleRecommendPrice articleRecommendPrice : articleRecommendPrices){
            articleRecommendMapper.insertRecommendArticle(id, articleRecommendPrice);
        }

    }

    @Override
    public ArticleRecommend getRecommentByArticleId(String articleId, String shopId) {
        return articleRecommendMapper.getRecommendByArticleId(articleId, shopId);
    }

    @Override
    public void deleteRecommendByArticleId(String articleId) {
         articleRecommendMapper.deleteRecommendByArticleId(articleId);
    }

    @Override
    public List<ArticleRecommendPrice> selectByRecommendArticleInfo(String articleId) {
        return articleRecommendMapper.selectByRecommendArticleInfo( articleId);
    }

    @Override
    public void updatePriceById(BigDecimal price, String id) {
        articleRecommendMapper.updatePriceById(price, id);
    }

    @Override
    public List<ArticleRecommend> getRecommendPackage() {
        return articleRecommendMapper.getRecommendPackage();
    }

    @Override
    public List<ArticleRecommend> getBrandRecommendList(String shopId) {
        return articleRecommendMapper.getBrandRecommendList(shopId);
    }
}
