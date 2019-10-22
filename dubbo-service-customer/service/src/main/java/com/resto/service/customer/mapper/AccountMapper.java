package com.resto.service.customer.mapper;

import com.resto.api.customer.entity.Account;
import com.resto.conf.mybatis.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper extends MyMapper<Account> {
    Account selectAccountByCustomerId(@Param("customerId") String customerId);
    List<Account> selectRebate();
    List<Account> selectAccountByIds(List<String> Ids);

}