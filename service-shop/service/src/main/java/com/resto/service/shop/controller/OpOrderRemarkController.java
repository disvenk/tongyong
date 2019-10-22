package com.resto.service.shop.controller;

import com.alibaba.fastjson.JSONObject;
import com.resto.api.shop.define.api.ShopApiOpOrderRemark;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.OrderRemarkService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xielc on 2018/1/5.
 */
@RestController
public class OpOrderRemarkController implements ShopApiOpOrderRemark {

    @Autowired
    private OrderRemarkService orderRemarkService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<JSONObject> getShopOrderRemark(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId,
                                               @ApiParam(value = "店铺shopId", required = true) @RequestParam("shopId")String shopId){
        return orderRemarkService.getShopOrderRemark(shopId);
    }

}
