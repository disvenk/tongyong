package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Examine;
import org.apache.ibatis.annotations.Param;

public interface ExamineMapper  extends BaseDao<Examine,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Examine record);

    int insertSelective(Examine record);

    Examine selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Examine record);

    int updateByPrimaryKey(Examine record);

    Examine selectByContractIdAndType(@Param("contractId") Long contractId, @Param("type") Integer type);
}
