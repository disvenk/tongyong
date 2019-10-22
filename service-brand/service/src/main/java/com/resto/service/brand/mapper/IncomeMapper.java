package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Income;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncomeMapper  extends BaseDao<Income,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Income record);

    int insertSelective(Income record);

    Income selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Income record);

    int updateByPrimaryKey(Income record);

	List<Income> selectIncomeAndContractList();

    List<Income> selectListByContractId(@Param("contractId") Long contractId);

}
