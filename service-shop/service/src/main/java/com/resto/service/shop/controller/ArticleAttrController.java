package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiArticleAttr;
import com.resto.api.shop.dto.ArticleAttrDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.ArticleAttr;
import com.resto.service.shop.service.ArticleAttrService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleAttrController implements ShopApiArticleAttr {

    @Autowired
    private ArticleAttrService articleAttrService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<ArticleAttrDto> selectListByShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                   @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId) {
        return mapper.mapAsList(articleAttrService.selectListByShopId(shopId),ArticleAttrDto.class);
    }

}
