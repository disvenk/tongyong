package com.resto.shop.web.dao;

import com.resto.shop.web.model.WeOrder;
import com.resto.brand.core.generic.GenericDao;

public interface WeOrderMapper  extends GenericDao<WeOrder,Integer> {
    int deleteByPrimaryKey(Long id);

    int insert(WeOrder record);

    int insertSelective(WeOrder record);

    WeOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeOrder record);

    int updateByPrimaryKey(WeOrder record);
}
