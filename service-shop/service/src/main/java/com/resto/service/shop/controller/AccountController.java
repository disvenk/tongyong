package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiAccount;
import com.resto.api.shop.dto.AccountDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.AccountService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements ShopApiAccount {

    @Autowired
    private AccountService accountService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public AccountDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                 @ApiParam(value = "账户id", required = true) @RequestParam("accountId") String accountId) {
        return mapper.map(accountService.selectById(accountId), AccountDto.class);
    }

    @Override
    public AccountDto selectAccountAndLogByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                      @ApiParam(value = "用户id", required = true) @RequestParam("customerId") String customerId) {
        return mapper.map(accountService.selectAccountAndLogByCustomerId(customerId), AccountDto.class);
    }

}
