package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiArticle;
import com.resto.api.shop.dto.ArticleDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.Article;
import com.resto.service.shop.service.ArticleService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController implements ShopApiArticle {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public void addArticleLikes(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                @ApiParam(value = "菜品id", required = true) @PathVariable("id") String id) {
        articleService.addLikes(id);
    }

    @Override
    public void updatePhotoSquare(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @ApiParam(value = "菜品id", required = true) @RequestParam("id") String id,
                                  @ApiParam(value = "photoSquare", required = true) @RequestParam("photoSquare") String photoSquare) {
        articleService.updatePhotoSquare(id, photoSquare);
    }

    @Override
    public ArticleDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "菜品id", required = true) @PathVariable("id") String id) {
        return mapper.map(articleService.selectById(id), ArticleDto.class);
    }

    @Override
    public int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      @RequestBody ArticleDto articleDto) {
        return articleService.update(mapper.map(articleDto, Article.class));
    }

    @Override
    public List<ArticleDto> selectListFull(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                           @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                           @ApiParam(value = "模式id", required = true) @RequestParam("distributionModeId") Integer distributionModeId,
                                           @ApiParam(value = "名称", required = true) @RequestParam("name") String name) {
        return mapper.mapAsList(articleService.selectListFull(shopId, distributionModeId, name), ArticleDto.class);
    }

    @Override
    public List<ArticleDto> selectArticleList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId) {
        List<Article> article=articleService.selectArticleList();
        return mapper.mapAsList(article, ArticleDto.class);
    }

    @Override
    public List<ArticleDto> selectListByShopIdRecommendCategory(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                                @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                                @ApiParam(value = "推荐分类id", required = true) @RequestParam("recommendCcategoryId") String recommendCcategoryId,
                                                                @ApiParam(value = "名称", required = true) @RequestParam("name") String name) {
        return mapper.mapAsList(articleService.selectListByShopIdRecommendCategory(shopId, recommendCcategoryId, name), ArticleDto.class);
    }

    @Override
    public void updateInitialsById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   @ApiParam(value = "菜品名称", required = true) @RequestParam("name") String name,
                                   @ApiParam(value = "菜品id", required = true) @RequestParam("id") String id) {
        articleService.updateInitialsById(name,id);
    }

}
