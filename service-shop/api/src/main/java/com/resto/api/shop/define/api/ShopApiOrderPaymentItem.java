package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.OrderPaymentItemDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "订单支付详情", position = 1)
@RequestMapping(value = ShopConstant.ORDER_PAYMENT_ITEM, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiOrderPaymentItem {

    @ApiOperation(value = "跟据orderId查询")
    @GetMapping("/selectByOrderId")
    List<OrderPaymentItemDto> selectByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                              @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "跟据注主键d查询")
    @GetMapping("/selectById")
    OrderPaymentItemDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "保存信息")
    @PostMapping("/insertByBeforePay")
    void insertByBeforePay(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                           @RequestBody OrderPaymentItemDto orderPaymentItemDto);

}
