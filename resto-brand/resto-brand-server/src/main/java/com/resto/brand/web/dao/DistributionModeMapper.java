package com.resto.brand.web.dao;

import com.resto.brand.web.model.DistributionMode;
import com.resto.brand.core.generic.GenericDao;

public interface DistributionModeMapper  extends GenericDao<DistributionMode,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributionMode record);

    int insertSelective(DistributionMode record);

    DistributionMode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributionMode record);

    int updateByPrimaryKey(DistributionMode record);
}
