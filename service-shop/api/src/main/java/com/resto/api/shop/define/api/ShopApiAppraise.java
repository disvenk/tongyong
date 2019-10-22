package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.AppraiseDto;
import com.resto.api.shop.dto.AppraisePraiseDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by bruce on 2017-12-28 15:08
 */
@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "评论", position = 1)
@RequestMapping(value = ShopConstant.APPRAISE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiAppraise {

    @ApiOperation(value = "根据门店id获取评论信息")
    @GetMapping("updateAndListAppraise")
    List<AppraiseDto> updateAndListAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                            @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                            @ApiParam(value = "当前页数", required = true) @RequestParam("currentPage") Integer currentPage,
                                            @ApiParam(value = "条数", required = true) @RequestParam("showCount") Integer showCount,
                                            @ApiParam(value = "最大级别", required = true) @RequestParam("maxLevel") Integer maxLevel,
                                            @ApiParam(value = "最小级别", required = true) @RequestParam("minLevel") Integer minLevel);

    @ApiOperation(value = "根据评论id获取评论详情信息")
    @GetMapping("selectDetailedById/{id}")
    AppraiseDto selectDetailedById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "评论id", required = true)
    @PathVariable("id") String id);

    @ApiOperation(value = "根据门店id获取评论数")
    @GetMapping("appraiseCount/{shopId}")
    Map<String, Object> appraiseCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "门店id", required = true)
    @PathVariable("shopId") String shopId);

    @ApiOperation(value = "根据门店id获取月评论总数")
    @GetMapping("appraiseMonthCount/{shopId}")
    List<Map<String, Object>> appraiseMonthCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "门店id", required = true)
    @PathVariable("shopId") String shopId);

    @ApiOperation(value = "保存评论")
    @PostMapping("saveAppraise")
    AppraiseDto saveAppraise(AppraiseDto appraiseDto);

    @ApiOperation(value = "根据订单id查询评论")
    @GetMapping("selectDeatilByOrderId/{orderId}")
    AppraiseDto selectDeatilByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                      @ApiParam(value = "订单id", required = true) @PathVariable("orderId") String orderId);

    @ApiOperation(value = "查询顾客所以评论")
    @GetMapping("selectCustomerAllAppraise")
    List<AppraiseDto> selectCustomerAllAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                @ApiParam(value = "顾客id", required = true) @RequestParam("customerId") String customerId,
                                                @ApiParam(value = "当前页数", required = true) @RequestParam("currentPage") Integer currentPage,
                                                @ApiParam(value = "展示条数", required = true) @RequestParam("showCount") Integer showCount);

    @ApiOperation(value = "根据顾客id查询条数")
    @GetMapping("selectByCustomerCount")
    int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                              @ApiParam(value = "顾客id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "查询所有的好评")
    @GetMapping("selectByGoodAppraise")
    List<AppraiseDto> selectByGoodAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId);

    @ApiOperation(value = "根据id查询评论")
    @GetMapping("selectById/{id}")
    AppraiseDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                           @ApiParam(value = "评论id", required = true) @PathVariable("id") String id);
}
