package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.ArticleAttrDto;
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
@Api(description = "菜品属性", position = 1)
@RequestMapping(value = ShopConstant.ARTICLE_ATTR, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiArticleAttr {

    @ApiOperation(value = "根据门店id查询菜品属性")
    @GetMapping("selectListByShopId")
    List<ArticleAttrDto> selectListByShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                            @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId);

}
