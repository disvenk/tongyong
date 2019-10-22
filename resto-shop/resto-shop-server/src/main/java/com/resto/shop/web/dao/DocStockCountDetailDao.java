package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.DocStockCountDetailDo;
import com.resto.shop.web.model.DocStockCountDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocStockCountDetailDao extends BaseDao<DocStockCountDetail> {

    List<DocStockCountDetailDo> findStockDetailListById(Long id);

    List<DocStockCountDetail> selectMaterialId(@Param("materialId") Long materialId);
}
