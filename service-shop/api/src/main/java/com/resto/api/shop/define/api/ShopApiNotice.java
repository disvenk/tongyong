package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.NoticeDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "通知", position = 1)
@RequestMapping(value = ShopConstant.NOTICE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiNotice {

    @ApiOperation(value = "根据门店id查询")
    @GetMapping("/selectListByShopId")
    List<NoticeDto> selectListByShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                       @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                       @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                       @ApiParam(value = "类型", required = true) @RequestParam("type") Integer type);

    @ApiOperation(value = "查询历史通知")
    @GetMapping("/addNoticeHistory")
    void addNoticeHistory(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                          @ApiParam(value = "通知id", required = true) @RequestParam("noticeId") String noticeId,
                          @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

}
