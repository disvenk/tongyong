package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ArticleFamily;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleFamilyMapper extends BaseDao<ArticleFamily,String> {

    int insert(ArticleFamily record);

    int updateByPrimaryKey(ArticleFamily record);

	List<ArticleFamily> selectListByDistributionModeId(@Param("currentShopId") String currentShopId, @Param("distributionModeId") Integer distributionModeId);

}
