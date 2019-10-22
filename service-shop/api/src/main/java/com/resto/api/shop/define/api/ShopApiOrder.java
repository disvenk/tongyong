package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.OrderDto;
import com.resto.api.shop.dto.OrderItemDto;
import com.resto.api.shop.dto.OrderPaymentItemDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "订单", position = 1)
@RequestMapping(value = ShopConstant.ORDER, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiOrder {

    @ApiOperation(value = "查询微信订单详情")
    @GetMapping("/selectWXOrderItems")
    List<OrderDto> selectWXOrderItems(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                      @RequestBody Map<String, Object> map);

    @ApiOperation(value = "查询订单")
    @GetMapping("/listOrder")
    List<OrderDto> listOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                             @ApiParam(value = "开始页", required = true) @RequestParam("start") Integer start,
                             @ApiParam(value = "总长度", required = true) @RequestParam("datalength") Integer datalength,
                             @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                             @ApiParam(value = "订单状态", required = true) @RequestParam("orderState") String orderState,
                             @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId);

    @ApiOperation(value = "根据id查询订单")
    @GetMapping("/selectById")
    OrderDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                        @ApiParam(value = "订单id", required = true) @RequestParam("id") String id);

    @ApiOperation(value = "查询准备订单")
    @GetMapping("/selectReadyOrder")
    List<OrderDto> selectReadyOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId);

    @ApiOperation(value = "推送订单")
    @GetMapping("/pushOrder")
    OrderDto pushOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                       @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "根据id查询订单状态")
    @GetMapping("/selectOrderStatesById")
    OrderDto selectOrderStatesById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "根据id查询订单信息")
    @GetMapping("/getOrderInfo")
    OrderDto getOrderInfo(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                          @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "更新订单")
    @PostMapping("/update")
    int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
               @RequestBody OrderDto orderDto);

    @ApiOperation(value = "跟据parentId查询订单")
    @GetMapping("/selectByParentId")
    List<OrderDto> selectByParentId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                                    @ApiParam(value = "支付类型", required = true) @RequestParam("payType") Integer payType);

    @ApiOperation(value = "查询payBefore")
    @GetMapping("/selectPayBefore")
    BigDecimal selectPayBefore(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "创建订单")
    @PostMapping("/createOrder")
    void createOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                     @RequestBody OrderDto orderDto);

    @ApiOperation(value = "pos折扣")
    @PostMapping("/posDiscount")
    void posDiscount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                     @ApiParam(value = "id", required = true) @RequestParam("parentOrderId") String parentOrderId);

    @ApiOperation(value = "准备订单")
    @PostMapping("/repayOrder")
    void repayOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                    @RequestBody OrderDto orderDto);

    @ApiOperation(value = "准备订单")
    @PostMapping("/cancelOrder")
    void cancelOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                     @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) throws Exception;

    @ApiOperation(value = "查询用户新订单")
    @GetMapping("/findCustomerNewOrder")
    OrderDto findCustomerNewOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                  @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                  @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "查询用户newPackage")
    @GetMapping("/findCustomerNewPackage")
    OrderDto findCustomerNewPackage(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                    @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "检查门店")
    @GetMapping("/checkShop")
    Boolean checkShop(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                      @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "检查菜品数量")
    @GetMapping("/checkArticleCount")
    void checkArticleCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                           @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "设置桌号")
    @GetMapping("/setTableNumber")
    void setTableNumber(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                        @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                        @ApiParam(value = "桌号", required = true) @RequestParam("tableNumber") String tableNumber);

    @ApiOperation(value = "支付价格")
    @GetMapping("/payPrice")
    void payPrice(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                  @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                  @ApiParam(value = "实际价格", required = true) @RequestParam("orderId") BigDecimal factMoney);

    @ApiOperation(value = "使用红包价格")
    @GetMapping("/useRedPrice")
    void useRedPrice(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                     @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                     @ApiParam(value = "实际价格", required = true) @RequestParam("orderId") BigDecimal factMoney);

    @ApiOperation(value = "未支付订单退款")
    @GetMapping("/refundPaymentByUnfinishedOrder")
    void refundPaymentByUnfinishedOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                        @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "后付")
    @GetMapping("/afterPay")
    void afterPay(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                  @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                  @ApiParam(value = "优惠券id", required = true) @RequestParam("couponId") String couponId,
                  @ApiParam(value = "价格", required = true) @RequestParam("price") BigDecimal price,
                  @ApiParam(value = "支付价格", required = true) @RequestParam("pay") BigDecimal pay,
                  @ApiParam(value = "待支付价格", required = true) @RequestParam("waitMoney") BigDecimal waitMoney,
                  @ApiParam(value = "模式", required = true) @RequestParam("payMode") Integer payMode);

    @ApiOperation(value = "用户订单")
    @GetMapping("/customerByOrderForMyPage")
    OrderDto customerByOrderForMyPage(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                      @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                      @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "微信支付订单")
    @PostMapping("/orderWxPaySuccess")
    void orderWxPaySuccess(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                           @RequestBody OrderPaymentItemDto orderPaymentItemDto);

    @ApiOperation(value = "用户订单")
    @GetMapping("/getLastOrderByTableNumber")
    OrderDto getLastOrderByTableNumber(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                       @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                       @ApiParam(value = "桌号", required = true) @RequestParam("tableNumber") String tableNumber);

    @ApiOperation(value = "查询子订单详情")
    @GetMapping("/getChildItem")
    List<OrderDto> getChildItem(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId);

    @ApiOperation(value = "修复退款")
    @PostMapping("/fixedRefund")
    String fixedRefund(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                       @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                       @ApiParam(value = "总数", required = true) @RequestParam("total")int total,
                       @ApiParam(value = "退款", required = true) @RequestParam("refund")int refund,
                       @ApiParam(value = "交易id", required = true) @RequestParam("transaction_id")String transaction_id,
                       @ApiParam(value = "商户id", required = true) @RequestParam("mchid")String mchid,
                       @ApiParam(value = "id", required = true) @RequestParam("id")String id);

    @ApiOperation(value = "阿里退款")
    @PostMapping("/alipayRefund")
    void alipayRefund(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                      @ApiParam(value = "金额", required = true) @RequestParam("money")BigDecimal money);

    @ApiOperation(value = "获取最后一位下单顾客")
    @GetMapping("/getLastOrderByCustomer")
    OrderDto getLastOrderByCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                    @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

    @ApiOperation(value = "SevenMode最后一位下单顾客")
    @GetMapping("/getLastOrderBySevenMode")
    OrderDto getLastOrderBySevenMode(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                     @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId);

}
