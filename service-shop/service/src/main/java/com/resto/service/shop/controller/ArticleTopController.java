package com.resto.service.shop.controller;


import com.resto.api.shop.define.api.ShopApiArticleTop;
import com.resto.api.shop.dto.ArticleTopDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.ArticleTop;
import com.resto.service.shop.service.ArticleTopService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xielc on 2018/1/5.
 */
@RestController
public class ArticleTopController implements ShopApiArticleTop {

    @Autowired
    private ArticleTopService articleTopService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public int insert(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId, ArticleTopDto articleTopDto){
        return articleTopService.insert(mapper.map(articleTopDto, ArticleTop.class));
    }
}
