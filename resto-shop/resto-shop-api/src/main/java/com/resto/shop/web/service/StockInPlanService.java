package com.resto.shop.web.service;


import com.resto.shop.web.dto.DocStkInPlanDetailDo;
import com.resto.shop.web.dto.DocStkInPlanHeaderDetailDo;
import com.resto.shop.web.dto.DocStkInPlanHeaderDo;
import java.util.List;

/**
 * 入库管理
 */
public interface StockInPlanService {


    /***
     * 微信端,新增入库单
     * @param docStkInPlanHeaderDo
     * @return
     */

    Long addDocStkInPlanHeaderDo(DocStkInPlanHeaderDo docStkInPlanHeaderDo);



    /**
     *
     *  后台分页查询入库单 页面显示header
     * @param
     * @return
     */
    List<DocStkInPlanHeaderDetailDo> queryJoin4Page(String shopDetailId);




    /**
     *
     *  根据HeadId查询 查询入库单明细
     * @param id
     * @return
     */
    List<DocStkInPlanDetailDo> queryByStkInPlanHeaderId(Long id);


    /**
     *
     *  修改入库单
     * @param docStkInPlanHeaderDo
     * @return
     */
    Long updateDocStkInPlanHeaderDo(DocStkInPlanHeaderDo docStkInPlanHeaderDo);


    /**
     *
     *  审核入库单
     * @param id
     * @param orderStatus
     * @return
     */
    Long updateDocStkStatusById(Long id, String orderStatus, String auditName, String shopName);





    Long deleteById(Long id);


}
