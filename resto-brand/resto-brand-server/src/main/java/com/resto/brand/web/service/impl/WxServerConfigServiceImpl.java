package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.WxServerConfigMapper;
import com.resto.brand.web.model.WxServerConfig;
import com.resto.brand.web.service.WxServerConfigService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by KONATA on 2016/12/3.
 */
@Component
@Service
public class WxServerConfigServiceImpl extends GenericServiceImpl<WxServerConfig, Long> implements WxServerConfigService {


    @Resource
    private WxServerConfigMapper wxServerConfigMapper;

    @Override
    public GenericDao<WxServerConfig, Long> getDao() {
        return wxServerConfigMapper;
    }

    @Override
    public List<WxServerConfig> getConfigList() {
        return wxServerConfigMapper.getConfigList();
    }
}
