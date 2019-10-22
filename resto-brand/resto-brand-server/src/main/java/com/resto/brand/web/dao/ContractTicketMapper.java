package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.ContractTicket;

public interface ContractTicketMapper extends GenericDao< ContractTicket,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(ContractTicket record);

    int insertSelective(ContractTicket record);

    ContractTicket selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ContractTicket record);

    int updateByPrimaryKey(ContractTicket record);
}