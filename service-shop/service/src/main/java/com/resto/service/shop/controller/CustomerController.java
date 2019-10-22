package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiCustomer;
import com.resto.api.shop.dto.CustomerDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.Customer;
import com.resto.service.shop.exception.AppException;
import com.resto.service.shop.service.CustomerService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController implements ShopApiCustomer {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public CustomerDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                  @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return mapper.map(customerService.selectById(customerId), CustomerDto.class);
    }

    @Override
    public CustomerDto login(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                             @ApiParam(value = "用户", required = true) @RequestParam("fromUser") String fromUser) {
        return mapper.map(customerService.login(fromUser), CustomerDto.class);
    }

    @Override
    public CustomerDto register(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                CustomerDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        return mapper.map(customerService.register(customer), CustomerDto.class);
    }

    @Override
    public CustomerDto selectByOpenIdInfo(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                          @ApiParam(value = "用户", required = true) @RequestParam("toUser") String toUser) {
        return mapper.map(customerService.selectByOpenIdInfo(toUser), CustomerDto.class);
    }

    @Override
    public void updateCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                               CustomerDto customerDto) {
        customerService.updateCustomer(mapper.map(customerDto, Customer.class));
    }

    @Override
    public CustomerDto registerCard(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    CustomerDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        return mapper.map(customerService.registerCard(customer), CustomerDto.class);
    }

    @Override
    public CustomerDto bindPhone(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                 @ApiParam(value = "手机", required = true) @RequestParam("phone") String phone,
                                 @ApiParam(value = "用户id", required = true) @RequestParam("id") String id) {
        try {
            return mapper.map(customerService.bindPhone(phone, id, 0, null, null, null), CustomerDto.class);
        } catch (AppException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateCustomerWechatId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                      CustomerDto customerDto) {
        return customerService.updateCustomerWechatId(mapper.map(customerDto, Customer.class));
    }

    @Override
    public void update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                       CustomerDto customerDto) {
        customerService.update(mapper.map(customerDto, Customer.class));
    }

    @Override
    public CustomerDto selectBySerialNumber(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                            @ApiParam(value = "df", required = true) @RequestParam("df") String df) {
        return mapper.map(customerService.selectBySerialNumber(df), CustomerDto.class);
    }

    @Override
    public List<CustomerDto> selectList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId) {
        return mapper.mapAsList(customerService.selectList(), CustomerDto.class);
    }

    @Override
    public List<String> selectTelephoneList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId) {
        return customerService.selectTelephoneList();
    }

    @Override
    public void unbindphone(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                            @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        customerService.unbindphone(customerId);
    }

    @Override
    public void updateNewNoticeTime(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                    @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        customerService.updateNewNoticeTime(customerId);
    }

    @Override
    public Boolean checkRegistered(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return customerService.checkRegistered(customerId);
    }

    @Override
    public Integer selectByShareCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                         @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return customerService.selectByShareCustomer(customerId);
    }

    @Override
    public List<CustomerDto> selectShareCustomerList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                     @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId,
                                                     @ApiParam(value = "当前页", required = true) @RequestParam("currentPage") Integer currentPage,
                                                     @ApiParam(value = "展示条数", required = true) @RequestParam("showCount") Integer showCount) {
        return mapper.mapAsList(customerService.selectShareCustomerList(customerId, currentPage, showCount), CustomerDto.class);
    }
}
