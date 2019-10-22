package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.PosLoginConfig;

/**
 * Created by KONATA on 2017/6/9.
 */
public interface PosLoginConfigService extends GenericService<PosLoginConfig, Long> {

    PosLoginConfig getConfigByIp(String ip);
}
