package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.DistributionMode;

public interface DistributionModeMapper  extends BaseDao<DistributionMode,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributionMode record);

    int insertSelective(DistributionMode record);

    DistributionMode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributionMode record);

    int updateByPrimaryKey(DistributionMode record);
}
