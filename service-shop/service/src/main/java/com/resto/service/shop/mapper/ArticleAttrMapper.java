package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ArticleAttr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleAttrMapper extends BaseDao<ArticleAttr, Integer> {

    /**
     * 根据店铺ID查询信息
     * @return
     */
    List<ArticleAttr> selectListByShopId(@Param(value = "shopId") String currentShopId);
    
}