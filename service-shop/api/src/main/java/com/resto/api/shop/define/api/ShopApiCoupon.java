package com.resto.api.shop.define.api;

import com.resto.api.brand.dto.CouponDto;
import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.CustomerDto;
import com.resto.api.shop.dto.NewCustomCouponDto;
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
@Api(description = "优惠券", position = 1)
@RequestMapping(value = ShopConstant.COUPON, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiCoupon {

    @ApiOperation(value = "添加有效优惠券")
    @GetMapping("/addRealTimeCoupon")
    List<CouponDto> addRealTimeCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                      @RequestBody CustomerDto customerDto);

    @ApiOperation(value = "使用优惠券")
    @GetMapping("/usedCouponBeforeByOrderId")
    List<CouponDto> usedCouponBeforeByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                              @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "根据id查询优惠券")
    @GetMapping("/selectById")
    CouponDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                         @ApiParam(value = "优惠券id", required = true) @RequestParam("id") String id);

    @ApiOperation(value = "查询有效优惠券列表")
    @PostMapping("/listCouponUsed")
    List<CouponDto> listCouponUsed(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   @RequestBody CouponDto couponDto);

    @ApiOperation(value = "查询优惠券列表")
    @PostMapping("/listCoupon")
    List<CouponDto> listCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                               @RequestBody CouponDto couponDto);

    @ApiOperation(value = "通过状态查询优惠券列表")
    @PostMapping("/listCouponByStatus")
    List<CouponDto> listCouponByStatus(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                       @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                       @ApiParam(value = "状态", required = true) @RequestParam("brandId") String status,
                                       @ApiParam(value = "用户id", required = true) @RequestParam("shopId") String customerId);

    @ApiOperation(value = "使用优惠券")
    @GetMapping("/useCouponById")
    void useCouponById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                       @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                       @ApiParam(value = "优惠券id", required = true) @RequestParam("couponId") String couponId);

}
