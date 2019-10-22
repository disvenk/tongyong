package com.resto.brand.web.dao;

import com.resto.brand.web.model.AccountTicket;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountTicketMapper  extends GenericDao<AccountTicket,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(AccountTicket record);

    int insertSelective(AccountTicket record);

    AccountTicket selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountTicket record);

    int updateByPrimaryKey(AccountTicket record);

	List<AccountTicket> selectListByBrandId(@Param("brandId") String brandId);
}
