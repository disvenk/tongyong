package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.AccountLog;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AccountLogMapper extends BaseDao<AccountLog,String> {
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
