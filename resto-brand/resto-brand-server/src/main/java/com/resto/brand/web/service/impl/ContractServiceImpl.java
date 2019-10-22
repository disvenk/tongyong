package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ContractMapper;
import com.resto.brand.web.dto.brandContractNums;
import com.resto.brand.web.model.Contract;
import com.resto.brand.web.service.ContractService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Component
@Service
public class ContractServiceImpl extends GenericServiceImpl<Contract, Long> implements ContractService {

    @Resource
    private ContractMapper contractMapper;

    @Override
    public GenericDao<Contract, Long> getDao() {
        return contractMapper;
    }

	@Override
	public int updateMoney(BigDecimal payMoney, Long id) {
		return contractMapper.updateMoney(payMoney,id);
	}



    @Override
    public List<brandContractNums> selectGroupByBrandName() {
        return contractMapper.selectGroupByBrandName();
    }

    @Override
    public Contract selectByConstractNum(String constractNum) {
        return contractMapper.selectByConstractNum(constractNum);
    }
}
