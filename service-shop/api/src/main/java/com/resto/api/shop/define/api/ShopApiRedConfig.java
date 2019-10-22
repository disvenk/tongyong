package com.resto.api.shop.define.api;

import com.resto.api.brand.dto.RedPacketDto;
import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.RedConfigDto;
import com.resto.api.shop.dto.ShareMoneyDto;
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
@Api(description = "红包配置", position = 1)
@RequestMapping(value = ShopConstant.RED_CONFIG, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiRedConfig {

    @ApiOperation(value = "根据门店id查询")
    @GetMapping("selectListByShopId")
    List<RedConfigDto> selectListByShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                          @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId);

    @ApiOperation(value = "查询红包列表")
    @GetMapping("selectShareMoneyList")
    List<ShareMoneyDto> selectShareMoneyList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                             @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                             @ApiParam(value = "当前页", required = true) @RequestParam("currentPage") Integer currentPage,
                                             @ApiParam(value = "展示条数", required = true) @RequestParam("showCount") Integer showCount);

}
