package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.dao.KitchenPrinterDtoMapper;
import com.resto.shop.web.dto.kitchenPrinterDto;
import com.resto.shop.web.service.KitchenPrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class KitchenPrinterServiceImpl implements KitchenPrinterService {

    @Autowired
    private KitchenPrinterDtoMapper KitchenPrinterDtoMapper;


    @Override
    public List<kitchenPrinterDto> selectKitchenAndPriterByShopID(String shopId) {
        return KitchenPrinterDtoMapper.selectByShopId(shopId);
    }
}
