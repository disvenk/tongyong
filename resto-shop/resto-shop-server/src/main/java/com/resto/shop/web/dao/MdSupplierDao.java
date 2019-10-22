package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.MdSupplierAndContactDo;
import com.resto.shop.web.dto.SupplierAndPmsHeadDo;
import com.resto.shop.web.dto.SupplierAndSupPriceDo;
import com.resto.shop.web.model.MdSupplier;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MdSupplierDao extends BaseDao<MdSupplier> {

    List<MdSupplierAndContactDo> queryJoin4Page(@Param("shopDetailId") String shopDetailId, @Param("state") Integer state);

    List<SupplierAndSupPriceDo> querySupplierAndSupPrice(@Param("shopDetailId") String shopId);

    List<SupplierAndPmsHeadDo> querySupplierAndPmsHeadDo(@Param("shopDetailId") String shopId);
}
