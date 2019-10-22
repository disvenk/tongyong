package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiChargeSetting;
import com.resto.api.shop.dto.ChargeSettingDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.ChargeSetting;
import com.resto.service.shop.service.ChargeSettingService;
import io.swagger.annotations.ApiParam;
import javafx.geometry.Orientation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChargeSettingController implements ShopApiChargeSetting{

    @Autowired
    private ChargeSettingService chargeSettingService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<ChargeSettingDto> selectListByShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId) {
        return mapper.mapAsList(chargeSettingService.selectListByShopId(),ChargeSettingDto.class);
    }

}
