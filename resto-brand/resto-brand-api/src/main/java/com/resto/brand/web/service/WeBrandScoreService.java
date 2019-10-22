package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.WeBrandScore;

import java.util.Date;

public interface WeBrandScoreService extends GenericService<WeBrandScore, Integer> {
    WeBrandScore   selectByBrandIdAndCreateTime(String brandId, Date createTime);
}
