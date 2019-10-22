package com.resto.shop.web.service;

import com.resto.shop.web.dto.kitchenPrinterDto;

import java.util.List;

public interface KitchenPrinterService {

    List<kitchenPrinterDto> selectKitchenAndPriterByShopID(String shopId);
}
