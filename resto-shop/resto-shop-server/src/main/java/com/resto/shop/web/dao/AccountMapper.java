package com.resto.shop.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.Account;

public interface AccountMapper  extends GenericDao<Account,String> {
    int deleteByPrimaryKey(String id);

    int insert(Account record);

    int insertSelective(Account record);

    Account selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);
    
    Account selectAccountByCustomerId(@Param("customerId") String customerId);

    List<Account> selectRebate();

    List<Account> selectAccountByIds(List<String> Ids);
}
