package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.MemberActivity;


public interface MemberActivityMapper  extends GenericDao<MemberActivity,Integer> {

    int deleteByPrimaryKey(Integer id);

    int insert(MemberActivity record);

    int insertSelective(MemberActivity record);

    MemberActivity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberActivity record);

    int updateByPrimaryKey(MemberActivity record);

}
