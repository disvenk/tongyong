package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.ShowPhotoDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "图片", position = 1)
@RequestMapping(value = ShopConstant.SHOW_PHOTO, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiShowPhoto {

    @ApiOperation(value = "根据菜品id查询图片")
    @GetMapping("selectById")
    ShowPhotoDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @ApiParam(value = "菜品id", required = true) @RequestParam("id") Integer id);

    @ApiOperation(value = "更新展示图片")
    @PostMapping("update")
    int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
               ShowPhotoDto showPhotoDto);

}
