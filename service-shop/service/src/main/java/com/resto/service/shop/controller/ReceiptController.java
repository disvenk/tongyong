package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiReceipt;
import com.resto.api.shop.dto.ReceiptDto;
import com.resto.api.shop.dto.ReceiptOrderDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.Receipt;
import com.resto.service.shop.service.ReceiptService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReceiptController implements ShopApiReceipt {

    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<ReceiptOrderDto> selectReceiptOrderList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                        @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                                        @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                        @ApiParam(value = "状态", required = true) @RequestParam("state") String state) {
        return mapper.mapAsList(receiptService.selectReceiptOrderList(customerId, shopId, state), ReceiptOrderDto.class);
    }

    @Override
    public ReceiptDto insertSelective(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                      @RequestBody ReceiptDto receiptDto) {
        Receipt receipt = mapper.map(receiptDto, Receipt.class);
        return mapper.map(receipt, ReceiptDto.class);
    }

}
