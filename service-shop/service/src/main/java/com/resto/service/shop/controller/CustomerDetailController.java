package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiCustomerDetail;
import com.resto.api.shop.dto.CustomerDetailDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.CustomerDetail;
import com.resto.service.shop.service.CustomerDetailService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xielc on 2018/1/5.
 */
@RestController
public class CustomerDetailController implements ShopApiCustomerDetail {

    @Autowired
    private CustomerDetailService customerDetailService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public CustomerDetailDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                        @ApiParam(value = "会员详情id", required = true) @RequestParam("id") String id) {
        return mapper.map(customerDetailService.selectById(id), CustomerDetailDto.class);
    }

    @Override
    public int insert(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      CustomerDetailDto customerDetailDto) {
        return customerDetailService.insert(mapper.map(customerDetailDto, CustomerDetail.class));
    }

    @Override
    public int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      CustomerDetailDto customerDetailDto) {
        return customerDetailService.update(mapper.map(customerDetailDto, CustomerDetail.class));
    }
}
