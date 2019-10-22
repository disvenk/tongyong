package com.resto.api.brand.define.hystrix;

import com.resto.api.brand.define.api.BrandApi;
import com.resto.api.brand.dto.*;
import com.resto.conf.api.BaseClientHystrixFallbackFactory;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

/**
 * title 品牌异常处理
 * company resto
 * author jimmy 2017/10/11 下午2:28
 * version 1.0
 */
@Component
public class BrandApiHystrixFallbackFactory
        extends BaseClientHystrixFallbackFactory<BrandApi>
{

    @Override
    public BrandApi getChild() {
        return new BrandClientFailBackWithInfo();
    }

    private static class BrandClientFailBackWithInfo implements BrandApi {

        @Override
        public BrandDto selectById(@ApiParam(value = "品牌id", required = true) @PathVariable("id") String id) {
            return null;
        }

        @Override
        public BrandDto selectBySign(@ApiParam(value = "二级域名brandSign", required = true) @PathVariable("brandSign") String brandSign) {
            return null;
        }

        @Override
        public BrandDto selectBrandBySetting(@ApiParam(value = "品牌配置settingId", required = true) @PathVariable("settingId") String settingId) {
            return null;
        }

        @Override
        public BrandDto selectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
            return null;
        }

        @Override
        public DatabaseConfigDto getDBSettingByBrandId(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
            return null;
        }

        @Override
        public List<BrandDto> selectList() {
            return null;
        }
    }
}
