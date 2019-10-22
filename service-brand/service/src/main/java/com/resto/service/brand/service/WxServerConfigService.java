package com.resto.service.brand.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.WxServerConfig;
import com.resto.service.brand.mapper.WxServerConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by KONATA on 2016/12/3.
 */
@Service
public class WxServerConfigService extends BaseService<WxServerConfig, Long> {

    @Autowired
    private WxServerConfigMapper wxServerConfigMapper;

    public BaseDao<WxServerConfig, Long> getDao() {
        return wxServerConfigMapper;
    }

}
