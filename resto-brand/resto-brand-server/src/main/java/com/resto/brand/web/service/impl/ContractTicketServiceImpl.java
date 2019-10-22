package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ContractTicketMapper;
import com.resto.brand.web.model.ContractTicket;
import com.resto.brand.web.service.ContractTicketService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 *
 */
@Component
@Service
public class ContractTicketServiceImpl extends GenericServiceImpl<ContractTicket, Long> implements ContractTicketService {

    @Resource
    private ContractTicketMapper contractticketMapper;

    @Override
    public GenericDao<ContractTicket, Long> getDao() {
        return contractticketMapper;
    }

}
