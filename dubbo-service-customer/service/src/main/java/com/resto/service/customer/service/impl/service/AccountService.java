package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.constants.AccountLogType;
import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.AccountLog;
import com.resto.api.customer.entity.ChargeOrder;
import com.resto.api.customer.entity.Customer;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.model.*;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.customer.mapper.AccountMapper;
import com.resto.service.customer.mapper.ChargeOrderMapper;
import com.resto.service.customer.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 *
 */
@Service
public class AccountService extends BaseServiceResto<Account, AccountMapper> {

	public Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AccountMapper accountMapper;

    @Resource
	AccountLogService accountLogService;

    @Resource
	private CustomerService customerService;


	public BigDecimal useAccount(BigDecimal payMoney, Account account,Integer source,String shopDetailId) {
    	if(account.getRemain().doubleValue() <= 0 || payMoney.doubleValue() <= 0){
    		throw new RuntimeException("金额为0，异常");
		}
//
//		if(account.getRemain().doubleValue() < payMoney.doubleValue()){
//    		//如果账户余额比 要支付的金额小的话
//			throw new RuntimeException("金额异常，要支付的金额 比 实际金额大");
//		}


//		if(account.getRemain().equals(BigDecimal.ZERO)||payMoney.equals(BigDecimal.ZERO)){
//			return BigDecimal.ZERO;
//		}
		//如果 需要支付的金额大于余额，则扣除所有余额
		BigDecimal useAccountValue = BigDecimal.ZERO;
		if(payMoney.compareTo(account.getRemain())>=0){
			useAccountValue=account.getRemain();
		}else{  //如果 需要支付的金额 小于 余额
			useAccountValue = payMoney;
		}
		account.setRemain(account.getRemain().subtract(useAccountValue));
		String remark= "使用余额:"+useAccountValue+"元";
		addLog(useAccountValue, account, remark, AccountLogType.PAY,source,shopDetailId);
		dbUpdate(account);
		return useAccountValue;
	}

	public void addAccount(BigDecimal value, String accountId, String remark,Integer source,String shopDetailId) {
		Account account = dbSelectByPrimaryKey(accountId);
		account.setRemain(account.getRemain().add(value));
		if(value.doubleValue() > 0){
			addLog(value, account, remark, AccountLogType.INCOME,source,shopDetailId);
		}else{
			addLog(new BigDecimal(-1).multiply(value), account, remark, AccountLogType.PAY,source,shopDetailId);
		}

		dbUpdate(account);
	}

	private void addLog(BigDecimal money,Account account,String remark,int type,int source,String shopDetailId){
		AccountLog acclog = new AccountLog();
		acclog.setCreateTime(new Date());
		acclog.setId(ApplicationUtils.randomUUID());
		acclog.setMoney(money);
		acclog.setRemain(account.getRemain());
		acclog.setPaymentType(type);
		acclog.setRemark(remark);
		acclog.setAccountId(account.getId());
		acclog.setSource(source);
        acclog.setShopDetailId(shopDetailId);
		accountLogService.dbSave(acclog);
	}

	public Account selectAccountAndLogByCustomerId(String customerId) {
		Account account = accountMapper.selectAccountByCustomerId(customerId);
		if(account!=null){
			List<AccountLog> accountLogs = accountLogService.selectLogsByAccountId(account.getId());
			account.setAccountLogs(accountLogs);
		}
		return account;
	}

	public Account createCustomerAccount(Customer cus) {
		Account acc =new Account();
		acc.setId(ApplicationUtils.randomUUID());
		acc.setRemain(BigDecimal.ZERO);
		dbSave(acc);
		cus.setAccountId(acc.getId());
		customerService.dbUpdate(cus);
		return acc;
	}


	public List<Account> selectRebate() {
		return accountMapper.selectRebate();
	}

	public Account selectAccountByCustomerId(String customerId) {
		return accountMapper.selectAccountByCustomerId(customerId);
	}

	public List<Account> selectAccountByIds(List<String> ids) {
		return accountMapper.selectAccountByIds(ids);
	}
}
