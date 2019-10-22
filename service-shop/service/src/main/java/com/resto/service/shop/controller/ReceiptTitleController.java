package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiReceiptTitle;
import com.resto.api.shop.dto.ReceiptTitleDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.ReceiptTitle;
import com.resto.service.shop.service.ReceiptTitleService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReceiptTitleController implements ShopApiReceiptTitle {

    @Autowired
    private ReceiptTitleService receiptTitleService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<ReceiptTitleDto> selectOneList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                               @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return mapper.mapAsList(receiptTitleService.selectOneList(customerId), ReceiptTitleDto.class);
    }

    @Override
    public List<ReceiptTitleDto> selectTypeList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                                @ApiParam(value = "类型", required = true) @RequestParam("type") String type) {
        return mapper.mapAsList(receiptTitleService.selectTypeList(customerId, type), ReceiptTitleDto.class);
    }

    @Override
    public List<ReceiptTitleDto> selectDefaultList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                   @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return mapper.mapAsList(receiptTitleService.selectDefaultList(customerId), ReceiptTitleDto.class);
    }

    @Override
    public int insertSelective(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               ReceiptTitleDto receiptTitleDto) {
        return receiptTitleService.insert(mapper.map(receiptTitleDto, ReceiptTitle.class));
    }

    @Override
    public int updateByPrimaryKeySelective(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                           ReceiptTitleDto receiptTitleDto) {
        return receiptTitleService.update(mapper.map(receiptTitleDto, ReceiptTitle.class));
    }

}
