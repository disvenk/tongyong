package com.resto.shop.web.dao;

import com.resto.shop.web.model.BonusLog;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BonusLogMapper  extends GenericDao<BonusLog,String> {
    int deleteByPrimaryKey(String id);

    int insert(BonusLog record);

    int insertSelective(BonusLog record);

    BonusLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BonusLog record);

    int updateByPrimaryKey(BonusLog record);

    List<Map<String, Object>> selectAllBonusLog(@Param("id") String id);

    List<Map<String, Object>> selectBonusLogBySelectMap(Map<String, Object> selectMap);
}
