package com.resto.service.shop.controller;

import com.resto.api.brand.dto.CouponDto;
import com.resto.api.shop.define.api.ShopApiCoupon;
import com.resto.api.shop.dto.CustomerDto;
import com.resto.api.shop.dto.NewCustomCouponDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.Coupon;
import com.resto.service.shop.entity.Customer;
import com.resto.service.shop.entity.NewCustomCoupon;
import com.resto.service.shop.service.CouponService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CouponController implements ShopApiCoupon {

    @Autowired
    private CouponService couponService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<CouponDto> addRealTimeCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                             @RequestBody CustomerDto customerDto) {
        List<NewCustomCoupon> customCoupons = mapper.mapAsList(customerDto.getNewCustomCoupons(), NewCustomCoupon.class);
        Customer customer = mapper.map(customerDto, Customer.class);
        return mapper.mapAsList(couponService.addRealTimeCoupon(customCoupons, customer), CouponDto.class);
    }

    @Override
    public List<CouponDto> usedCouponBeforeByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                     @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        return mapper.mapAsList(couponService.usedCouponBeforeByOrderId(orderId), CouponDto.class);
    }

    @Override
    public CouponDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                @ApiParam(value = "优惠券id", required = true) @RequestParam("id") String id) {
        return mapper.map(couponService.selectById(id), CouponDto.class);
    }

    @Override
    public List<CouponDto> listCouponUsed(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                          @RequestBody CouponDto couponDto) {
        Coupon coupon = mapper.map(couponDto, Coupon.class);
        return mapper.mapAsList(couponService.listCouponUsed(coupon), CouponDto.class);
    }

    @Override
    public List<CouponDto> listCoupon(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                      @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                      @RequestBody CouponDto couponDto) {
        Coupon coupon = mapper.map(couponDto, Coupon.class);
        return mapper.mapAsList(couponService.listCoupon(coupon, brandId, shopId), CouponDto.class);
    }

    @Override
    public List<CouponDto> listCouponByStatus(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                              @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                              @ApiParam(value = "状态", required = true) @RequestParam("brandId") String status,
                                              @ApiParam(value = "用户id", required = true) @RequestParam("shopId") String customerId) {
        return mapper.mapAsList(couponService.listCouponByStatus(status, customerId, brandId, shopId), CouponDto.class);
    }

    @Override
    public void useCouponById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                              @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                              @ApiParam(value = "优惠券id", required = true) @RequestParam("couponId") String couponId) {
        couponService.useCouponById(orderId, couponId);
    }

}
