package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.AppraiseFileDto;
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
@Api(description = "评论文件", position = 1)
@RequestMapping(value = ShopConstant.APPRAISE_FILE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiAppraiseFile {

    @ApiOperation(value = "根据id获取评论文件列表")
    @GetMapping("appraiseFileList/{id}")
    List<AppraiseFileDto> appraiseFileList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                           @ApiParam(value = "点赞id", required = true) @PathVariable("id") String id);

    @ApiOperation(value = "保存菜品文件")
    @PostMapping("insert")
    int insert(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
               AppraiseFileDto appraiseFileDto);
}
