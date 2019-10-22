package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WeShopScore;

import java.util.Date;

public interface WeShopScoreService extends GenericService<WeShopScore, Integer> {

    WeShopScore selectByShopIdAndDate(String id, Date beforeYesterDay);
}
