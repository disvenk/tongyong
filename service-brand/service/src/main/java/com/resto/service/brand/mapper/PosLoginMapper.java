package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.PosLoginConfig;

/**
 * Created by KONATA on 2017/6/9.
 */
public interface PosLoginMapper extends BaseDao<PosLoginConfig, Long> {

    PosLoginConfig getConfigByIp(String ip);
}
