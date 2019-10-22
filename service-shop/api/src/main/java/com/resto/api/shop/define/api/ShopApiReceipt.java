package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.ReceiptDto;
import com.resto.api.shop.dto.ReceiptOrderDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bouncycastle.cms.Recipient;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "发票", position = 1)
@RequestMapping(value = ShopConstant.RECEIPT, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiReceipt {

    @ApiOperation(value = "根据用户id，类型查询发票列表")
    @GetMapping("selectReceiptOrderList")
    List<ReceiptOrderDto> selectReceiptOrderList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                 @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                                 @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                 @ApiParam(value = "状态", required = true) @RequestParam("state") String state);

    @ApiOperation(value = "保存发票信息")
    @PostMapping("insertSelective")
    ReceiptDto insertSelective(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               @RequestBody  ReceiptDto receiptDto);

}
