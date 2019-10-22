package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ArticleUnit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleUnitMapper extends BaseDao<ArticleUnit,Integer> {
    
    List<ArticleUnit> selectListByAttrId(@Param(value = "attrId") Integer attrId);

}