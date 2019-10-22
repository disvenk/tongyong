package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiAccountLog;
import com.resto.service.shop.service.AccountLogService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class AccountLogController implements ShopApiAccountLog{

    @Autowired
    private AccountLogService accountLogService;

    @Override
    public BigDecimal selectByShareMoney(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                         @ApiParam(value = "账户id", required = true) @RequestParam("accountId") String accountId) {
        return accountLogService.selectByShareMoney(accountId);
    }

    @Override
    public Integer selectByShareMoneyCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                              @ApiParam(value = "账户id", required = true) @RequestParam("accountId") String accountId) {
        return accountLogService.selectByShareMoneyCount(accountId);
    }
}
