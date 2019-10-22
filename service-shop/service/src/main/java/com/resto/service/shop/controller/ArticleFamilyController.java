package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiArticleFamily;
import com.resto.api.shop.dto.ArticleFamilyDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.ArticleFamilyService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleFamilyController implements ShopApiArticleFamily {

    @Autowired
    private ArticleFamilyService articleFamilyService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<ArticleFamilyDto> selectListByDistributionModeId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                                 @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                                 @ApiParam(value = "模式id", required = true) @RequestParam("id") Integer id) {
        return mapper.mapAsList(articleFamilyService.selectListByDistributionModeId(shopId, id), ArticleFamilyDto.class);
    }

}
