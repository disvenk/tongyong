package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.WechatChargeConfig;

/**
 * Created by KONATA on 2016/12/26.
 */
public interface WechatChargeConfigMapper extends GenericDao<WechatChargeConfig, String> {

    int createWechatChargeConfig(WechatChargeConfig wechatChargeConfig);
}
