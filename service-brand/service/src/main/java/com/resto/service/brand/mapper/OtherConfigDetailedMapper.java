package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.OtherConfigDetailed;

public interface OtherConfigDetailedMapper  extends BaseDao<OtherConfigDetailed,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(OtherConfigDetailed record);

    int insertSelective(OtherConfigDetailed record);

    OtherConfigDetailed selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OtherConfigDetailed record);

    int updateByPrimaryKey(OtherConfigDetailed record);
}
