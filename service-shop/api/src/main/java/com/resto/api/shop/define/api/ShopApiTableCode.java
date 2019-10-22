package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.TableCodeDto;
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

/**
 * Created by xielc on 2018/1/5.
 */
@FeignClient(value = ShopConstant.API_NAME, fallbackFactory = ShopApiHystrixFallbackFactory.class)
@Api(description = "桌位", position = 1)
@RequestMapping(value = ShopConstant.TABLECODE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiTableCode {

    @ApiOperation(value = "根据桌位id查询桌位信息")
    @GetMapping("/selectById")
    TableCodeDto selectById(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId,
                            @ApiParam(value = "桌位id", required = true) @RequestParam("id")String id);
}
