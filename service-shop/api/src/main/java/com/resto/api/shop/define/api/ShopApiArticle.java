package com.resto.api.shop.define.api;

import com.resto.api.shop.define.hystrix.ShopApiHystrixFallbackFactory;
import com.resto.api.shop.dto.ArticleDto;
import com.resto.api.shop.util.ShopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ShopConstant.API_NAME
        , fallbackFactory = ShopApiHystrixFallbackFactory.class
)
@Api(description = "菜品", position = 1)
@RequestMapping(value = ShopConstant.ARTICLE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ShopApiArticle {

    @ApiOperation(value = "添加相关菜品")
    @GetMapping("addArticleLikes/{id}")
    void addArticleLikes(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                         @ApiParam(value = "菜品id", required = true) @PathVariable("id") String id);

    @ApiOperation(value = "更新图片尺寸")
    @GetMapping("updatePhotoSquare")
    void updatePhotoSquare(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                           @ApiParam(value = "菜品id", required = true) @RequestParam("id") String id,
                           @ApiParam(value = "photoSquare", required = true) @RequestParam("photoSquare") String photoSquare);

    @ApiOperation(value = "根据菜品id查找信息")
    @GetMapping("selectById/{id}")
    ArticleDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                          @ApiParam(value = "菜品id", required = true) @PathVariable("id") String id);

    @ApiOperation(value = "更新菜品")
    @PostMapping("modifyArticle")
    int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
               @RequestBody ArticleDto articleDto);

    @ApiOperation(value = "根据门店id，模式id查询菜品")
    @GetMapping("selectListFull")
    List<ArticleDto> selectListFull(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                    @ApiParam(value = "模式id", required = true) @RequestParam("distributionModeId") Integer distributionModeId,
                                    @ApiParam(value = "名称", required = true) @RequestParam("name") String name);

    @ApiOperation(value = "查询所有菜品")
    @GetMapping("selectArticleList")
    List<ArticleDto> selectArticleList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId);

    @ApiOperation(value = "根据门店id查询推荐菜品")
    @GetMapping("selectListByShopIdRecommendCategory")
    List<ArticleDto> selectListByShopIdRecommendCategory(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                         @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                         @ApiParam(value = "推荐分类id", required = true) @RequestParam("recommendCcategoryId") String recommendCcategoryId,
                                                         @ApiParam(value = "名称", required = true) @RequestParam("name") String name);

    @ApiOperation(value = "根据id修改")
    @GetMapping("updateInitialsById")
    void updateInitialsById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @ApiParam(value = "菜品名称", required = true) @RequestParam("name") String name,
                            @ApiParam(value = "菜品id", required = true) @RequestParam("id") String id);

}
