package com.resto.shop.web.report;

import com.resto.brand.web.dto.ArticleSellDto;

import java.util.List;
import java.util.Map;

public interface MealAttrMapperReport{

    List<ArticleSellDto> queryArticleMealAttr(Map<String, Object> selectMap);

}
