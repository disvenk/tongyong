package com.resto.service.brand.controller;

import com.resto.api.brand.define.api.BrandApi;
import com.resto.api.brand.dto.BrandDto;
import com.resto.api.brand.dto.DatabaseConfigDto;
import com.resto.core.common.OrikaBeanMapper;
import com.resto.service.brand.service.BrandService;
import com.resto.service.brand.service.DatabaseConfigService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BrandController implements BrandApi {

    @Autowired
    private BrandService brandService;
    @Autowired
    private DatabaseConfigService databaseConfigService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public BrandDto selectById(@ApiParam(value = "品牌id", required = true) @PathVariable("id") String id) {
        return mapper.map(brandService.selectById(id),BrandDto.class);
    }

    @Override
    public BrandDto selectBySign(@ApiParam(value = "二级域名brandSign", required = true) @PathVariable("brandSign") String brandSign) {
        return mapper.map(brandService.selectBySign(brandSign),BrandDto.class);
    }

    @Override
    public BrandDto selectBrandBySetting(@ApiParam(value = "品牌配置settingId", required = true) @PathVariable("settingId") String settingId) {
        return mapper.map(brandService.selectBrandBySetting(settingId),BrandDto.class);
    }

    @Override
    public BrandDto selectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
        return mapper.map(brandService.selectByPrimaryKey(brandId),BrandDto.class);
    }

    @Override
    public DatabaseConfigDto getDBSettingByBrandId(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
        return databaseConfigService.selectByBrandId(brandId);
    }

    @Override
    public List<BrandDto> selectList(){
        return mapper.mapAsList(brandService.selectList(),BrandDto.class);
    }

}