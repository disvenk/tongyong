package com.resto.service.brand.controller;


import com.resto.api.brand.define.api.BrandApiShopDetail;
import com.resto.api.brand.dto.ShopDetailDto;
import com.resto.core.common.OrikaBeanMapper;
import com.resto.service.brand.service.ShopDetailService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShopDetailController implements BrandApiShopDetail {

    @Autowired
    private ShopDetailService shopDetailService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public ShopDetailDto selectByPrimaryKey(@ApiParam(value = "店铺id", required = true) @PathVariable("id") String id) {
        return mapper.map(shopDetailService.selectByPrimaryKey(id),ShopDetailDto.class);
    }

    @Override
    public List<ShopDetailDto> getShopDetailsListParams(@RequestParam(value = "shopDetailIds") List<String> shopDetailIds) {
        return mapper.mapAsList(shopDetailService.getShopDetailsListParams(shopDetailIds),ShopDetailDto.class);
    }

    @Override
    public ShopDetailDto selectByRestaurantId(@ApiParam(value = "饿了吗店铺id", required = true) @PathVariable("restaurantId") Integer restaurantId) {
        return mapper.map(shopDetailService.selectByRestaurantId(restaurantId),ShopDetailDto.class);
    }

    @Override
    public List<ShopDetailDto> selectByBrandId(@ApiParam(value = "品牌id", required = true) @PathVariable("id") String id) {
        return mapper.mapAsList(shopDetailService.selectByBrandId(id),ShopDetailDto.class);
    }

    @Override
    public List<ShopDetailDto> selectByShopCity(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
        return mapper.mapAsList(shopDetailService.selectByShopCity(brandId),ShopDetailDto.class);
    }

    @Override
    public List<ShopDetailDto> selectByCityOrName(@ApiParam(value = "城市名称", required = true) @PathVariable("name") String name, @ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
        return mapper.mapAsList(shopDetailService.selectByCityOrName(name,brandId),ShopDetailDto.class);
    }

    @Override
    public List<ShopDetailDto> selectByCity(@ApiParam(value = "城市", required = true) @PathVariable("city") String city, @ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
        return mapper.mapAsList(shopDetailService.selectByCity(city,brandId),ShopDetailDto.class);
    }

    @Override
    public List<ShopDetailDto> selectOrderByIndex(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
        return mapper.mapAsList(shopDetailService.selectOrderByIndex(brandId),ShopDetailDto.class);
    }

}
