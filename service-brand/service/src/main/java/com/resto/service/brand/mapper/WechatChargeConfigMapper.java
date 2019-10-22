package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.WechatChargeConfig;

/**
 * Created by KONATA on 2016/12/26.
 */
public interface WechatChargeConfigMapper extends BaseDao<WechatChargeConfig, String> {

    int createWechatChargeConfig(WechatChargeConfig wechatChargeConfig);
}
