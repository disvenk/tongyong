package com.resto.service.shop.controller;

import com.resto.api.brand.define.api.BrandApi;
import com.resto.api.shop.define.api.ShopApi;
import com.resto.service.shop.datasource.DataSourceContextHolder;
import com.resto.service.shop.service.ArticleTopService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bruce on 2017-11-20 10:45
 */
@RestController
public class ShopController implements ShopApi {

    @Autowired
    private BrandApi brandApi;
    @Autowired
    private ArticleTopService articleTopService;
    @Override
    public String getBrandByBrandId(@ApiParam(value = "品牌id", required = true, defaultValue = "41946c940e194311b117e3fff5327215")
                                    @RequestParam("brandId") String brandId) {
        DataSourceContextHolder.setDataSourceName(brandId);
        articleTopService.selectById(111L);
        System.out.println("=============");
//        System.out.println(brandApi.getDBSettingByBrandId("brandId"));
        return "success";
    }

}
