package com.resto.brand.web.service;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-09-18 14:57
 */
public interface WechatFaPiaoService {
     String getAuthUrl(String appId,String appSecret,String json);
     String getS_pappid(String appid, String secret);
     void setPhone(String tel, Integer time,String appid, String secret) throws Exception;
     String getTicket(String appid, String secret);
     String writeReceipt(String appId,String appSecret,String json);
}
