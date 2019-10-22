package com.resto.service.brand.service;

import com.resto.conf.util.ApplicationUtils;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.WechatChargeConfig;
import com.resto.service.brand.mapper.WechatChargeConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by KONATA on 2016/12/26.
 * 微信充值账户实现类
 */
@Service
public class WechatChargeConfigService extends BaseService<WechatChargeConfig, String> {

    @Autowired
    private WechatChargeConfigMapper wechatChargeConfigMapper;

    public BaseDao<WechatChargeConfig, String> getDao() {
        return wechatChargeConfigMapper;
    }

}
