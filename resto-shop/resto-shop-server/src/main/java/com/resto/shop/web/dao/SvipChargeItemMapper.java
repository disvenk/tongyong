package com.resto.shop.web.dao;

import com.resto.shop.web.model.SvipChargeItem;
import com.resto.brand.core.generic.GenericDao;

public interface SvipChargeItemMapper  extends GenericDao<SvipChargeItem,String> {
    int deleteByPrimaryKey(String id);

    int insert(SvipChargeItem record);

    int insertSelective(SvipChargeItem record);

    SvipChargeItem selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SvipChargeItem record);

    int updateByPrimaryKeyWithBLOBs(SvipChargeItem record);

    int updateByPrimaryKey(SvipChargeItem record);
}
