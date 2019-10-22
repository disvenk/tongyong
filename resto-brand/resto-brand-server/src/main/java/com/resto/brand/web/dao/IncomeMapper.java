package com.resto.brand.web.dao;

import com.resto.brand.web.model.Income;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncomeMapper  extends GenericDao<Income,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Income record);

    int insertSelective(Income record);

    Income selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Income record);

    int updateByPrimaryKey(Income record);

	List<Income> selectIncomeAndContractList();

    List<Income> selectListByContractId(@Param("contractId") Long contractId);

}
