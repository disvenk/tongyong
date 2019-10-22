package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.ArticleTopDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by xielc on 2018/1/5.
 */
@FeignClient(value = ShopConstant.API_NAME, fallbackFactory = ShopApiHystrixFallbackFactory.class)
@Api(description = "红黑榜", position = 1)
@RequestMapping(value = ShopConstant.ARTICLETOP, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiArticleTop {

    @ApiOperation(value = "红黑榜插入信息")
    @PostMapping("/insert")
    int insert(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId, ArticleTopDto articleTopDto);
}
