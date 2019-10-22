package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ShopTvConfig;

/**
 * Created by carl on 2017/7/14.
 */
public interface ShopTvConfigService extends GenericService<ShopTvConfig, Long> {

    ShopTvConfig selectByShopId(String shopId);

}
