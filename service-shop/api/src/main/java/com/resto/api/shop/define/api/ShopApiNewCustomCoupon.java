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
import java.util.Map;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "新优惠券", position = 1)
@RequestMapping(value = ShopConstant.NEW_COUPON, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiNewCustomCoupon {

    @ApiOperation(value = "查询有效优惠券")
    @GetMapping("/selectRealTimeCoupon")
    List<NewCustomCouponDto> selectRealTimeCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                  @RequestBody Map<String, Object> map);

    @ApiOperation(value = "查询生日优惠券")
    @GetMapping("/selectBirthCoupon")
    List<NewCustomCouponDto> selectBirthCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId);

    @ApiOperation(value = "查询新优惠券")
    @GetMapping("/selectListByBrandId")
    List<NewCustomCouponDto> selectListByBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                 @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId);

    @ApiOperation(value = "通过类型查询新优惠券")
    @GetMapping("/selectListByCouponType")
    List<NewCustomCouponDto> selectListByCouponType(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                    @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                    @ApiParam(value = "品牌id", required = true) @RequestParam("couponType") Integer couponType);
    @ApiOperation(value = "gift优惠券")
    @PostMapping("/giftCoupon")
    List<CouponDto> giftCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                               @ApiParam(value = "优惠券类型", required = true) @RequestParam("couponType") Integer couponType,
                               @RequestBody CustomerDto customerDto);
}
