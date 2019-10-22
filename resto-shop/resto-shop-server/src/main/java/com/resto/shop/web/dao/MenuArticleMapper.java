package com.resto.shop.web.dao;

import com.resto.shop.web.model.MenuArticle;
import com.resto.brand.core.generic.GenericDao;

import java.util.List;

public interface MenuArticleMapper  extends GenericDao<MenuArticle,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(MenuArticle record);

    int insertSelective(MenuArticle record);

    MenuArticle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MenuArticle record);

    int updateByPrimaryKey(MenuArticle record);

    List<MenuArticle> selectListMenuId(String menuId);

    int deleteByMenuId(String menuId);

    int deleteArticleId(String articleId);
}
