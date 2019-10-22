package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.RefundRemark;

public interface RefundRemarkMapper  extends BaseDao<RefundRemark,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundRemark record);

    int insertSelective(RefundRemark record);

    RefundRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefundRemark record);

    int updateByPrimaryKey(RefundRemark record);
}
