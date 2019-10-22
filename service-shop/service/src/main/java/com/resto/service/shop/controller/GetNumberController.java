package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiGetNumber;
import com.resto.api.shop.dto.GetNumberDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.GetNumber;
import com.resto.service.shop.service.GetNumberService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetNumberController implements ShopApiGetNumber {

    @Autowired
    private GetNumberService getNumberService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public GetNumberDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   @ApiParam(value = "id", required = true) @RequestParam("id") String id) {
        return mapper.map(getNumberService.selectById(id), GetNumberDto.class);
    }

    @Override
    public GetNumberDto getWaitInfoByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "会员id", required = true) @RequestParam("customerId") String customerId, @ApiParam(value = "店铺id", required = true) @RequestParam("shopId") String shopId) {
        return mapper.map(getNumberService.getWaitInfoByCustomerId(customerId,shopId), GetNumberDto.class);
    }


    @Override
    public int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      GetNumberDto getNumberDto) {
        return getNumberService.update(mapper.map(getNumberDto, GetNumber.class));
    }

}
