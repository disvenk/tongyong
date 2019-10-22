package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.IncomeMapper;
import com.resto.brand.web.model.Contract;
import com.resto.brand.web.model.Income;
import com.resto.brand.web.service.ContractService;
import com.resto.brand.web.service.IncomeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Component
@Service
public class IncomeServiceImpl extends GenericServiceImpl<Income, Long> implements IncomeService {

    @Resource
    private IncomeMapper incomeMapper;

    @Resource
	private ContractService contractService;

    @Override
    public GenericDao<Income, Long> getDao() {
        return incomeMapper;
    }

	@Override
	public void insertIncome(Income income) {

		Contract contract= contractService.selectByConstractNum(income.getConstractNum());
		//更新 合同信息
		int temp = contractService.updateMoney(income.getPayMoney(),contract.getId());
		income.setContractId(contract.getId());
		if(temp>0){
			incomeMapper.insertSelective(income);
		}


	}

	@Override
	public List<Income> selectIncomeAndContractList() {
		return incomeMapper.selectIncomeAndContractList();
	}

    @Override
    public List<Income> selectListByContractId(Long contractId) {
        return incomeMapper.selectListByContractId(contractId);
    }

    @Override
    public BigDecimal selectByConstractNum(String constractNum) {
        Contract contract = contractService.selectByConstractNum(constractNum);
        BigDecimal temp = BigDecimal.ZERO;
        List<Income> incomeList = incomeMapper.selectListByContractId(contract.getId());
        if(incomeList!=null&& !incomeList.isEmpty()){
            for(Income income:incomeList){
                temp = temp.add(income.getPayMoney());
            }
        }
        return temp;

    }
}
