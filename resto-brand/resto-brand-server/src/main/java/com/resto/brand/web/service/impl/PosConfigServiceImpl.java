package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.PosConfigMapper;
import com.resto.brand.web.model.PosConfig;
import com.resto.brand.web.service.PosConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KONATA on 2017/5/27.
 */
@Component
@Service
public class PosConfigServiceImpl extends GenericServiceImpl<PosConfig, Long> implements PosConfigService {

    @Autowired
    private PosConfigMapper posConfigMapper;

    @Override
    public GenericDao<PosConfig, Long> getDao() {
        return posConfigMapper;
    }


    @Override
    public PosConfig getConfigByClientIp(String clientIp) {
        return posConfigMapper.getConfigByClientIp(clientIp);
    }
}
