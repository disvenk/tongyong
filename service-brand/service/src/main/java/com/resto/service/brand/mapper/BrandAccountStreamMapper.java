package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.BrandAccountStream;

public interface BrandAccountStreamMapper  extends BaseDao<BrandAccountStream,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(BrandAccountStream record);

    int insertSelective(BrandAccountStream record);

    BrandAccountStream selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BrandAccountStream record);

    int updateByPrimaryKey(BrandAccountStream record);
}
