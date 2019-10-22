package com.resto.shop.web.dao;

import com.resto.shop.web.model.SvipActivity;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

public interface SvipActivityMapper  extends GenericDao<SvipActivity,String> {
    int deleteByPrimaryKey(String id);

    int insert(SvipActivity record);

    int insertSelective(SvipActivity record);

    SvipActivity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SvipActivity record);

    int updateByPrimaryKey(SvipActivity record);

    Long getOpendAct();

    SvipActivity getAct();

    Long getMyself(@Param("id")String id);
}
