package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.OtherConfig;

public interface OtherConfigMapper  extends BaseDao<OtherConfig,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(OtherConfig record);

    int insertSelective(OtherConfig record);

    OtherConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OtherConfig record);

    int updateByPrimaryKey(OtherConfig record);
    
    //根据 其他配置 名称 查询 
    OtherConfig selectOtherConfigName(String otherConfigName);
}
