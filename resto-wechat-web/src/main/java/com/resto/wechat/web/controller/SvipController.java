package com.resto.wechat.web.controller;

import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.model.SvipActivity;
import com.resto.shop.web.service.SvipActivityService;
import com.resto.shop.web.service.SvipService;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import org.apache.taglibs.standard.tag.common.core.RedirectSupport;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;

/**
 * Created by disvenk.dai on 2018-11-14 16:47
 */
@Controller
@RequestMapping("svip")
public class SvipController extends GenericController{

    @Autowired
    BrandSettingService brandSettingService;

    @Autowired
    SvipService svipService;

    @Autowired
    SvipActivityService svipActivityService;

    @RequestMapping("selectBanner")
    @ResponseBody
    public void selectBanner(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AppUtils.unpack(request, response);
        SvipActivity act = svipActivityService.getAct();

        Long svip = svipService.isSvip(getCurrentCustomerId());
        if(act==null || svip>0){
            JSONObject json = new JSONObject();
            json.put("statusCode", "0");
            json.put("success", false);
            ApiUtils.setBackInfo(response,json);
        }
        act.setBeginTimes(act.getBeginDateTime().getTime());
        act.setEndTimes(act.getEndDateTime().getTime());
        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("success", true);
        json.put("data", new JSONObject(act));
        ApiUtils.setBackInfo(response,json);
    }

    @RequestMapping("getCustomerSvip")
    @ResponseBody
    public void getCustomerSvip(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long svip = svipService.isSvip(getCurrentCustomerId());
        Boolean isSvip = false;
        if(svip>0){
         isSvip=true;
        }
        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("success", isSvip);
        ApiUtils.setBackInfo(response,json);
    }
}
