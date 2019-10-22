package com.resto.service.brand.controller;

import com.resto.api.brand.define.api.BrandApiWechatConfig;
import com.resto.api.brand.dto.WechatConfigDto;
import com.resto.core.common.OrikaBeanMapper;
import com.resto.service.brand.service.WechatConfigService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xielc on 2018/1/2.
 */
@RestController
public class WechatConfigController implements BrandApiWechatConfig {

    @Autowired
    private WechatConfigService wechatConfigService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public WechatConfigDto selectByBrandId(@ApiParam(value = "品牌id", required = true) @PathVariable("brandId") String brandId) {
        return mapper.map(wechatConfigService.selectByBrandId(brandId),WechatConfigDto.class);
    }

    @Override
    public WechatConfigDto selectById(@ApiParam(value = "微信配置id", required = true) @PathVariable("id") String id) {
        return mapper.map(wechatConfigService.selectById(id),WechatConfigDto.class);
    }

}
