package com.resto.shop.web.service;



import com.resto.shop.web.dto.DocStockCountHeaderDo;
import com.resto.shop.web.dto.DocStockInput;
import com.resto.shop.web.dto.MaterialStockDo;

import java.util.List;

/**
 * 库存盘点管理
 */
public interface StockCountCheckService {

    //查询所有盘点单
    List<DocStockCountHeaderDo> findStockList(String shopId);

    //保存盘点单
    boolean saveStock(DocStockInput docStockInput);

    //初始化盘点单
    List<MaterialStockDo> findDefaultStock(String shopId);


    /**
     *
     * 根据盘点单id审核
     * @param id
     * @param status
     * @return
     */
     Long approveStockStatusById(Long id, String status, String shopId, String auditName);


    void scheduledTaskComsumerMaterialCount(String brandId, String shopId);

}
