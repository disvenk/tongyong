package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiOrder;
import com.resto.api.shop.dto.OrderDto;
import com.resto.api.shop.dto.OrderPaymentItemDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.Order;
import com.resto.service.shop.entity.OrderPaymentItem;
import com.resto.service.shop.exception.AppException;
import com.resto.service.shop.service.OrderService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController implements ShopApiOrder {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<OrderDto> selectWXOrderItems(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                             @RequestBody Map<String, Object> map) {
        return mapper.mapAsList(orderService.selectWXOrderItems(map), OrderDto.class);
    }

    @Override
    public List<OrderDto> listOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    @ApiParam(value = "开始页", required = true) @RequestParam("start") Integer start,
                                    @ApiParam(value = "总长度", required = true) @RequestParam("datalength") Integer datalength,
                                    @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                    @ApiParam(value = "订单状态", required = true) @RequestParam("orderState") String orderState,
                                    @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId) {
        return mapper.mapAsList(orderService.listOrder(start, datalength, shopId, customerId, orderState), OrderDto.class);
    }

    @Override
    public OrderDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               @ApiParam(value = "订单id", required = true) @RequestParam("id") String id) {
        Order order = orderService.selectById(id);
        return mapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> selectReadyOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                           @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId) {
        return mapper.mapAsList(orderService.selectReadyOrder(shopId), OrderDto.class);
    }

    @Override
    public OrderDto pushOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                              @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        try {
            return mapper.map(orderService.pushOrder(orderId), OrderDto.class);
        } catch (AppException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OrderDto selectOrderStatesById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                          @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        return mapper.map(orderService.selectOrderStatesById(orderId), OrderDto.class);
    }

    @Override
    public OrderDto getOrderInfo(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                 @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        return mapper.map(orderService.getOrderInfo(orderId), OrderDto.class);
    }

    @Override
    public int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      @RequestBody OrderDto orderDto) {
        return orderService.update(mapper.map(orderDto, Order.class));
    }

    @Override
    public List<OrderDto> selectByParentId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                           @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                                           @ApiParam(value = "支付类型", required = true) @RequestParam("payType") Integer payType) {
        return mapper.mapAsList(orderService.selectByParentId(orderId, payType), OrderDto.class);
    }

    @Override
    public BigDecimal selectPayBefore(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                      @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        return orderService.selectPayBefore(orderId);
    }

    @Override
    public void createOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @RequestBody OrderDto orderDto) {
        try {
            orderService.createOrder(mapper.map(orderDto, Order.class));
        } catch (AppException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void posDiscount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @ApiParam(value = "id", required = true) @RequestParam("parentOrderId") String parentOrderId) {
        orderService.posDiscount(parentOrderId, new BigDecimal("1"), null, new BigDecimal("0"), new BigDecimal("0"), 0);
    }

    @Override
    public void repayOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                           @RequestBody OrderDto orderDto) {
        try {
            orderService.repayOrder(mapper.map(orderDto, Order.class));
        } catch (AppException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) throws Exception {
        orderService.cancelOrder(orderId);
    }

    @Override
    public OrderDto findCustomerNewOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                         @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                         @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                         @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        return mapper.map(orderService.findCustomerNewOrder(customerId, shopId, orderId), OrderDto.class);
    }

    @Override
    public OrderDto findCustomerNewPackage(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                           @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                           @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return mapper.map(orderService.findCustomerNewPackage(customerId, shopId), OrderDto.class);
    }

    @Override
    public Boolean checkShop(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                             @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                             @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        return orderService.checkShop(orderId, shopId);
    }

    @Override
    public void checkArticleCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        orderService.checkArticleCount(orderId);
    }

    @Override
    public void setTableNumber(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                               @ApiParam(value = "桌号", required = true) @RequestParam("tableNumber") String tableNumber) {
        orderService.setTableNumber(orderId, tableNumber);
    }

    @Override
    public void payPrice(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                         @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                         @ApiParam(value = "实际价格", required = true) @RequestParam("orderId") BigDecimal factMoney) {
        orderService.payPrice(factMoney, orderId);
    }

    @Override
    public void useRedPrice(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                            @ApiParam(value = "实际价格", required = true) @RequestParam("orderId") BigDecimal factMoney) {
        orderService.useRedPrice(factMoney, orderId);
    }

    @Override
    public void refundPaymentByUnfinishedOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                               @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        orderService.refundPaymentByUnfinishedOrder(orderId);
    }

    @Override
    public void afterPay(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                         @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                         @ApiParam(value = "优惠券id", required = true) @RequestParam("couponId") String couponId,
                         @ApiParam(value = "价格", required = true) @RequestParam("price") BigDecimal price,
                         @ApiParam(value = "支付价格", required = true) @RequestParam("pay") BigDecimal pay,
                         @ApiParam(value = "待支付价格", required = true) @RequestParam("waitMoney") BigDecimal waitMoney,
                         @ApiParam(value = "模式", required = true) @RequestParam("payMode") Integer payMode) {
        orderService.afterPay(orderId, couponId, price, pay, waitMoney, payMode);
    }

    @Override
    public OrderDto customerByOrderForMyPage(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                             @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                             @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return mapper.map(orderService.customerByOrderForMyPage(customerId, shopId), OrderDto.class);
    }

    @Override
    public void orderWxPaySuccess(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @RequestBody OrderPaymentItemDto orderPaymentItemDto) {
        orderService.orderWxPaySuccess(mapper.map(orderPaymentItemDto, OrderPaymentItem.class));
    }

    @Override
    public OrderDto getLastOrderByTableNumber(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                              @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                              @ApiParam(value = "桌号", required = true) @RequestParam("tableNumber") String tableNumber) {
        return mapper.map(orderService.getLastOrderByTableNumber(tableNumber, shopId), OrderDto.class);
    }

    @Override
    public List<OrderDto> getChildItem(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                       @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        return mapper.mapAsList(orderService.getChildItem(orderId), OrderDto.class);
    }

    @Override
    public String fixedRefund(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                              @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                              @ApiParam(value = "总数", required = true) @RequestParam("total") int total,
                              @ApiParam(value = "退款", required = true) @RequestParam("refund") int refund,
                              @ApiParam(value = "交易id", required = true) @RequestParam("transaction_id") String transaction_id,
                              @ApiParam(value = "商户id", required = true) @RequestParam("mchid") String mchid,
                              @ApiParam(value = "id", required = true) @RequestParam("id") String id) {
        return orderService.fixedRefund(brandId, shopId, total, refund, transaction_id, mchid, id);
    }

    @Override
    public void alipayRefund(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                             @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId,
                             @ApiParam(value = "金额", required = true) @RequestParam("money") BigDecimal money) {
        orderService.alipayRefund(orderId, money);
    }

    @Override
    public OrderDto getLastOrderByCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                           @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                           @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return mapper.map(orderService.getLastOrderByCustomer(customerId, shopId), OrderDto.class);
    }

    @Override
    public OrderDto getLastOrderBySevenMode(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                            @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return mapper.map(orderService.getLastOrderBySevenMode(customerId), OrderDto.class);
    }
}
