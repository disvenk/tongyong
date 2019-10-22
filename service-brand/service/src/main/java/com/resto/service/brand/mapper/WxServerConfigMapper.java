package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.WxServerConfig;

import java.util.List;

/**
 * Created by KONATA on 2016/12/3.
 */
public interface WxServerConfigMapper extends BaseDao<WxServerConfig,Long> {

    List<WxServerConfig> getConfigList();

}
