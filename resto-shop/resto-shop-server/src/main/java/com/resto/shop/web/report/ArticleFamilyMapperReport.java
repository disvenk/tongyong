package com.resto.shop.web.report;

import com.resto.brand.web.dto.ArticleSellDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleFamilyMapperReport{

    List<ArticleSellDto> selectByShopId(@Param("shopId") String shopId);

    List<ArticleSellDto> selectByShopIdNew(@Param("shopId") String shopId);
}
