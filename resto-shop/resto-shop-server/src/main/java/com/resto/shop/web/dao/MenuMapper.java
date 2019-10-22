package com.resto.shop.web.dao;

import com.resto.shop.web.model.Menu;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper  extends GenericDao<Menu,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    List<Menu> selectMenuTypeList(@Param("menuState") String menuState,@Param("menuType") Integer menuType);

    List<Menu> selectParentIdList(Integer parentId);
}
