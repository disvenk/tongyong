package com.resto.shop.web.service;

import com.resto.shop.web.model.CollectPos;

import java.util.Date;
import java.util.List;

public interface ToCollectPostDataService {


    /**
     * 更新失败订单
     * @param collectPo
     */
    void updateFailure(CollectPos collectPo);

    /**
     * 查询所有失败数据
     * @return
     */
    List<CollectPos> selectFailureOrders(Date staDate, Date endDate, String shopId);


}
