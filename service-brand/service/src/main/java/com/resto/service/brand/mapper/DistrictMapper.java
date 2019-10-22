package com.resto.service.brand.mapper;



import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.District;

import java.util.List;

public interface DistrictMapper extends BaseDao<District,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(District record);

    int insertSelective(District record);

    District selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(District record);

    int updateByPrimaryKey(District record);

    List<District> selectByCityId(Integer cityId);
    List<District> selectList();
}