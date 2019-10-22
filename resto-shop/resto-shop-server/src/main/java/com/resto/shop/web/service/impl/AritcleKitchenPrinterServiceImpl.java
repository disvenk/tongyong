package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.dao.AritclekitchenPrinterDtoMapper;
import com.resto.shop.web.dto.AritclekitchenPrinterDto;
import com.resto.shop.web.service.ArticleKitchenPrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class AritcleKitchenPrinterServiceImpl implements ArticleKitchenPrinterService {

    @Autowired
    private AritclekitchenPrinterDtoMapper aritclekitchenPrinterDtoMapper;
    @Override
    public List<AritclekitchenPrinterDto> selectByArticleAndShopId(String shopId) {

        return aritclekitchenPrinterDtoMapper.selectByShopId(shopId);
    }
}
