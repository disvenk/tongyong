package com.resto.shop.web.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.AccountLog;

public interface AccountLogService extends GenericService<AccountLog, String> {
	/**
	 * 根据 账户ID 查询 账户交易明细
	 * @param accountId
	 * @return
	 */
    List<AccountLog> selectLogsByAccountId(String accountId);
    
	/**
	 * 根据 账户ID 查询 账户充值的次数
	 * @param id
	 * @return
     */
	int selectByCustomerIdNumber(String id);
	
	List<String> selectBrandMarketing(Map<String, String> selectMap);

	/**
	 * 根据 账户ID 查询 分享返利总额
	 * @param accountId
	 * @return
     */
	BigDecimal selectByShareMoney(String accountId);

	/**
	 * 根据 账户ID 查询 分享返利总数
	 * @param accountId
	 * @return
	 */
	Integer selectByShareMoneyCount(String accountId);

	AccountLog selectByOrderId(String orderId);
}
