package com.resto.shop.web.service.impl;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.MenuArticleMapper;
import com.resto.shop.web.dao.MenuMapper;
import com.resto.shop.web.model.Menu;
import com.resto.shop.web.model.MenuArticle;
import com.resto.shop.web.service.MenuArticleService;
import com.resto.shop.web.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;

@Component
@Service
public class MenuServiceImpl extends GenericServiceImpl<Menu, Long> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuArticleMapper menuArticleMapper;

    @Override
    public GenericDao<Menu, Long> getDao() {
        return menuMapper;
    }

    @Override
    public List<Menu> selectMenuTypeList(String menuState,Integer menuType) {
        return menuMapper.selectMenuTypeList(menuState,menuType);
    }

    @Override
    public List<Menu> selectParentIdList(Integer parentId) {
        return menuMapper.selectParentIdList(parentId);
    }


    @Override
    public int insert(Menu menu,List<MenuArticle> menuArticles){
        int i= menuMapper.insertSelective(menu);
        if(menuArticles!=null && !menuArticles.isEmpty()){
            menuArticles.forEach(s ->{
                s.setId(null);
                s.setMenuId(menu.getId());
                menuArticleMapper.insertSelective(s);
            });
        }
        return i;
    }
}
