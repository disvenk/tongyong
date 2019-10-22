package com.resto.shop.web.service;


import com.resto.shop.web.dto.ArticleBomDo;
import com.resto.shop.web.dto.ArticleSellCountDto;

import java.util.List;
import java.util.Map;

public interface ArticleSaleService {


    public static final String materialTotalCount = "materialTotalCount";

    public static final String materialMealFeeNumber = "materialMealFeeNumber";


    Map<Long, List<ArticleBomDo>>  countMaterialStockSellGroupByMaterialId(String shopId, String lastCountTime, List<ArticleSellCountDto> articleSellCountDtos);





}
