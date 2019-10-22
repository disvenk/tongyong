package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.ReceiptTitleDto;
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
@Api(description = "发票抬头", position = 1)
@RequestMapping(value = ShopConstant.RECEIPT_TITLE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiReceiptTitle {

    @ApiOperation(value = "根据用户id查询发票列表")
    @GetMapping("selectOneList")
    List<ReceiptTitleDto> selectOneList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                        @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "根据用户id，类型查询列表")
    @GetMapping("selectTypeList")
    List<ReceiptTitleDto> selectTypeList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                         @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                         @ApiParam(value = "类型", required = true) @RequestParam("type") String type);

    @ApiOperation(value = "根据用户id，类型查询默认发票列表")
    @GetMapping("selectDefaultList")
    List<ReceiptTitleDto> selectDefaultList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                            @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "保存发票抬头信息")
    @PostMapping("insertSelective")
    int insertSelective(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                        ReceiptTitleDto receiptTitleDto);

    @ApiOperation(value = "根据id更新发票抬头信息")
    @PostMapping("updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    ReceiptTitleDto receiptTitleDto);

}
