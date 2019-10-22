package com.resto.api.brand.define.api;

import com.resto.api.brand.define.hystrix.BrandApiHystrixFallbackFactory;
import com.resto.api.brand.dto.ShopDetailDto;
import com.resto.api.brand.util.BrandConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by xielc on 2017/12/29.
 */
@FeignClient(value = BrandConstant.BASE_NAME,fallbackFactory = BrandApiHystrixFallbackFactory.class)
@Api(description = "店铺", position = 2)
@RequestMapping(value = BrandConstant.SHOPDETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
public interface BrandApiShopDetail {

    @ApiOperation(value = "根据店铺id查询店铺信息")
    @GetMapping("/selectByPrimaryKey/{id}")
    ShopDetailDto selectByPrimaryKey(@ApiParam(value = "店铺id", required = true) @PathVariable("id")String id);

    @ApiOperation(value = "根据多个店铺id查询店铺信息")
    @PostMapping("/getShopDetailsListParams")
    List<ShopDetailDto> getShopDetailsListParams(@ApiParam(value = "多个店铺id", required = true) @RequestParam(value = "shopDetailIds") List<String> shopDetailIds);

    @ApiOperation(value = "根据饿了吗店铺id查询店铺信息")
    @GetMapping("/selectByRestaurantId/{restaurantId}")
    ShopDetailDto selectByRestaurantId(@ApiParam(value = "饿了吗店铺id", required = true) @PathVariable("restaurantId") Integer restaurantId);

    @ApiOperation(value = "根据品牌id,查询品牌下的所有店铺")
    @GetMapping("/selectByBrandId/{id}")
    List<ShopDetailDto> selectByBrandId(@ApiParam(value = "品牌id", required = true) @PathVariable("id") String id);

    @ApiOperation(value = "根据品牌id,查询品牌所在城市")
    @GetMapping("/selectByShopCity/{brandId}")
    List<ShopDetailDto> selectByShopCity(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId")String brandId);

    @ApiOperation(value = "根据城市名name、品牌id,查询信息")
    @GetMapping("/selectByCityOrName/{name}/{brandId}")
    List<ShopDetailDto> selectByCityOrName(@ApiParam(value = "城市名称", required = true) @PathVariable("name")String name, @ApiParam(value = "品牌id", required = true) @PathVariable("brandId")String brandId);

    @ApiOperation(value = "根据城市city、品牌id,查询信息")
    @GetMapping("/selectByCity/{city}/{brandId}")
    List<ShopDetailDto> selectByCity(@ApiParam(value = "城市", required = true) @PathVariable("city")String city, @ApiParam(value = "品牌id", required = true) @PathVariable("brandId")String brandId);

    @ApiOperation(value = "根据品牌id,查询店铺信息,按店铺序号排列")
    @GetMapping("/selectOrderByIndex/{brandId}")
    List<ShopDetailDto> selectOrderByIndex(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId")String brandId);
}
