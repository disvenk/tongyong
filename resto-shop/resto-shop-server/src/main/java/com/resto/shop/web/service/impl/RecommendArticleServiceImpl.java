package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.RecommendArticleMapper;
import com.resto.shop.web.model.RecommendArticle;
import com.resto.shop.web.service.RecommendArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service
public class RecommendArticleServiceImpl extends GenericServiceImpl<RecommendArticle,Long> implements RecommendArticleService {

    @Autowired
    RecommendArticleMapper recommendArticleMapper;
    @Override
    public GenericDao<RecommendArticle, Long> getDao() {
        return recommendArticleMapper;
    }
}
