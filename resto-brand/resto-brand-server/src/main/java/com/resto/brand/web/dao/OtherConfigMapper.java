package com.resto.brand.web.dao;

import com.resto.brand.web.model.OtherConfig;
import com.resto.brand.core.generic.GenericDao;

public interface OtherConfigMapper  extends GenericDao<OtherConfig,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(OtherConfig record);

    int insertSelective(OtherConfig record);

    OtherConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OtherConfig record);

    int updateByPrimaryKey(OtherConfig record);
    
    //根据 其他配置 名称 查询 
    OtherConfig selectOtherConfigName(String otherConfigName);
}
