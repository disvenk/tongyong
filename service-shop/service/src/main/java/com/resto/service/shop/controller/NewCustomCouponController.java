package com.resto.service.shop.controller;

import com.resto.api.brand.dto.CouponDto;
import com.resto.api.shop.define.api.ShopApiNewCustomCoupon;
import com.resto.api.shop.dto.CustomerDto;
import com.resto.api.shop.dto.NewCustomCouponDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.Customer;
import com.resto.service.shop.service.NewCustomCouponService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class NewCustomCouponController implements ShopApiNewCustomCoupon {

    @Autowired
    private NewCustomCouponService newCustomCouponService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<NewCustomCouponDto> selectRealTimeCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                         @RequestBody Map<String, Object> map) {
        return mapper.mapAsList(newCustomCouponService.selectRealTimeCoupon(map), NewCustomCouponDto.class);
    }

    @Override
    public List<NewCustomCouponDto> selectBirthCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId) {
        return mapper.mapAsList(newCustomCouponService.selectBirthCoupon(), NewCustomCouponDto.class);
    }

    @Override
    public List<NewCustomCouponDto> selectListByBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                        @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId) {
        return mapper.mapAsList(newCustomCouponService.selectListByBrandId(brandId, shopId), NewCustomCouponDto.class);
    }

    @Override
    public List<NewCustomCouponDto> selectListByCouponType(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                           @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                           @ApiParam(value = "品牌id", required = true) @RequestParam("couponType") Integer couponType) {
        return mapper.mapAsList(newCustomCouponService.selectListByCouponType(brandId, couponType, shopId), NewCustomCouponDto.class);
    }

    @Override
    public List<CouponDto> giftCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                      @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                      @ApiParam(value = "优惠券类型", required = true) @RequestParam("couponType") Integer couponType,
                                      @RequestBody CustomerDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        return mapper.mapAsList(newCustomCouponService.giftCoupon(customer, couponType, shopId), CouponDto.class);
    }

}
