package com.resto.shop.web.service;


import com.resto.shop.web.dto.MdRulArticleBomHeadDo;
import com.resto.shop.web.model.MdRulArticleBomDetail;
import com.resto.shop.web.model.MdRulArticleBomHead;

import java.util.List;

/**
 *
 * 菜品bom清单管理
 */

public interface ArticleBomHeadService {


    /***
     * 新增Bom清单
     * @param mdRulArticleBomHeadDo
     * @return
     */

    Long addArticleBomHead(MdRulArticleBomHeadDo mdRulArticleBomHeadDo);


    /**
     *
     *  分页查询bom清单  页面显示header 点击详情可以展开bomDetail
     * @param
     * @return
     */
    List<MdRulArticleBomHeadDo> queryJoin4Page(String shopDetailId);


    /**
     *
     *  根据id查询一个bom清单 暂时用不到
     * @param id
     * @return
     */
    MdRulArticleBomHead queryById(Long id);


    /**
     *
     *  根据articleBomHeadId查询 bom详情
     * @param articleBomHeadId
     * @return
     */
    List<MdRulArticleBomDetail> queryByArticleBomHeadId(Long articleBomHeadId);


    /**
     *
     *  修改bom清单
     * @param mdRulArticleBomHeadDo
     * @return
     */
    Long updateRulArticleBomHead(MdRulArticleBomHeadDo mdRulArticleBomHeadDo);


    Long deleteById(Long id);


    /**
     *
     *
     * @param shopDetailId
     * @param articleId
     * @return
     */

    List<MdRulArticleBomHead> findBomHeadByState(String shopDetailId, String articleId, Integer state);

}
