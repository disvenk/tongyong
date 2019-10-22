package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiOrderItem;
import com.resto.api.shop.dto.OrderItemDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.OrderItemService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderItemController implements ShopApiOrderItem {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public OrderItemDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   @ApiParam(value = "订单详情id", required = true) @PathVariable("id") String id) {
        return mapper.map(orderItemService.selectById(id), OrderItemDto.class);
    }

    @Override
    public List<OrderItemDto> listByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                            @ApiParam(value = "订单id", required = true) @PathVariable("id") String id) {
        return mapper.mapAsList(orderItemService.listByOrderId(id), OrderItemDto.class);
    }
}
