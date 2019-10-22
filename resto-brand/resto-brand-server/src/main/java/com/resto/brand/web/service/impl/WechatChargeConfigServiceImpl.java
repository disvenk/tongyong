package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dao.WechatChargeConfigMapper;
import com.resto.brand.web.model.WechatChargeConfig;
import com.resto.brand.web.service.WechatChargeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KONATA on 2016/12/26.
 * 微信充值账户实现类
 */
@Component
@Service
public class WechatChargeConfigServiceImpl extends GenericServiceImpl<WechatChargeConfig, String> implements WechatChargeConfigService {

    @Autowired
    private WechatChargeConfigMapper wechatChargeConfigMapper;


    @Override
    public GenericDao<WechatChargeConfig, String> getDao() {
        return wechatChargeConfigMapper;
    }

    @Override
    public String createWechatChargeConfig(WechatChargeConfig wechatChargeConfig) {
        wechatChargeConfig.setId(ApplicationUtils.randomUUID());
        wechatChargeConfigMapper.createWechatChargeConfig(wechatChargeConfig);
        return wechatChargeConfig.getId();
    }
}
