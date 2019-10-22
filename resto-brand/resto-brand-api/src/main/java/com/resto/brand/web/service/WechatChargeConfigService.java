package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.WechatChargeConfig;

/**
 * Created by KONATA on 2016/12/26.
 * 微信充值账户
 */
public interface WechatChargeConfigService extends GenericService<WechatChargeConfig, String> {
    String createWechatChargeConfig(WechatChargeConfig wechatChargeConfig);
}
