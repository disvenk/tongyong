package com.resto.brand.web.dao;

import com.resto.brand.web.model.RefundRemark;
import com.resto.brand.core.generic.GenericDao;

public interface RefundRemarkMapper  extends GenericDao<RefundRemark,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundRemark record);

    int insertSelective(RefundRemark record);

    RefundRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefundRemark record);

    int updateByPrimaryKey(RefundRemark record);
}
