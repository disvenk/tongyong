package com.resto.shop.web.dao;

import com.resto.shop.web.model.Permission;
import com.resto.brand.core.generic.GenericDao;

public interface PermissionMapper  extends GenericDao<Permission,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}
