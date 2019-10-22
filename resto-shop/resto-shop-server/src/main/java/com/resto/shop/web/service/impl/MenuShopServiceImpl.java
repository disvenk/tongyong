package com.resto.shop.web.service.impl;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.MenuShopMapper;
import com.resto.shop.web.model.Menu;
import com.resto.shop.web.model.MenuShop;
import com.resto.shop.web.service.MenuShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;

@Component
@Service
public class MenuShopServiceImpl extends GenericServiceImpl<MenuShop, Long> implements MenuShopService {

    @Autowired
    private MenuShopMapper menushopMapper;

    @Override
    public GenericDao<MenuShop, Long> getDao() {
        return menushopMapper;
    }

    @Override
    public List<MenuShop> selectByMenuId(String menuId) {
        return menushopMapper.selectByMenuId(menuId);
    }

    @Override
    public void deleteMenuId(String menuId) {
        menushopMapper.deleteMenuId(menuId);
    }

    @Override
    public List<MenuShop> checkShopMenu(List<String> shops,Integer type) {
        return menushopMapper.checkShopMenu(shops,type);
    }

    @Override
    public void updateMenuId(MenuShop menushop) {
        menushopMapper.updateMenuId(menushop);
    }

    @Override
    public List<MenuShop> listShopMenu(String currentShopId) {
        return menushopMapper.listShopMenu(currentShopId);
    }
}
