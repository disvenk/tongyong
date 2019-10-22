package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.BrandTemplateRoot;

public interface BrandTemplateRootMapper extends GenericDao<BrandTemplateRoot,Integer>{
    int deleteByPrimaryKey(Integer id);

    int insert(BrandTemplateRoot record);

    int insertSelective(BrandTemplateRoot record);

    BrandTemplateRoot selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BrandTemplateRoot record);

    int updateByPrimaryKey(BrandTemplateRoot record);
}