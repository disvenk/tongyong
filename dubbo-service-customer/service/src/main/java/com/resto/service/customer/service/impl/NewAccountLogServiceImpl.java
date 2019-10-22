package com.resto.service.customer.service.impl;

import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.AccountLog;
import com.resto.api.customer.service.NewAccountLogService;
import com.resto.service.customer.service.impl.service.AccountLogService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-10-18 13:48
 */

@RestController
public class NewAccountLogServiceImpl implements NewAccountLogService {

    @Resource
    private AccountLogService accountLogService;

    @Override
    public AccountLog dbSave(String brandId, AccountLog accountLog) {
        accountLogService.dbSave(accountLog);
        return accountLog;
    }

    @Override
    public int dbInsertList(String brandId, List<AccountLog> list) {
        return accountLogService.dbInsertList(list);
    }

    @Override
    public int dbDelete(String brandId, AccountLog accountLog) {
        return accountLogService.dbDelete(accountLog);
    }

    @Override
    public int dbDeleteByPrimaryKey(String brandId, Object var) {
        return accountLogService.dbDeleteByPrimaryKey(var.toString());
    }

    @Override
    public int dbDeleteByIds(String brandId, String var) {
        return accountLogService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(String brandId, AccountLog accountLog) {
        return accountLogService.dbUpdate(accountLog);
    }

    @Override
    public List<AccountLog> dbSelect(String brandId, AccountLog t) {
        return accountLogService.dbSelect(t);
    }

    @Override
    public List<AccountLog> dbSelectPage(String brandId, AccountLog t, Integer pageNum, Integer pageSize) {
        return accountLogService.dbSelectPage(t,pageNum ,pageSize );
    }

    @Override
    public AccountLog dbSelectByPrimaryKey(String brandId, Object key) {
        return accountLogService.dbSelectByPrimaryKey(key );
    }

    @Override
    public AccountLog dbSelectOne(String brandId, AccountLog record) {
        return accountLogService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(String brandId, AccountLog record) {
        return accountLogService.dbSelectCount(record);
    }

    @Override
    public List<AccountLog> dbSelectByIds(String brandId, String ids) {
        return accountLogService.dbSelectByIds(ids);
    }

    @Override
    public List<AccountLog> selectLogsByAccountId(String brandId, String accountId) {
        return accountLogService.selectLogsByAccountId(accountId);
    }

    @Override
    public int selectByCustomerIdNumber(String brandId, String id) {
        return accountLogService.selectByCustomerIdNumber(id);
    }

    @Override
    public List<String> selectBrandMarketing(String brandId, Map<String, String> selectMap) {
        return accountLogService.selectBrandMarketing(selectMap);
    }

    @Override
    public BigDecimal selectByShareMoney(String brandId, String accountId) {
        return accountLogService.selectByShareMoney(accountId);
    }

    @Override
    public Integer selectByShareMoneyCount(String brandId, String accountId) {
        return accountLogService.selectByShareMoneyCount(accountId);
    }

    @Override
    public AccountLog selectByOrderId(String brandId, String orderId) {
        return accountLogService.selectByOrderId(orderId);
    }
}
