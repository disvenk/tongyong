package com.resto.pos.web.interceptors;

import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.pos.web.config.SessionKey;
import com.resto.pos.web.controller.PageController;
import com.resto.pos.web.rpcinterceptors.DataSourceTarget;
import com.resto.shop.web.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.resto.pos.web.controller.GenericController.getIpAddr;

public class PosLoginInterceptor implements HandlerInterceptor {

    @Resource
    BrandService brandService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    CustomerService customerService;

    @Resource
    PageController pageController;

    @Autowired
    PosUserService posUserService;

    @Autowired
    BrandUserService brandUserService;

    @Autowired
    BrandSettingService brandSettingService;


    @Resource
    AccountSettingService accountSettingService;

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(72000);
        String urlIgr = ".*/((login)|(ping)|(xlogin)|(meituan)|(socket)|(orderServer)|(tvLogin)|(LocalPosSyncData)).*";
        String path = request.getServletPath();
        if (request.getRequestURI().matches(".*/loginOut.*")) {
            posUserService.deleteUserByIp(getIpAddr(request));
            session.removeAttribute(SessionKey.USER_INFO);
            response.sendRedirect("/pos/login?redirect=" + request.getRequestURL());
            return false;
        }

        if (path.matches(urlIgr)) {
            return true;
        }
        if (request.getRequestURI().matches(".*/tv/getOrderItems.*")) {
            return true;
        }
        if (session.getAttribute(SessionKey.USER_INFO) == null) {
            if (getIpAddr(request).contains("116.228.222.198") || getIpAddr(request).contains("0:0:0:0:0:0:0:1")) {
                response.sendRedirect("/pos/login?redirect=" + request.getRequestURL());
                return false;
            }
            PosUser posUser = posUserService.getUserByIp(getIpAddr(request));
            if(getIpAddr(request).contains("116.228.222.198")){
                response.sendRedirect("/pos/login?redirect=" + request.getRequestURL());
                return false;
            }
            if (posUser == null) {
                response.sendRedirect("/pos/login?redirect=" + request.getRequestURL());
                return false;
            } else {
                BrandUser loginUser = brandUserService.authentication(new BrandUser(posUser.getUserName(), ApplicationUtils.pwd(posUser.getPassWord())));
                if (loginUser == null) {
                    response.sendRedirect("/pos/login?redirect=" + request.getRequestURL());
                    return false;
                } else {
                    ShopDetail shop = shopDetailService.selectById(loginUser.getShopDetailId());
                    Brand brand = brandService.selectById(shop.getBrandId());
                    BrandSetting brandSetting = brandSettingService.selectById(brand.getBrandSettingId());
                    session.setAttribute(SessionKey.CURRENT_BRAND_SETTING, brandSetting);
                    session.setAttribute(SessionKey.USER_INFO, loginUser);
                    session.setAttribute(SessionKey.CURRENT_SHOP, shop);
                    session.setAttribute(SessionKey.CURRENT_SHOP_ID, shop.getId());
                    session.setAttribute(SessionKey.CURRENT_BRAND, brand);

                    //yz 2017/07/29 存品牌账户相关信息 -- begin
                    if(brandSetting.getOpenBrandAccount()==1){
                        request.getSession().setAttribute(SessionKey.OPENBRANDACCOUNT,true);
                        AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
                        request.getSession().setAttribute(SessionKey.CURRENT_ACCOUNT_SETTING,accountSetting);
                    }else {
                        request.getSession().setAttribute(SessionKey.OPENBRANDACCOUNT,false);
                    }

                    //yz 存品牌账户相关信息 --- end

                    DataSourceTarget.setDataSourceName(brand.getId());
                    return true;
                }
            }
        } else {
            Brand brand = (Brand) session.getAttribute(SessionKey.CURRENT_BRAND);
            DataSourceTarget.setDataSourceName(brand.getId());
            return true;
        }
    }

    private Map<String, String> cookiesToMap(Cookie[] cookies) {
        Map<String, String> cMap = new HashMap<String, String>();
        if (cookies == null) {
            return cMap;
        }
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();
            cMap.put(name, value);
        }
        return cMap;
    }


}
