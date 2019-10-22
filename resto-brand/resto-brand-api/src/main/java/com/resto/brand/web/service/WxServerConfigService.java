package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.WxServerConfig;

import java.util.List;

/**
 * Created by KONATA on 2016/12/3.
 */
public interface WxServerConfigService extends GenericService<WxServerConfig, Long> {

    List<WxServerConfig> getConfigList();
}
