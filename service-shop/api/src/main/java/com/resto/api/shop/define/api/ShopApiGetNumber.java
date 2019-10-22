package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.GetNumberDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "用户", position = 1)
@RequestMapping(value = ShopConstant.GET_NUMBER, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiGetNumber {

    @ApiOperation(value = "根据id查询信息")
    @GetMapping("selectById")
    GetNumberDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @ApiParam(value = "id", required = true) @RequestParam("id") String id);

    @ApiOperation(value = "根据会员id和店铺id查询信息")
    @GetMapping("getWaitInfoByCustomerId")
    GetNumberDto getWaitInfoByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                         @ApiParam(value = "会员id", required = true) @RequestParam("customerId")String customerId,
                                         @ApiParam(value = "店铺id", required = true) @RequestParam("shopId")String shopId);

    @ApiOperation(value = "更新信息")
    @GetMapping("update")
    int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
               GetNumberDto getNumberDto);

}
