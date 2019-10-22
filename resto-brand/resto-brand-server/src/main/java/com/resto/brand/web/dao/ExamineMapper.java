package com.resto.brand.web.dao;

import com.resto.brand.web.model.Examine;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

public interface ExamineMapper  extends GenericDao<Examine,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Examine record);

    int insertSelective(Examine record);

    Examine selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Examine record);

    int updateByPrimaryKey(Examine record);

    Examine selectByContractIdAndType(@Param("contractId") Long contractId, @Param("type") Integer type);
}
