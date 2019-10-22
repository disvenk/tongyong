package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.MeiTuanOrderDto;
import com.resto.shop.web.model.PlatformOrderExtra;

import java.util.List;

public interface PlatformOrderExtraService extends GenericService<PlatformOrderExtra, String> {

    /**
     *  根据第三方平台的订单ID查询订单详情
     * @param platformOrderId
     * @return
     */
    List<PlatformOrderExtra> selectByPlatformOrderId(String platformOrderId);

    void meituanOrderExtra(MeiTuanOrderDto orderDto);
}
