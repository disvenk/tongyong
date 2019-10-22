package com.resto.api.shop.define.hystrix;

import com.resto.api.shop.define.api.ShopApi;
import com.resto.conf.api.BaseClientHystrixFallbackFactory;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * title 门店异常处理
 * company resto
 * author jimmy 2017/10/11 下午2:28
 * version 1.0
 */
@Component
public class ShopApiHystrixFallbackFactory
        extends BaseClientHystrixFallbackFactory<ShopApi>
{

    @Override
    public ShopApi getChild() {
        return new ShopClientFailBackWithInfo();
    }

    private static class ShopClientFailBackWithInfo implements ShopApi {

        @Override
        public String getBrandByBrandId(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
            return null;
        }
    }
}
