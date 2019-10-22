package com.resto.brand.web.dao;

import com.resto.brand.web.model.Area;
import com.resto.brand.core.generic.GenericDao;

public interface AreaMapper  extends GenericDao<Area,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);
}
