package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.OrderItemDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "订单详情", position = 1)
@RequestMapping(value = ShopConstant.ORDER_ITEM, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiOrderItem {

    @ApiOperation(value = "根据订单详情id查找信息")
    @GetMapping("selectById/{id}")
    OrderItemDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @ApiParam(value = "订单详情id", required = true) @PathVariable("id") String id);

    @ApiOperation(value = "根据id查找订单列表")
    @GetMapping("listByOrderId/{id}")
    List<OrderItemDto> listByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                     @ApiParam(value = "订单id", required = true) @PathVariable("id") String id);

}
