package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiOrderPaymentItem;
import com.resto.api.shop.dto.OrderPaymentItemDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.OrderPaymentItem;
import com.resto.service.shop.service.OrderPaymentItemService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderPaymentItemController implements ShopApiOrderPaymentItem {

    @Autowired
    private OrderPaymentItemService orderPaymentItemService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<OrderPaymentItemDto> selectByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                     @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        return mapper.mapAsList(orderPaymentItemService.selectByOrderId(orderId), OrderPaymentItemDto.class);
    }

    @Override
    public OrderPaymentItemDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                          @ApiParam(value = "订单id", required = true) @RequestParam("orderId") String orderId) {
        return mapper.map(orderPaymentItemService.selectById(orderId), OrderPaymentItemDto.class);
    }

    @Override
    public void insertByBeforePay(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @RequestBody OrderPaymentItemDto orderPaymentItemDto) {
        orderPaymentItemService.insertByBeforePay(mapper.map(orderPaymentItemDto, OrderPaymentItem.class));
    }

}
