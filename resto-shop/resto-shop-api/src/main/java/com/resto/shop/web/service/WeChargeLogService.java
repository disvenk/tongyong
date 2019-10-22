package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WeChargeLog;

import java.util.List;

public interface WeChargeLogService extends GenericService<WeChargeLog, Long> {

    List<WeChargeLog> selectByShopIdAndTime(String id, String zuoriDay);

    void deleteByIds(List<Long> ids);
}
