package com.resto.shop.web.dao;


import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.DayAppraiseMessage;
import com.resto.shop.web.model.DayAppraiseMessageWithBLOBs;

public interface DayAppraiseMessageMapper  extends GenericDao<DayAppraiseMessage,String> {
    int deleteByPrimaryKey(String id);

    int insert(DayAppraiseMessageWithBLOBs record);

    int insertSelective(DayAppraiseMessageWithBLOBs record);

    DayAppraiseMessageWithBLOBs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DayAppraiseMessageWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(DayAppraiseMessageWithBLOBs record);

    int updateByPrimaryKey(DayAppraiseMessage record);
}
