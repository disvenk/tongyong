package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.MqConfigMapper;
import com.resto.brand.web.model.MqConfig;
import com.resto.brand.web.service.MqConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KONATA on 2017/7/12.
 */
@Component
@Service
public class MqConfigServiceImpl  extends GenericServiceImpl<MqConfig, Long>  implements MqConfigService  {


    @Autowired
    MqConfigMapper mqConfigMapper;

    @Override
    public GenericDao<MqConfig, Long> getDao() {
        return mqConfigMapper;
    }
}
