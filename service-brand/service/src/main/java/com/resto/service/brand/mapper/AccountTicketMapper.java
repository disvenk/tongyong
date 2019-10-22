package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.AccountTicket;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountTicketMapper  extends BaseDao<AccountTicket,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(AccountTicket record);

    int insertSelective(AccountTicket record);

    AccountTicket selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountTicket record);

    int updateByPrimaryKey(AccountTicket record);

	List<AccountTicket> selectListByBrandId(@Param("brandId") String brandId);
}
