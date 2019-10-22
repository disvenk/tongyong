package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiArticleRecommend;
import com.resto.api.shop.dto.ArticleRecommendDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.ArticleRecommend;
import com.resto.service.shop.service.ArticleRecommendService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleRecommendController implements ShopApiArticleRecommend {

    @Autowired
    private ArticleRecommendService articleRecommendService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public ArticleRecommendDto getRecommentByArticleId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                       @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                          @ApiParam(value = "菜品id", required = true) @RequestParam("articleId") String articleId) {
        ArticleRecommend articleRecommend =articleRecommendService.getRecommentByArticleId(articleId, shopId);
        return mapper.map(articleRecommend, ArticleRecommendDto.class);
    }

}
