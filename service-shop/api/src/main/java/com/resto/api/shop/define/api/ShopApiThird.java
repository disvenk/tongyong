package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "第三方", position = 1)
@RequestMapping(value = ShopConstant.THIRD, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiThird {

    @ApiOperation(value = "查询规格")
    @PostMapping("/orderAccept")
    Boolean orderAccept(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                        @RequestBody Map map);

}
