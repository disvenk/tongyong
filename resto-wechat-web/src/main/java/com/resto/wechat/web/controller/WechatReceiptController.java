package com.resto.wechat.web.controller;

import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.service.WeChatReceiptService;
import com.resto.wechat.web.form.ReceiptForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by disvenk.dai on 2018-09-18 14:51
 */

@Controller
@RequestMapping("wechatReceipt")
public class WechatReceiptController extends GenericController{

    @Autowired
    WeChatReceiptService weChatReceiptService;

    /**
     *@Description:获取授权页链接
     *@Author:disvenk.dai
     *@Date:16:46 2018/6/1 0001
     */
    @RequestMapping("getAuthUrl")
    public Result getTicket(HttpServletRequest request, HttpServletResponse response,@RequestBody ReceiptForm receiptForm
                          ) throws Exception {

        String authUrl = weChatReceiptService.getAuthUrl(receiptForm.receipt, getCurrentBrandId(), getCurrentBrand().getWechatConfigId(), receiptForm.orderId, receiptForm.serialNum, receiptForm.type);
        JSONResult jsonResult = new JSONResult();
        jsonResult.setData(authUrl);
        jsonResult.setSuccess(true);
        return jsonResult;
    }
}
