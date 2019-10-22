package com.resto.brand.web.dao;

import com.resto.brand.web.model.BrandAccountStream;
import com.resto.brand.core.generic.GenericDao;

public interface BrandAccountStreamMapper  extends GenericDao<BrandAccountStream,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(BrandAccountStream record);

    int insertSelective(BrandAccountStream record);

    BrandAccountStream selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BrandAccountStream record);

    int updateByPrimaryKey(BrandAccountStream record);
}
