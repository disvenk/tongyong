package com.resto.shop.web.dao;

import com.resto.shop.web.model.DayDataMessage;
import com.resto.brand.core.generic.GenericDao;


public interface DayDataMessageMapper  extends GenericDao<DayDataMessage,String> {
    int deleteByPrimaryKey(String id);

    int insert(DayDataMessage record);

    int insertSelective(DayDataMessage record);

    DayDataMessage selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DayDataMessage record);

    int updateByPrimaryKey(DayDataMessage record);
}
