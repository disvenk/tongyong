package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Income;

import java.math.BigDecimal;
import java.util.List;

public interface IncomeService extends GenericService<Income, Long> {

	void insertIncome(Income income);


	List<Income> selectIncomeAndContractList();

    List<Income> selectListByContractId(Long contractId);

    /**
     * 查询改合同剩余最大可以开发票金额
     * @param constractNum
     * @return
     */
    BigDecimal selectByConstractNum(String constractNum);

}
