package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.CustomerDetailDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by xielc on 2018/1/5.
 */
@FeignClient(value = ShopConstant.API_NAME, fallbackFactory = ShopApiHystrixFallbackFactory.class)
@Api(description = "会员详情", position = 1)
@RequestMapping(value = ShopConstant.CUSTOMERDETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiCustomerDetail {

    @ApiOperation(value = "根据会员详情id查询会员信息")
    @GetMapping("/selectById")
    CustomerDetailDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                 @ApiParam(value = "会员详情id", required = true) @RequestParam("id") String id);

    @ApiOperation(value = "红黑榜插入信息")
    @PostMapping("/insert")
    int insert(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
               CustomerDetailDto customerDetailDto);

    @ApiOperation(value = "红黑榜更新信息")
    @PostMapping("/update")
    int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
               CustomerDetailDto customerDetailDto);
}
