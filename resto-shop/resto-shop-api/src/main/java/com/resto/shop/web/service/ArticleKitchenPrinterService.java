package com.resto.shop.web.service;

import com.resto.shop.web.dto.AritclekitchenPrinterDto;

import java.util.List;

public interface ArticleKitchenPrinterService {

    List<AritclekitchenPrinterDto> selectByArticleAndShopId(String shopId);
}
