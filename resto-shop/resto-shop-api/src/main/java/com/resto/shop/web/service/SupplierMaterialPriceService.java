package com.resto.shop.web.service;


import com.resto.shop.web.dto.MdSupplierPriceHeadDo;
import com.resto.shop.web.model.MdSupplierPriceHead;

import java.util.List;

/**
 *
 * 供应商报价管理
 */

public interface SupplierMaterialPriceService {



    /***
     * 新增报价单
     * @param mdSupplierPriceHeadDo
     * @return
     */

    Long addMdSupplierPrice(MdSupplierPriceHeadDo mdSupplierPriceHeadDo);



    /**
     *
     *  分页查询报价单
     * @param
     * @return
     */
    List<MdSupplierPriceHeadDo> queryJoin4Page(String shopDetailId);





    /**
     *
     *  修改报价单
     * @param mdSupplierPriceHeadDo
     * @return
     */
    Long updateMdSupplierPrice(MdSupplierPriceHeadDo mdSupplierPriceHeadDo);

    /**
     *
     *  根据报价单id, 修改单状态
     * @param  id
     * @param  supStatus
     * @return
     */
    Long updateMdSupplierPriceStatus(Long id, String supStatus);




    /**
     *
     *  根据报价单id, 导出报价单详情
     * @param  id
     * @return
     */

    List<MdSupplierPriceHead> exportDocSupplierPrice(Long id);



    Long deleteById(Long id);


    List<MdSupplierPriceHeadDo> findEffectiveList(String shopDetailId, Long supplierId);


    /**
     *
     * 查询该门店下的某一个供应商的有效报价单supCode
     * @param  shopDetailId
     * @param  supplierId
     * @return
     */
    List<String> findEffectiveSupPriceIds(String shopDetailId, Long supplierId);


}
