package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.ExperienceDto;
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

import java.util.List;

/**
 * Created by yangwei on 2018/1/4.
 */
@FeignClient(value = ShopConstant.API_NAME, fallbackFactory = ShopApiHystrixFallbackFactory.class)
@Api(description = "体验", position = 1)
@RequestMapping(value = ShopConstant.EXPERIENCE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiExperience {

    @ApiOperation(value = "根据店铺shopId查询店铺体验信息")
    @GetMapping("/selectListByShopId")
    List<ExperienceDto> selectListByShopId(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId,
                                           @ApiParam(value = "店铺shopId", required = true) @RequestParam("shopId")String shopId);

    @ApiOperation(value = "根据id查询店铺体验信息")
    @GetMapping("/selectById")
    ExperienceDto selectById(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId,
                                           @ApiParam(value = "体验id", required = true) @RequestParam("id")Integer id);
}
