package com.resto.service.customer.service.impl;

import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.Customer;
import com.resto.api.customer.service.NewAccountService;
import com.resto.service.customer.service.impl.service.AccountService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-18 11:03
 */

@RestController
public class NewAccountServiceImpl implements NewAccountService{

    @Resource
    private AccountService accountService;

    @Override
    public Account dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Account account) {
        accountService.dbSave(account);
        return account;
    }

    @Override
    public int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, List<Account> list) {
        return accountService.dbInsertList(list);
    }

    @Override
    public int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Account account) {
        return accountService.dbDelete(account);
    }

    @Override
    public int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Object var) {
        return accountService.dbDeleteByPrimaryKey(var.toString());
    }

    @Override
    public int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, String var) {
        return accountService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Account account) {
        return accountService.dbUpdate(account);
    }

    @Override
    public List<Account> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Account t) {
        return accountService.dbSelect(t);
    }

    @Override
    public List<Account> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Account t, Integer pageNum, Integer pageSize) {
        return accountService.dbSelectPage(t,pageNum ,pageSize );
    }

    @Override
    public Account dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Object key) {
        return accountService.dbSelectByPrimaryKey(key);
    }

    @Override
    public Account dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Account record) {
        return accountService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Account record) {
        return accountService.dbSelectCount(record);
    }

    @Override
    public List<Account> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, String ids) {
        return accountService.dbSelectByIds(ids);
    }

    @Override
    public BigDecimal useAccount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, BigDecimal payMoney, Account account, Integer source, String shopDetailId) {
        return accountService.useAccount(payMoney,account ,source ,shopDetailId );
    }

    @Override
    public void addAccount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, BigDecimal value, String accountId, String remark, Integer source, String shopDetailId) {
        accountService.addAccount(value,accountId ,remark ,source ,shopDetailId );
    }

    @Override
    public Account selectAccountAndLogByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, String customerId) {
        return null;
    }

    @Override
    public Account createCustomerAccount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, Customer cus) {
        return accountService.createCustomerAccount(cus);
    }

    @Override
    public List<Account> selectRebate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId) {
        return accountService.selectRebate();
    }

    @Override
    public Account selectAccountByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, String customerId) {
        return accountService.selectAccountByCustomerId(customerId);
    }

    @Override
    public List<Account> selectAccountByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, List<String> ids) {
        return accountService.selectAccountByIds(ids);
    }
}
