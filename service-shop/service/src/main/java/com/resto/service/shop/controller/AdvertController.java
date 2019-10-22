package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiAdvert;
import com.resto.api.shop.dto.AdvertDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.AdvertService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdvertController implements ShopApiAdvert {

    @Autowired
    private AdvertService advertService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<AdvertDto> selectListByShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                              @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId) {
        return mapper.mapAsList(advertService.selectListByShopId(shopId),AdvertDto.class);
    }

}
