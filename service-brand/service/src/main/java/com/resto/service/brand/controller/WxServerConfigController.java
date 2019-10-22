package com.resto.service.brand.controller;

import com.resto.api.brand.define.api.BrandApiWxServerConfig;
import com.resto.api.brand.dto.WxServerConfigDto;
import com.resto.core.common.OrikaBeanMapper;
import com.resto.service.brand.service.WxServerConfigService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xielc on 2018/1/2.
 */
@RestController
public class WxServerConfigController implements BrandApiWxServerConfig {

    @Autowired
    private WxServerConfigService wxServerConfigService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public WxServerConfigDto selectById(@ApiParam(value = "id", required = true) @PathVariable("id") Long id) {
        return mapper.map(wxServerConfigService.selectById(id),WxServerConfigDto.class);
    }

}
