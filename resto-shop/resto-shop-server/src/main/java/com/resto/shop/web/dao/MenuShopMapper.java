package com.resto.shop.web.dao;

import com.resto.shop.web.model.Menu;
import com.resto.shop.web.model.MenuShop;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuShopMapper  extends GenericDao<MenuShop,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(MenuShop record);

    int insertSelective(MenuShop record);

    MenuShop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MenuShop record);

    int updateByPrimaryKey(MenuShop record);

    List<MenuShop> selectByMenuId(@Param("menuId")String menuId);

    void deleteMenuId(String menuId);

    List<MenuShop> checkShopMenu(@Param("shops") List<String> shops,@Param("type") Integer type);

    void updateMenuId(MenuShop menushop);

    List<MenuShop> listShopMenu(String currentShopId);
}
