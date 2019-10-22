package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiAppraiseFile;
import com.resto.api.shop.dto.AppraiseFileDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.AppraiseFile;
import com.resto.service.shop.service.AppraiseFileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppraiseFileController implements ShopApiAppraiseFile{

    @Autowired
    private AppraiseFileService appraiseFileService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<AppraiseFileDto> appraiseFileList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                  @ApiParam(value = "点赞id", required = true) @PathVariable("id") String id) {
        return mapper.mapAsList(appraiseFileService.appraiseFileList(id),AppraiseFileDto.class);
    }

    @Override
    public int insert(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId,
                      AppraiseFileDto appraiseFileDto) {
        AppraiseFile appraiseFile = mapper.map(appraiseFileDto,AppraiseFile.class);
        return appraiseFileService.insert(appraiseFile);
    }


}
