package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ArticleRecommend;
import com.resto.service.shop.mapper.ArticleRecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleRecommendService extends BaseService<ArticleRecommend, String> {

    @Autowired
    private ArticleRecommendMapper articleRecommendMapper;

    @Override
    public BaseDao<ArticleRecommend, String> getDao() {
        return articleRecommendMapper;
    }

    public ArticleRecommend getRecommentByArticleId(String articleId, String shopId) {
        return articleRecommendMapper.getRecommendByArticleId(articleId, shopId);
    }

}
