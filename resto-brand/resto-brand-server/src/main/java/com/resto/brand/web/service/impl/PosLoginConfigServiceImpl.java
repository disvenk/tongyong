package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.PosLoginMapper;
import com.resto.brand.web.model.PosLoginConfig;
import com.resto.brand.web.service.PosLoginConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KONATA on 2017/6/9.
 */
@Component
@Service
public class PosLoginConfigServiceImpl extends GenericServiceImpl<PosLoginConfig, Long> implements PosLoginConfigService {

    @Autowired
    private PosLoginMapper posLoginMapper;

    @Override
    public GenericDao<PosLoginConfig, Long> getDao() {
        return posLoginMapper;
    }

    @Override
    public PosLoginConfig getConfigByIp(String ip) {
        return posLoginMapper.getConfigByIp(ip);
    }
}
