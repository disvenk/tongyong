package com.resto.shop.web.service;


import com.resto.shop.web.dto.MdSupplierAndContactDo;
import com.resto.shop.web.dto.MdSupplierDo;
import com.resto.shop.web.dto.SupplierAndPmsHeadListDo;
import com.resto.shop.web.dto.SupplierAndSupPriceListDo;
import com.resto.shop.web.model.MdSupplier;

import java.util.List;

public interface SupplierService {

    /***
     * 新增供应商
     * @param mdSupplierDo
     * @return
     */

    Long addMdSupplier(MdSupplierDo mdSupplierDo);



    /**
     *
     *  分页查询供应商
     * @param shopDetailId
     * @return
     */
    List<MdSupplierAndContactDo> queryJoin4Page(String shopDetailId, Integer state);


    /**
     *
     *  根据供应商id查询供应商
     * @param id
     * @return
     */
    MdSupplier queryById(Long id);



    /**
     *  修改供应商,并且版本号+1
     * @param mdSupplierDo
     * @return
     */
    Integer updateMdSupplier(MdSupplierDo mdSupplierDo);


    /**
     *  删除供应商
     * @param id
     * @return
     */
    Integer delete(Long id, String shopId);

    List<SupplierAndSupPriceListDo> querySupplierAndSupPrice(String shopId);

    List<SupplierAndPmsHeadListDo> querySupplierAndPmsHeadDo(String shopId);



}
