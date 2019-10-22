package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.Menu;
import com.resto.shop.web.model.MenuShop;

import java.util.List;

public interface MenuShopService extends GenericService<MenuShop, Long> {

    List<MenuShop> selectByMenuId(String menuId);

    void deleteMenuId(String menuId);

    List<MenuShop> checkShopMenu(List<String> shops,Integer type);

    void updateMenuId(MenuShop menushop);

    List<MenuShop> listShopMenu(String currentShopId);
}
