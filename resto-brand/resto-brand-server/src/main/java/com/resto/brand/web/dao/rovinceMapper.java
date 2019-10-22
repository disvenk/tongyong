package com.resto.brand.web.dao;

import com.resto.brand.web.model.rovince;

public interface rovinceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(rovince record);

    int insertSelective(rovince record);

    rovince selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(rovince record);

    int updateByPrimaryKey(rovince record);
}