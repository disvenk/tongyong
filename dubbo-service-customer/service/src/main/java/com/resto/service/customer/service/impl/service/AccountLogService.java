package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.AccountLog;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.customer.mapper.AccountLogMapper;
import com.resto.service.customer.mapper.AccountMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class AccountLogService extends BaseServiceResto<AccountLog, AccountLogMapper> {

    @Resource
    private AccountLogMapper accountlogMapper;

	public List<AccountLog> selectLogsByAccountId(String accountId) {
		return accountlogMapper.selectLogsByAccountId(accountId);
	}

    public int selectByCustomerIdNumber(String id) {
        List<AccountLog> list = accountlogMapper.selectByCustomerIdNumber(id);
        return list.size();
    }

	public List<String> selectBrandMarketing(Map<String, String> selectMap) {
		return accountlogMapper.selectBrandMarketing(selectMap);
	}

    public BigDecimal selectByShareMoney(String accountId) {
        return accountlogMapper.selectByShareMoney(accountId);
    }

    public Integer selectByShareMoneyCount(String accountId) {
        return accountlogMapper.selectByShareMoneyCount(accountId);
    }

    public AccountLog selectByOrderId(String orderId) {
	    AccountLog accountLog = new AccountLog();
	    accountLog.setOrderId(orderId);
        return dbSelectOne(accountLog);
    }
}
