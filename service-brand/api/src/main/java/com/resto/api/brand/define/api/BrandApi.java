package com.resto.api.brand.define.api;

import com.resto.api.brand.define.hystrix.BrandApiHystrixFallbackFactory;
import com.resto.api.brand.dto.BrandDto;
import com.resto.api.brand.dto.DatabaseConfigDto;
import com.resto.api.brand.util.BrandConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = BrandConstant.BASE_NAME
        , fallbackFactory = BrandApiHystrixFallbackFactory.class
)

@Api(description = "品牌", position = 2)
@RequestMapping(value = BrandConstant.BRAND_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
public interface BrandApi {

    @ApiOperation(value = "根据品牌id查询品牌信息")
    @GetMapping("selectById/{id}")
    BrandDto selectById(@ApiParam(value = "品牌id", required = true) @PathVariable("id")String id);

    @ApiOperation(value = "根据品牌二级域名的 Sign,查询信息")
    @GetMapping("selectBySign/{brandSign}")
    BrandDto selectBySign(@ApiParam(value = "二级域名brandSign", required = true) @PathVariable("brandSign")String brandSign);

    @ApiOperation(value = "根据品牌参数配置id,查询信息")
    @GetMapping("selectBrandBySetting/{settingId}")
    BrandDto selectBrandBySetting(@ApiParam(value = "品牌配置settingId", required = true) @PathVariable("settingId")String settingId);

    @ApiOperation(value = "根据品牌主键id查询品牌信息")
    @GetMapping("selectByPrimaryKey/{brandId}")
    BrandDto selectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId")String brandId);

    @ApiOperation(value = "根据品牌id获取品牌数据库信息")
    @GetMapping("dbConfig/{brandId}")
    DatabaseConfigDto getDBSettingByBrandId(@ApiParam(value = "品牌id", required = true)
                                            @PathVariable("brandId") String brandId);

    @ApiOperation(value = "查询所有品牌")
    @GetMapping("selectList")
    List<BrandDto> selectList();
}