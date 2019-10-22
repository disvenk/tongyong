package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.CustomerDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "用户", position = 1)
@RequestMapping(value = ShopConstant.CUSTOMER, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiCustomer {

    @ApiOperation(value = "根据品牌id获取用户信息")
    @GetMapping("selectById")
    CustomerDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                           @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "用户登录")
    @GetMapping("login")
    CustomerDto login(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      @ApiParam(value = "用户", required = true) @RequestParam("fromUser") String fromUser);

    @ApiOperation(value = "用户注册")
    @GetMapping("register")
    CustomerDto register(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                         CustomerDto customerDto);

    @ApiOperation(value = "获取用户信息")
    @GetMapping("selectByOpenIdInfo")
    CustomerDto selectByOpenIdInfo(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   @ApiParam(value = "用户", required = true) @RequestParam("toUser") String toUser);

    @ApiOperation(value = "更新用户信息")
    @PostMapping("updateCustomer")
    void updateCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                        CustomerDto customerDto);

    @ApiOperation(value = "注册卡")
    @PostMapping("registerCard")
    CustomerDto registerCard(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                             CustomerDto customerDto);

    @ApiOperation(value = "注册卡")
    @GetMapping("bindPhone")
    CustomerDto bindPhone(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                          @ApiParam(value = "手机", required = true) @RequestParam("phone") String phone,
                          @ApiParam(value = "用户id", required = true) @RequestParam("id") String id);

    @ApiOperation(value = "根据wechatId更新用户信息")
    @PostMapping("updateCustomerWechatId")
    int updateCustomerWechatId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               CustomerDto customerDto);

    @ApiOperation(value = "更新用户信息")
    @PostMapping("update")
    void update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                CustomerDto customerDto);

    @ApiOperation(value = "根据序列号查询")
    @GetMapping("selectBySerialNumber")
    CustomerDto selectBySerialNumber(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                     @ApiParam(value = "df", required = true) @RequestParam("df") String df);

    @ApiOperation(value = "根据序列号查询")
    @GetMapping("selectList")
    List<CustomerDto> selectList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId);

    @ApiOperation(value = "查询手机号列表")
    @GetMapping("selectTelephoneList")
    List<String> selectTelephoneList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId);

    @ApiOperation(value = "解绑手机号")
    @GetMapping("unbindphone")
    void unbindphone(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                     @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "更新通知时间")
    @GetMapping("updateNewNoticeTime")
    void updateNewNoticeTime(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                             @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "检查注册")
    @GetMapping("checkRegistered")
    Boolean checkRegistered(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "通过分享用户查询")
    @GetMapping("selectByShareCustomer")
    Integer selectByShareCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "查询分享用户list")
    @GetMapping("selectShareCustomerList")
    List<CustomerDto> selectShareCustomerList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                              @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                              @ApiParam(value = "当前页", required = true) @RequestParam("currentPage") Integer currentPage,
                                              @ApiParam(value = "展示条数", required = true) @RequestParam("showCount") Integer showCount);
}
