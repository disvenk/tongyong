package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.AccountDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "账户", position = 1)
@RequestMapping(value = ShopConstant.ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiAccount {

    @ApiOperation(value = "根据id获取账户信息")
    @GetMapping("selectById")
    AccountDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                          @ApiParam(value = "账户id", required = true) @RequestParam("accountId") String accountId);

    @ApiOperation(value = "根据用户id获取账户及日志信息")
    @GetMapping("selectAccountAndLogByCustomerId")
    AccountDto selectAccountAndLogByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                               @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

}
