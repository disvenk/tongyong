package com.resto.shop.web.dao;

import com.resto.brand.web.dto.ArticleTopDto;
import com.resto.shop.web.model.ArticleTop;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ArticleTopMapper  extends GenericDao<ArticleTop,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(ArticleTop record);

    int insertSelective(ArticleTop record);

    ArticleTop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleTop record);

    int updateByPrimaryKey(ArticleTop record);

    int selectSumGoodByTime(@Param("beginDate") Date begin, @Param("endDate") Date end,@Param("shopId") String shopId);
    int selectSumBadByTime(@Param("beginDate") Date begin, @Param("endDate") Date end,@Param("shopId") String shopId);

    List<ArticleTopDto> selectListByTimeAndGoodType(@Param("beginDate") Date xunBegin, @Param("endDate") Date xunEnd, @Param("shopId") String shopId);

    List<ArticleTopDto> selectListByTimeAndBadType(@Param("beginDate") Date xunBegin, @Param("endDate") Date xunEnd, @Param("shopId") String shopId);
}
