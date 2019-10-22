package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.RedConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RedConfigMapper extends BaseDao<RedConfig,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(RedConfig record);

    int insertSelective(RedConfig record);

    RedConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RedConfig record);

    int updateByPrimaryKey(RedConfig record);
    
    //根据店铺ID查询信息
    List<RedConfig> selectListByShopId(@Param(value = "shopId") String currentShopId);
}
