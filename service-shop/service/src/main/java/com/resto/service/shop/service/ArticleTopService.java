package com.resto.service.shop.service;


import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ArticleTop;
import com.resto.service.shop.mapper.ArticleTopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ArticleTopService extends BaseService<ArticleTop, Long> {

    @Autowired
    private ArticleTopMapper articletopMapper;

    public BaseDao<ArticleTop, Long> getDao() {
        return articletopMapper;
    }


}
