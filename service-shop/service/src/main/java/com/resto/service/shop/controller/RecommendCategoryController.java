package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiRecommendCategory;
import com.resto.api.shop.dto.RecommendCategoryDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.RecommendCategoryService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xielc on 2018/1/4.
 */
@RestController
public class RecommendCategoryController implements ShopApiRecommendCategory {

    @Autowired
    private RecommendCategoryService recommendCategoryService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<RecommendCategoryDto> selectListSortShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "店铺shopId", required = true) @RequestParam("shopId") String shopId) {
        return mapper.mapAsList(recommendCategoryService.selectListSortShopId(shopId),RecommendCategoryDto.class);
    }


}
