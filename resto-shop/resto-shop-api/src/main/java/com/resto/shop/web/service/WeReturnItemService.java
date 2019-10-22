package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WeReturnItem;

import java.util.List;

public interface WeReturnItemService extends GenericService<WeReturnItem, Long> {

    List<WeReturnItem> selectByShopIdAndTime(String zuoriDay, String id);

    void deleteByIds(List<Long> ids);
}
