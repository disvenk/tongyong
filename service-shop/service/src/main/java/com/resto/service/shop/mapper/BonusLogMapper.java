package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.BonusLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BonusLogMapper extends BaseDao<BonusLog,String> {

    int insert(BonusLog record);

    int updateByPrimaryKey(BonusLog record);

    List<Map<String, Object>> selectAllBonusLog(@Param("id") String id);

    List<Map<String, Object>> selectBonusLogBySelectMap(Map<String, Object> selectMap);
}
