package com.resto.service.shop.controller;

import com.resto.api.brand.util.JSONResult;
import com.resto.api.shop.define.api.ShopApiChargeOrder;
import com.resto.api.shop.dto.ChargeOrderDto;
import com.resto.api.shop.dto.ChargePaymentDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.ChargeOrder;
import com.resto.service.shop.entity.ChargePayment;
import com.resto.service.shop.service.ChargeOrderService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ChargeOrderController implements ShopApiChargeOrder {

    @Autowired
    private ChargeOrderService chargeOrderService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<ChargeOrderDto> selectByCustomerIdAndBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                             @ApiParam(value = "用户id", required = true) @RequestParam("id") String id) {
        return mapper.mapAsList(chargeOrderService.selectByCustomerIdAndBrandId(id, brandId), ChargeOrderDto.class);
    }

    @Override
    public ChargeOrderDto createChargeOrder(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                            @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                            @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                            @ApiParam(value = "充值id", required = true) @RequestParam("settingId") String settingId) {
        return mapper.map(chargeOrderService.createChargeOrder(settingId, customerId, shopId, brandId), ChargeOrderDto.class);
    }

    @Override
    public int insert(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      ChargeOrderDto chargeOrderDto) {
        return chargeOrderService.insert(mapper.map(chargeOrderDto, ChargeOrder.class));
    }

    @Override
    public void chargeorderWxPaySuccess(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                        @RequestBody ChargePaymentDto chargeOrderDto) {
        chargeOrderService.chargeorderWxPaySuccess(mapper.map(chargeOrderDto, ChargePayment.class));
    }

    @Override
    public BigDecimal selectByCustomerIdNotChangeId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                    @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return chargeOrderService.selectByCustomerIdNotChangeId(customerId);
    }

    @Override
    public JSONResult withdrawals(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @ApiParam(value = "金额", required = true) @RequestParam("money") BigDecimal money,
                                  @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return chargeOrderService.withdrawals(money, customerId);
    }
}
