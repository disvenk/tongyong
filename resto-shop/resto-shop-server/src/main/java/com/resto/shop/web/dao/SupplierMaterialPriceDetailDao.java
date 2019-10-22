package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.model.MdSupplierPriceDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupplierMaterialPriceDetailDao extends BaseDao<MdSupplierPriceDetail> {

    List<MdSupplierPriceDetail> queryJoinMaterialView(@Param("supPriceId") Long supPriceId, @Param("version") Integer version);
}
