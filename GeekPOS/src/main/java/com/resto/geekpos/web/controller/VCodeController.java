package com.resto.geekpos.web.controller;

import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.service.VCodeService;
import com.resto.geekpos.web.constant.DictConstants;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.service.SmsLogService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by carl on 2016/9/5.
 */
@Controller
@RequestMapping("vcode")
public class VCodeController  extends GenericController{

    @Resource
    VCodeService vCodeService;

    @Resource
    SmsLogService smsLogService;

    @Value("#{configProperties['signName']}")
    private String signName;


    @RequestMapping(value="/getCode",method = RequestMethod.POST)
    public void getCode(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        String phone = queryParams.get("phone").toString();

        JSONObject json = new JSONObject();

        if(phone.length() != 11 && !isStringNumber(phone)){
            json.put("code", DictConstants.FAIL);
            json.put("msg", "手机号格式不对");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }else {
            String code = vCodeService.insertVCode(phone);
            SMSUtils.sendMessage(phone, null, "餐加",code);

            json.put("code", DictConstants.SUCCESS);
            json.put("msg", "请求成功");
            json.put("data", code);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }


    }

    public boolean isStringNumber(String str) {
        try {
            int num = Integer.valueOf(str);//把字符串强制转换为数字
            return true;//如果是数字，返回True
        } catch (Exception e) {
            return false;//如果抛出异常，返回False}
        }
    }
}
