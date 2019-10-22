package com.resto.service.shop.controller;

import com.resto.api.brand.dto.BrandSettingDto;
import com.resto.api.shop.define.api.ShopApiThird;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.ThirdService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ThirdController implements ShopApiThird {

    @Autowired
    private ThirdService thirdService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public Boolean orderAccept(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               @RequestBody Map map) {
        return thirdService.orderAccept(map, (BrandSettingDto) map.get("brandSetting"));
    }

}
