package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.AccountLog;
import com.resto.shop.web.model.SvipChargeOrder;

public interface SvipChargeOrderMapper extends GenericDao<SvipChargeOrder,String> {
    int deleteByPrimaryKey(String id);

    int insert(SvipChargeOrder record);

    int insertSelective(SvipChargeOrder record);

    SvipChargeOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SvipChargeOrder record);

    int updateByPrimaryKey(SvipChargeOrder record);
}