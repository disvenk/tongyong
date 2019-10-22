package com.resto.brand.web.dao;

import com.resto.brand.web.model.UserGroup;
import com.resto.brand.core.generic.GenericDao;

public interface UserGroupMapper  extends GenericDao<UserGroup,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(UserGroup record);

    int insertSelective(UserGroup record);

    UserGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserGroup record);

    int updateByPrimaryKey(UserGroup record);
}
