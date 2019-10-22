package com.resto.api.brand.define.api;

import com.resto.api.brand.define.hystrix.BrandApiHystrixFallbackFactory;
import com.resto.api.brand.dto.WxServerConfigDto;
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
@Api(description = "微信服务配置", position = 2)
@RequestMapping(value = BrandConstant.WXSERVERCONFIG, produces = MediaType.APPLICATION_JSON_VALUE)
public interface BrandApiWxServerConfig {

    @ApiOperation(value = "根据id查询微信服务配置信息")
    @GetMapping("/selectById/{id}")
    WxServerConfigDto selectById(@ApiParam(value = "id", required = true) @PathVariable("id")Long id);

}
