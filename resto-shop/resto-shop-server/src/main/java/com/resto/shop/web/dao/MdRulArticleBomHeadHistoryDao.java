package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.MdRulArticleBomHeadDtailDo;
import com.resto.shop.web.model.MdRulArticleBomHeadHistory;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MdRulArticleBomHeadHistoryDao extends BaseDao<MdRulArticleBomHeadHistory> {


    List<MdRulArticleBomHeadDtailDo> queryJoin4Page(@Param("shopDetailId") String shopDetailId, @Param("articleId") String articleId);


}
