package com.resto.service.shop.controller;

import com.resto.api.brand.dto.RedPacketDto;
import com.resto.api.shop.define.api.ShopApiRedConfig;
import com.resto.api.shop.dto.RedConfigDto;
import com.resto.api.shop.dto.ShareMoneyDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.RedConfig;
import com.resto.service.shop.service.RedConfigService;
import com.resto.service.shop.service.RedPacketService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RedConfigController implements ShopApiRedConfig {

    @Autowired
    private RedConfigService redConfigService;
    @Autowired
    private RedPacketService redPacketService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<RedConfigDto> selectListByShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                 @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId) {
        return mapper.mapAsList(redConfigService.selectListByShopId(shopId), RedConfigDto.class);
    }

    @Override
    public List<ShareMoneyDto> selectShareMoneyList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                    @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                                    @ApiParam(value = "当前页", required = true) @RequestParam("currentPage") Integer currentPage,
                                                    @ApiParam(value = "展示条数", required = true) @RequestParam("showCount") Integer showCount) {
        return mapper.mapAsList(redPacketService.selectShareMoneyList(customerId, currentPage, showCount), ShareMoneyDto.class);
    }
}
