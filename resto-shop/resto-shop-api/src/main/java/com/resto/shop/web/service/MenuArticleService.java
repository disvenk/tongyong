package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.MenuArticle;

import java.util.List;

public interface MenuArticleService extends GenericService<MenuArticle, Long> {

    List<MenuArticle> selectListMenuId(String menuId);

    int deleteByMenuId(String menuId);

    int deleteArticleId(String articleId);

    void updateMenuArticle(MenuArticle menuarticle);

    void insertMenuArticle(MenuArticle s);
}
