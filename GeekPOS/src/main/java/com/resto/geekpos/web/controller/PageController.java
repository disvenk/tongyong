package com.resto.geekpos.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.model.NewCustomCoupon;
import com.resto.shop.web.model.RedConfig;
import com.resto.shop.web.service.NewCustomCouponService;
import com.resto.shop.web.service.RedConfigService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.resto.brand.web.model.Brand;
import com.resto.brand.web.service.BrandService;
import com.resto.geekpos.web.config.SessionKey;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by KONATA on 2016/10/17.
 */
@RequestMapping("/pos")
@Controller
public class PageController extends GenericController {

    @Value("#{configProperties['pos.host']}")
    private String wechatHost;
    
    @Resource
    BrandService brandService;

    @Resource
    BrandSettingService brandSettingService;

    @Resource
    NewCustomCouponService newCustomCouponService;

    @Resource
    RedConfigService redConfigService;
    

    @RequestMapping("/index")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String URL = request.getRequestURL() + "";
        String URI = request.getRequestURI();
        String wechatURL = URL.substring(0, URL.length() - URI.length() + 1) + wechatHost;  //计算域名

        String query = request.getQueryString();

        if (query == null) {
            query = "baseUrl=" +
                    URL.substring(0, URL.length() - URI.length() + 1).substring(0, URL.substring(0, URL.length() - URI.length() + 1).length() - 1);    //添加个品牌域名传参
            query = query == null ? "" : "?" + query;
        } else {
            query += "&baseUrl=" +
                    URL.substring(0, URL.length() - URI.length() + 1).substring(0, URL.substring(0, URL.length() - URI.length() + 1).length() - 1);    //添加个品牌域名传参
            query = query == null ? "" : "?" + query;
        }

        //query += ("&t=" + new Date().getTime());

        query = StringUtils.isEmpty(query) ? "?t=" + new Date().getTime() : query + "&t=" + new Date().getTime();
        
        
    	Brand brand = (Brand) session.getAttribute(SessionKey.CURRENT_BRAND);
        String requestURL = request.getRequestURL().toString();
        if (brand == null) {
            String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
            brand = brandService.selectBySign(brandSign);
            request.getSession().setAttribute(SessionKey.CURRENT_BRAND, brand);
            request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID, brand.getId());
            request.getSession().setAttribute(SessionKey.CURRENT_BRAND_SIGN, brandSign);
        }
        
        response.sendRedirect(wechatURL + query);
    }

}
