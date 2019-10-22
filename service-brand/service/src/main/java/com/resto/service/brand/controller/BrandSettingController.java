package com.resto.service.brand.controller;

import com.resto.api.brand.define.api.BrandApiBrandSetting;
import com.resto.api.brand.dto.BrandSettingDto;
import com.resto.core.common.OrikaBeanMapper;
import com.resto.service.brand.service.BrandSettingService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xielc on 2018/1/2.
 */
@RestController
public class BrandSettingController implements BrandApiBrandSetting {

    @Autowired
    private OrikaBeanMapper mapper;

    @Autowired
    private BrandSettingService brandSettingService;

    @Override
    public BrandSettingDto selectByBrandId(@ApiParam(value = "品牌brandId", required = true) @PathVariable("brandId") String brandId) {
        return mapper.map(brandSettingService.selectByBrandId(brandId),BrandSettingDto.class);
    }

}
