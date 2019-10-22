package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.CustomerAddressDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by xielc on 2018/1/4.
 */
@FeignClient(value = ShopConstant.API_NAME, fallbackFactory = ShopApiHystrixFallbackFactory.class)
@Api(description = "R+外卖配送地址", position = 1)
@RequestMapping(value = ShopConstant.CUSTOMERADDRESS, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiCustomerAddress {

    @ApiOperation(value = "根据customer_id查询配送地址")
    @GetMapping("/selectListSortShopId")
    List<CustomerAddressDto> selectOneList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                           @ApiParam(value = "会员customer_id", required = true) @RequestParam("customer_id") String customer_id);

    @ApiOperation(value = "根据配送地址id查询配送地址")
    @GetMapping("/selectByPrimaryKey")
    CustomerAddressDto selectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                          @ApiParam(value = "配送地址id", required = true) @RequestParam("id") String id);

    @ApiOperation(value = "插入配送地址")
    @PostMapping("/insertSelective")
    int insertSelective(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                        CustomerAddressDto record);

    @ApiOperation(value = "更新配送地址")
    @PostMapping("/updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    CustomerAddressDto record);
}
