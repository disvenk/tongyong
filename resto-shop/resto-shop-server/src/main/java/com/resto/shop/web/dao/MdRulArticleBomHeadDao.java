package com.resto.shop.web.dao;

import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.ArticleBomDo;
import com.resto.shop.web.dto.ArticleSellCountDto;
import com.resto.shop.web.dto.MdRulArticleBomHeadDtailDo;
import com.resto.shop.web.model.MdRulArticleBomHead;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MdRulArticleBomHeadDao extends BaseDao<MdRulArticleBomHead> {

    List<MdRulArticleBomHeadDtailDo> queryJoin4Page(String shopDetailId);

    List<ArticleBomDo> queryBom(@Param("shopDetailId") String shopId);

    List<ArticleSellCountDto> findArticleByLastCountTime(@Param("shopId") String shopId, @Param("lastCountTime") String lastCountTime);


}
