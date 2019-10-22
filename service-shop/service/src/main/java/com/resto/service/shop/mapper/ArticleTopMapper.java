package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ArticleTop;


public interface ArticleTopMapper extends BaseDao<ArticleTop,Long> {

    int insert(ArticleTop record);

    int updateByPrimaryKey(ArticleTop record);

}
