package com.resto.shop.web.dao;

import com.resto.shop.web.model.PosUser;
import com.resto.brand.core.generic.GenericDao;

public interface PosUserMapper  extends GenericDao<PosUser,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(PosUser record);

    int insertSelective(PosUser record);

    PosUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PosUser record);

    int updateByPrimaryKey(PosUser record);
}
