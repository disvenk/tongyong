package com.resto.shop.web.service;



import com.resto.shop.web.dto.MdRulArticleBomHeadDo;

import java.util.List;

/**
 *
 * 历史bom
 */

public interface ArticleBomHeadHistoryService {




    /**
     *
     *  分页查询历史bom清单  页面显示header 点击详情可以展开bomDetail
     * @param
     * @return
     */
    List<MdRulArticleBomHeadDo> queryJoin4Page(String shopDetailId, String articleId);


    /**
     *
     *  修改bom清单
     * @param mdRulArticleBomHeadDo
     * @return
     */
    Long updateRulArticleBomHead(MdRulArticleBomHeadDo mdRulArticleBomHeadDo);




}
