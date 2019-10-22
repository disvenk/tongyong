package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.brandContractNums;
import com.resto.brand.web.model.Contract;

import java.math.BigDecimal;
import java.util.List;

public interface ContractService extends GenericService<Contract, Long> {


	int  updateMoney(BigDecimal payMoney, Long id);

    List<brandContractNums> selectGroupByBrandName();

    Contract selectByConstractNum(String constractNum);


}
