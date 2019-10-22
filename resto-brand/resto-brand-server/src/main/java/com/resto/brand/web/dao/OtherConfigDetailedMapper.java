package com.resto.brand.web.dao;

import com.resto.brand.web.model.OtherConfigDetailed;
import com.resto.brand.core.generic.GenericDao;

public interface OtherConfigDetailedMapper  extends GenericDao<OtherConfigDetailed,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(OtherConfigDetailed record);

    int insertSelective(OtherConfigDetailed record);

    OtherConfigDetailed selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OtherConfigDetailed record);

    int updateByPrimaryKey(OtherConfigDetailed record);
}
