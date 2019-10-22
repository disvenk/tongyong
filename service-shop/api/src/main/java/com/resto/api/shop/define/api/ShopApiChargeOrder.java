package com.resto.api.shop.define.api;

import com.resto.api.brand.util.JSONResult;
import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.ChargeOrderDto;
import com.resto.api.shop.dto.ChargePaymentDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "充值订单", position = 1)
@RequestMapping(value = ShopConstant.CHARGE_ORDER, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiChargeOrder {

    @ApiOperation(value = "根据用户id跟品牌id查询")
    @GetMapping("selectByCustomerIdAndBrandId")
    List<ChargeOrderDto> selectByCustomerIdAndBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                      @ApiParam(value = "用户id", required = true) @RequestParam("id") String id);

    @ApiOperation(value = "创建充值订单")
    @GetMapping("createChargeOrder")
    ChargeOrderDto createChargeOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                     @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                     @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                     @ApiParam(value = "充值id", required = true) @RequestParam("settingId") String settingId);

    @ApiOperation(value = "保存充值订单")
    @PostMapping("insert")
    int insert(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
               @RequestBody ChargeOrderDto chargeOrderDto);

    @ApiOperation(value = "微信充值订单")
    @PostMapping("chargeorderWxPaySuccess")
    void chargeorderWxPaySuccess(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                 @RequestBody ChargePaymentDto chargePaymentDto);

    @ApiOperation(value = "查询用户")
    @GetMapping("selectByCustomerIdNotChangeId")
    BigDecimal selectByCustomerIdNotChangeId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                             @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "提现")
    @GetMapping("withdrawals")
    JSONResult withdrawals(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                           @ApiParam(value = "金额", required = true) @RequestParam("money") BigDecimal money,
                           @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);
}
