package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.AccountLog;
import com.resto.service.shop.mapper.AccountLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class AccountLogService extends BaseService<AccountLog, String> {

    @Autowired
    private AccountLogMapper accountlogMapper;

    @Override
    public BaseDao<AccountLog, String> getDao() {
        return accountlogMapper;
    }

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
        return accountlogMapper.selectByOrderId(orderId);
    }
}
