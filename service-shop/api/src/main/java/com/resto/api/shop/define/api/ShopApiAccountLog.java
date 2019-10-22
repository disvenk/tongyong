package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "账户日志", position = 1)
@RequestMapping(value = ShopConstant.ACCOUNT_LOG, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiAccountLog {

    @ApiOperation(value = "根据id获取分享金额")
    @GetMapping("selectByShareMoney")
    BigDecimal selectByShareMoney(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @ApiParam(value = "账户id", required = true) @RequestParam("accountId") String accountId);

    @ApiOperation(value = "根据id获取分享金额数量")
    @GetMapping("selectByShareMoneyCount")
    Integer selectByShareMoneyCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                       @ApiParam(value = "账户id", required = true) @RequestParam("accountId") String accountId);

}
