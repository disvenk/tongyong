package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.PosConfig;

/**
 * Created by KONATA on 2017/5/27.
 */
public interface PosConfigMapper extends BaseDao<PosConfig,Long> {

    PosConfig getConfigByClientIp(String clientIp);
}
