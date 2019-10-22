package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.MdSupplierPriceHeadDo;
import com.resto.shop.web.model.MdSupplierPriceHead;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SupplierMaterialPriceHeadDao extends BaseDao<MdSupplierPriceHead> {

    List<MdSupplierPriceHeadDo> queryJoin4Page(String shopDetailId);

    List<MdSupplierPriceHeadDo> findEffectiveList(@Param("shopDetailId") String shopDetailId, @Param("supplierId") Long supplierId);

}
