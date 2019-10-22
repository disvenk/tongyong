package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiUnit;
import com.resto.api.shop.dto.UnitArticleDto;
import com.resto.api.shop.dto.UnitDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.UnitService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UnitController implements ShopApiUnit {

    @Autowired
    private UnitService unitService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<UnitDto> getUnitByArticleid(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                            @ApiParam(value = "菜品id", required = true) @RequestParam("articleId") String articleId) {
        return mapper.mapAsList(unitService.getUnitByArticleid(articleId), UnitDto.class);
    }

    @Override
    public List<UnitDto> getUnitByArticleidWechat(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                  @ApiParam(value = "菜品id", required = true) @RequestParam("articleId") String articleId) {
        return mapper.mapAsList(unitService.getUnitByArticleidWechat(articleId), UnitDto.class);
    }

    @Override
    public List<UnitArticleDto> selectUnitDetail(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                 @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId) {
        return mapper.mapAsList(unitService.selectUnitDetail(shopId), UnitArticleDto.class);
    }

}
