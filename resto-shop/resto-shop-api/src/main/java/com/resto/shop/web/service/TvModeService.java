package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.TvMode;

import java.util.List;

public interface TvModeService extends GenericService<TvMode, Integer> {

    TvMode selectByDeviceShopIdSource(String shopId, String deviceToken, Integer appSource);

    List<TvMode> selectByShopIdSource(String shopId, Integer appSource);
}
