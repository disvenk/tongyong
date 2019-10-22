package com.resto.shop.web.service.impl;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ArticleLibraryMapper;
import com.resto.shop.web.model.ArticleLibrary;
import com.resto.shop.web.service.ArticleLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;

@Component
@Service
public class ArticleLibraryServiceImpl extends GenericServiceImpl<ArticleLibrary, String> implements ArticleLibraryService {

    @Autowired
    private ArticleLibraryMapper articlelibraryMapper;

    @Override
    public GenericDao<ArticleLibrary, String> getDao() {
        return articlelibraryMapper;
    }


    @Override
    public void addArticleLikes(String id) {
        articlelibraryMapper.addArticleLikes(id);
    }
}
