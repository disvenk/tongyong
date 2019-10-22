package com.resto.shop.web.dao;

import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.DocStockCountHeaderDo;
import com.resto.shop.web.model.DocStockCountHeader;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DocStockCountHeaderDao extends BaseDao<DocStockCountHeader> {

    List<DocStockCountHeaderDo> findStockList(String shopId);

    String findLastStockCountOfTime(String shopId);

    DocStockCountHeaderDo findStockListById(@Param("shopId") String shopId, @Param("id") Long id);
}
