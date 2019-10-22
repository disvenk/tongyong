package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiCustomerAddress;
import com.resto.api.shop.define.api.ShopApiRecommendCategory;
import com.resto.api.shop.dto.CustomerAddressDto;
import com.resto.api.shop.dto.RecommendCategoryDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.CustomerAddress;
import com.resto.service.shop.service.CustomerAddressService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xielc on 2018/1/4.
 */
@RestController
public class CustomerAddressController implements ShopApiCustomerAddress {

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<CustomerAddressDto> selectOneList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                  @ApiParam(value = "会员customer_id", required = true) @RequestParam("customer_id") String customer_id){
        return mapper.mapAsList(customerAddressService.selectOneList(customer_id),CustomerAddressDto.class);
    }

    @Override
    public CustomerAddressDto selectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                 @ApiParam(value = "配送地址id", required = true) @RequestParam("id") String id){
        return mapper.map(customerAddressService.selectByPrimaryKey(id),CustomerAddressDto.class);
    }

    @Override
    public int insertSelective(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               CustomerAddressDto record){
        return customerAddressService.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId,
                                           CustomerAddressDto record){
        return customerAddressService.updateByPrimaryKeySelective(record);
    }
}
