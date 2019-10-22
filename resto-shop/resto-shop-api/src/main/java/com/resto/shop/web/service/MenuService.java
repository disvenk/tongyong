package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.Menu;
import com.resto.shop.web.model.MenuArticle;

import java.util.List;

public interface MenuService extends GenericService<Menu, Long> {

    List<Menu> selectMenuTypeList(String menuState,Integer menuType);

    List<Menu> selectParentIdList(Integer parentId);

    int insert(Menu menu,List<MenuArticle> menuArticles);
}
