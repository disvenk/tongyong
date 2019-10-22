package com.resto.service.customer.mapper;

import com.resto.api.customer.entity.AccountLog;
import com.resto.conf.mybatis.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AccountLogMapper extends MyMapper<AccountLog> {

    List<AccountLog> selectLogsByAccountId(@Param("accountId") String accountId);
    List<AccountLog> selectByCustomerIdNumber(String id);
    List<String> selectBrandMarketing(Map<String, String> selectMap);
    BigDecimal selectByShareMoney(String accountId);
    Integer selectByShareMoneyCount(String accountId);
}