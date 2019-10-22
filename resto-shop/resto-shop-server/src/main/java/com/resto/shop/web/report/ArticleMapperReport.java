package com.resto.shop.web.report;

import com.resto.brand.web.dto.ArticleSellDto;

import java.util.List;
import java.util.Map;

public interface ArticleMapperReport{

    List<ArticleSellDto> queryOrderArtcile(Map<String, Object> selectMap);

    List<ArticleSellDto> selectArticleByType(Map<String, Object> selectMap);

    Map<String, Object> selectArticleOrderCount(Map<String, Object> selectMap);

    List<ArticleSellDto> queryOrderArtcileNew(Map<String, Object> selectMap);

    List<ArticleSellDto> selectArticleByTypeNew(Map<String, Object> selectMap);

    Map<String,Object> selectArticleOrderCountNew(Map<String, Object> selectMap);
}