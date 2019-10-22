package com.resto.shop.web.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.AccountLog;

public interface AccountLogMapper  extends GenericDao<AccountLog,String> {
    int deleteByPrimaryKey(String id);

    int insert(AccountLog record);

    int insertSelective(AccountLog record);

    AccountLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountLog record);

    int updateByPrimaryKey(AccountLog record);
    
    /**
	 * 根据 账户ID 查询 账户交易明细
	 * @param accountId
	 * @return
	 */
    List<AccountLog> selectLogsByAccountId(@Param("accountId") String accountId);
    
    List<AccountLog> selectByCustomerIdNumber(String id);
    
    List<String> selectBrandMarketing(Map<String, String> selectMap);

    BigDecimal selectByShareMoney(String accountId);

    Integer selectByShareMoneyCount(String accountId);

    AccountLog selectByOrderId(String orderId);
}
