package com.resto.shop.web.dao;

import com.resto.shop.web.model.BadTop;
import com.resto.brand.core.generic.GenericDao;

public interface BadTopMapper  extends GenericDao<BadTop,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(BadTop record);

    int insertSelective(BadTop record);

    BadTop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BadTop record);

    int updateByPrimaryKey(BadTop record);
}
