package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.AppraiseCommentDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "评论回复", position = 1)
@RequestMapping(value = ShopConstant.APPRAISE_COMMENT, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiAppraiseComment {

    @ApiOperation(value = "根据id获取评论列表")
    @GetMapping("appraiseCommentList")
    List<AppraiseCommentDto> appraiseCommentList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                 @ApiParam(value = "点赞id", required = true) @RequestParam("id") String id);

    @ApiOperation(value = "插入评论")
    @PostMapping("insertComment")
    AppraiseCommentDto insertComment(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                     AppraiseCommentDto appraiseCommentDto);

    @ApiOperation(value = "根据顾客id查询顾客评论条数")
    @PostMapping("selectByCustomerCount")
    int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                              @ApiParam(value = "点赞id", required = true) @RequestParam("customerId") String customerId);

}
