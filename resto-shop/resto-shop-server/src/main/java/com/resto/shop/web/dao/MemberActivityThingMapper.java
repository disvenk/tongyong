package com.resto.shop.web.dao;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.MemberActivityThing;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MemberActivityThingMapper extends GenericDao<MemberActivityThing,Integer> {

    int deleteByPrimaryKey(Integer id);

    int insert(MemberActivityThing record);

    int insertSelective(MemberActivityThing record);

    MemberActivityThing selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberActivityThing record);

    int updateByPrimaryKey(MemberActivityThing record);

    MemberActivityThing selectByTelephone(String telephone);

    List<JSONObject> selectCustomerInfo(@Param("memberActivityId") Integer memberActivityId);

}
