package com.resto.shop.web.service;



import com.resto.shop.web.dto.MaterialStockDo;
import com.resto.shop.web.model.MdMaterialConsumeDetail;

import java.util.List;

public interface MaterialStockService {


    /**
     *
     *  分页查询原料库存
     * @param shopDetailId
     * @return
     */
    List<MaterialStockDo> queryJoin4Page(String shopDetailId, String startTime, String endTime);


    /**
     *
     *  根据id查询原料清单
     * @param id
     * @return
     */

    MaterialStockDo queryById(Long id);


    /**
     *
     *  新增原料库存
     * @param materialStockDo
     * @return
     */
    Integer addMaterial(MaterialStockDo materialStockDo);


    /**
     *
     *  修改原料库存
     * @param materialStockDo
     * @return
     */
    int updateMaterial(MaterialStockDo materialStockDo);



    Integer deleteById(Long id);

    List<MdMaterialConsumeDetail> findMaterialStockConsumeDetailByMaterialId(String shopId, Long materialId);









}
