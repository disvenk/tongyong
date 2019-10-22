package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiNotice;
import com.resto.api.shop.dto.NoticeDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.NoticeService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NoticeController implements ShopApiNotice {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<NoticeDto> selectListByShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                              @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                              @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                              @ApiParam(value = "类型", required = true) @RequestParam("type") Integer type) {
        return mapper.mapAsList(noticeService.selectListByShopId(shopId, customerId, type), NoticeDto.class);
    }

    @Override
    public void addNoticeHistory(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                 @ApiParam(value = "通知id", required = true) @RequestParam("noticeId") String noticeId,
                                 @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        noticeService.addNoticeHistory(customerId, noticeId);
    }

}
