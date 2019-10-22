package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.AppraisePraiseDto;
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

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "评论点赞", position = 1)
@RequestMapping(value = ShopConstant.APPRAISE_PRAISE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiAppraisePraise {

    @ApiOperation(value = "根据门店id获取点赞评论")
    @GetMapping("appraisePraiseList/{id}")
    List<AppraisePraiseDto> appraisePraiseList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                               @ApiParam(value = "点赞id", required = true) @PathVariable("id") String id);

    @ApiOperation(value = "查询顾客评论")
    @GetMapping("selectByAppraiseIdCustomerId")
    AppraisePraiseDto selectByAppraiseIdCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                   @ApiParam(value = "点赞id", required = true) @RequestParam("appraiseId") String appraiseId,
                                                   @ApiParam(value = "顾客id", required = true) @RequestParam("customerId")String customerId);
    @ApiOperation(value = "更新取消评论")
    @GetMapping("updateCancelPraise")
    void updateCancelPraise(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            AppraisePraiseDto appraisePraiseDto);

    @ApiOperation(value = "根据顾客id查询评论点赞人数")
    @GetMapping("selectByCustomerCount")
    int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                              @ApiParam(value = "顾客id", required = true) @RequestParam("customerId") String customerId);

}
