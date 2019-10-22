package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.PosLoginConfig;

/**
 * Created by KONATA on 2017/6/9.
 */
public interface PosLoginMapper extends GenericDao<PosLoginConfig, Long> {

    PosLoginConfig getConfigByIp(String ip);
}
