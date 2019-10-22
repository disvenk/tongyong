package com.resto.geekpos.web.controller;

import com.resto.brand.web.service.BrandSettingService;
import com.resto.geekpos.web.model.CheckResult;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by KONATA on 2016/9/1.
 */
@Controller
@RequestMapping("test")
public class Test extends GenericController{

    @Resource
    BrandSettingService brandSettingService;

    @RequestMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "test");
        if (!result.isSuccess()) {
            return;
        }


        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        JSONObject object = new JSONObject();
        object.put("token","202cb962ac59075b964b07152d234b70");
        object.put("sign","202cb962ac59075b964b07152d234b72");
        object.put("brandId","202cb962ac59075b964b07152d234b72");
        object.put("userId",33);
        json.put("data", object);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}
