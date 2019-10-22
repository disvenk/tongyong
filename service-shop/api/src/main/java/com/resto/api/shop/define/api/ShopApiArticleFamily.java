package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.ArticleFamilyDto;
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
@Api(description = "菜品套餐", position = 1)
@RequestMapping(value = ShopConstant.ARTICLE_FAMILY, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiArticleFamily {

    @ApiOperation(value = "根据模式查询列表")
    @GetMapping("selectListByDistributionModeId")
    List<ArticleFamilyDto> selectListByDistributionModeId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                          @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                          @ApiParam(value = "模式id", required = true) @RequestParam("id") Integer id);

}
