package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.ContractTicket;

public interface ContractTicketMapper  extends BaseDao<ContractTicket,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(ContractTicket record);

    int insertSelective(ContractTicket record);

    ContractTicket selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ContractTicket record);

    int updateByPrimaryKey(ContractTicket record);
}
