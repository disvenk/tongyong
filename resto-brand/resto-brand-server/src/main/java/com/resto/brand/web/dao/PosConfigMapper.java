package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.PosConfig;

/**
 * Created by KONATA on 2017/5/27.
 */
public interface PosConfigMapper extends GenericDao<PosConfig,Long> {

    PosConfig getConfigByClientIp(String clientIp);
}
