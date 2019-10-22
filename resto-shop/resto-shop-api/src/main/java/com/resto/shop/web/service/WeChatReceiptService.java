package com.resto.shop.web.service;

import com.resto.shop.web.model.Receipt;

import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-09-19 11:11
 */
public interface WeChatReceiptService {
     String getAuthUrl(Receipt receipt, String brandId, String wechatConfigId, String orderId, String orderNum, Integer type)throws Exception;
     String blueTicket(String brandId, String serialNum, BigDecimal money, String openId, String appid, String secret);
    int updateReceipt(String serialNo);
}
