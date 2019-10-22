package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WeItem;

import java.util.List;

public interface WeItemService extends GenericService<WeItem, Long> {

    List<WeItem> selectByShopIdAndTime(String zuoriDay, String id);

    void deleteByIds(List<Long> ids);
}
