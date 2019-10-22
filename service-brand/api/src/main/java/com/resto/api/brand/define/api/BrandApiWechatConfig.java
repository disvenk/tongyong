package com.resto.api.brand.define.api;

import com.resto.api.brand.define.hystrix.BrandApiHystrixFallbackFactory;
import com.resto.api.brand.dto.WechatConfigDto;
import com.resto.api.brand.util.BrandConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xielc on 2018/1/2.
 */
@FeignClient(value = BrandConstant.BASE_NAME,fallbackFactory = BrandApiHystrixFallbackFactory.class)
@Api(description = "微信配置", position = 2)
@RequestMapping(value = BrandConstant.WECHATCONFIG, produces = MediaType.APPLICATION_JSON_VALUE)
public interface BrandApiWechatConfig {

    @ApiOperation(value = "根据品牌id查询微信配置信息")
    @GetMapping("/selectByBrandId/{brandId}")
    WechatConfigDto selectByBrandId(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId")String brandId);

    @ApiOperation(value = "根据微信配置id查询微信配置信息")
    @GetMapping("/selectById/{id}")
    WechatConfigDto selectById(@ApiParam(value = "微信配置id", required = true) @PathVariable("id")String id);
}

