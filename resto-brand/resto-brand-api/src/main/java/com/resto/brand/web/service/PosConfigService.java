package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.PosConfig;

/**
 * Created by KONATA on 2017/5/27.
 */
public interface PosConfigService extends GenericService<PosConfig, Long> {

    PosConfig getConfigByClientIp(String clientIp);
}
