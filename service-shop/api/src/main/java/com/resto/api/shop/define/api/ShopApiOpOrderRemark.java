package com.resto.api.shop.define.api;

import com.alibaba.fastjson.JSONObject;
import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
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
 * Created by xielc on 2018/1/5.
 */
@FeignClient(value = ShopConstant.API_NAME, fallbackFactory = ShopApiHystrixFallbackFactory.class)
@Api(description = "订单备注", position = 1)
@RequestMapping(value = ShopConstant.ORDERREMARK, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiOpOrderRemark {

    @ApiOperation(value = "根据店铺shopId查询店铺订单备注")
    @GetMapping("/getShopOrderRemark")
    List<JSONObject> getShopOrderRemark(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId,
                                        @ApiParam(value = "店铺shopId", required = true) @RequestParam("shopId")String shopId);

}
