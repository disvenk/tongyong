package com.resto.brand.web.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ElectronicTicketConfigMapper;
import com.resto.brand.web.model.ElectronicTicketConfig;
import com.resto.brand.web.service.ElectronicTicketConfigService;
import org.springframework.stereotype.Component;

/**
 *
 */
@Service
@Component
public class ElectronicTicketConfigServiceImpl extends GenericServiceImpl<ElectronicTicketConfig, Long> implements ElectronicTicketConfigService {

    @Resource
    private ElectronicTicketConfigMapper electronicticketconfigMapper;

    @Override
    public GenericDao<ElectronicTicketConfig, Long> getDao() {
        return electronicticketconfigMapper;
    }

    @Override
    public ElectronicTicketConfig selectByBrandId(String brandId) {
        return electronicticketconfigMapper.selectByBrandId(brandId);
    }
}
