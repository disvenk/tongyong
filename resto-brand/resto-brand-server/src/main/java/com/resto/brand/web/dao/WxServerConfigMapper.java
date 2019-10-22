package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.WxServerConfig;

import java.util.List;

/**
 * Created by KONATA on 2016/12/3.
 */
public interface WxServerConfigMapper extends GenericDao<WxServerConfig,Long> {

    List<WxServerConfig> getConfigList();

}
