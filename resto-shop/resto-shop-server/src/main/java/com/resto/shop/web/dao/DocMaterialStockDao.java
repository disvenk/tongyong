package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.MaterialStockDo;
import com.resto.shop.web.model.DocMaterialStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DocMaterialStockDao extends BaseDao<DocMaterialStock> {

    List<MaterialStockDo> queryJoin4Page(@Param("shopId") String shopDetailId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    DocMaterialStock findStockByShopId(@Param("shopId") String shopId, @Param("materialId") Long materialId);

}
