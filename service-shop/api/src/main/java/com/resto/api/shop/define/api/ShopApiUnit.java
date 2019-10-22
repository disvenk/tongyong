package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.UnitArticleDto;
import com.resto.api.shop.dto.UnitDetailDto;
import com.resto.api.shop.dto.UnitDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "规格", position = 1)
@RequestMapping(value = ShopConstant.UNIT, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiUnit {

    @ApiOperation(value = "查询规格")
    @GetMapping("/getUnitByArticleid")
    List<UnitDto> getUnitByArticleid(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                     @ApiParam(value = "菜品id", required = true) @RequestParam("articleId") String articleId);

    @ApiOperation(value = "查询微信规格")
    @GetMapping("/getUnitByArticleidWechat")
    List<UnitDto> getUnitByArticleidWechat(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                           @ApiParam(value = "菜品id", required = true) @RequestParam("articleId") String articleId);

    @ApiOperation(value = "查询规格详情")
    @GetMapping("/selectUnitDetail")
    List<UnitArticleDto> selectUnitDetail(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                          @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId);

}
