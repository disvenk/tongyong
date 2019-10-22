package com.resto.shop.web.report;

import com.resto.brand.web.dto.AppraiseShopDto;
import com.resto.shop.web.dto.AppraiseNewBrandDto;
import com.resto.shop.web.dto.AppraiseNewShopDetailDto;
import com.resto.shop.web.dto.AppraiseNewShopDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppraiseMapperReport{

    List<AppraiseShopDto> selectAppraiseShopDto(Map<String, Object> selectMap);

    List<AppraiseNewBrandDto> selectAppraiseNewBrand(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("brandId") String brandId);

    List<AppraiseNewShopDto> selectAppraiseNewShop(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("shopId") String shopId);

    List<AppraiseNewShopDetailDto> selectAppraiseNewShopDetail(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("shopId") String shopId);
}
