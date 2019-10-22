package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Wether;


public interface WetherService extends GenericService<Wether, Integer> {
    public Wether selectDateAndShopId(String shopId, String date);
}
