package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiAppraise;
import com.resto.api.shop.dto.AppraiseDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.Appraise;
import com.resto.service.shop.exception.AppException;
import com.resto.service.shop.service.AppraiseService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by bruce on 2018-01-10 17:25
 */
@RestController
public class AppraiseController implements ShopApiAppraise {

    @Autowired
    private AppraiseService appraiseService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<AppraiseDto> updateAndListAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                   @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId,
                                                   @ApiParam(value = "当前页数", required = true) @RequestParam("currentPage") Integer currentPage,
                                                   @ApiParam(value = "条数", required = true) @RequestParam("showCount") Integer showCount,
                                                   @ApiParam(value = "最大级别", required = true) @RequestParam("maxLevel") Integer maxLevel,
                                                   @ApiParam(value = "最小级别", required = true) @RequestParam("minLevel") Integer minLevel) {
        return mapper.mapAsList(appraiseService.updateAndListAppraise(shopId, currentPage, showCount, maxLevel, minLevel), AppraiseDto.class);
    }

    @Override
    public AppraiseDto selectDetailedById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "评论id", required = true)
    @PathVariable("id") String id) {
        return mapper.map(appraiseService.selectDetailedById(id), AppraiseDto.class);
    }

    @Override
    public Map<String, Object> appraiseCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "门店id", required = true)
    @PathVariable("shopId") String shopId) {
        return appraiseService.appraiseCount(shopId);
    }

    @Override
    public List<Map<String, Object>> appraiseMonthCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "门店id", required = true)
    @PathVariable("shopId") String shopId) {
        return appraiseService.appraiseMonthCount(shopId);
    }

    @Override
    public AppraiseDto saveAppraise(AppraiseDto appraiseDto) {
        Appraise appraise = mapper.map(appraiseDto, Appraise.class);
        try {
            return mapper.map(appraiseService.saveAppraise(appraise), AppraiseDto.class);
        } catch (AppException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AppraiseDto selectDeatilByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "订单id", required = true)
    @PathVariable("orderId") String orderId) {
        return mapper.map(appraiseService.selectDeatilByOrderId(orderId), AppraiseDto.class);
    }

    @Override
    public List<AppraiseDto> selectCustomerAllAppraise(String brandId, String customerId, Integer currentPage, Integer showCount) {
        return mapper.mapAsList(appraiseService.selectCustomerAllAppraise(customerId, currentPage, showCount), AppraiseDto.class);
    }

    @Override
    public int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                     @ApiParam(value = "顾客id", required = true) @RequestParam("customerId") String customerId) {
        return appraiseService.selectByCustomerCount(customerId);
    }

    @Override
    public List<AppraiseDto> selectByGoodAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId) {
        return mapper.mapAsList(appraiseService.selectByGoodAppraise(),AppraiseDto.class);
    }

    @Override
    public AppraiseDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @ApiParam(value = "评论id", required = true) @PathVariable("id") String id) {
        return mapper.map(appraiseService.selectById(id),AppraiseDto.class);
    }

}
