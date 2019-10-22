package com.resto.geekpos.web.listeners;

import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.WeChatService;
import com.resto.geekpos.web.config.SessionKey;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import com.resto.geekpos.web.utils.EmojiFilter;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.service.CustomerService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by KONATA on 2016/10/16.
 */
public class JumpQueueInterceptor implements HandlerInterceptor {


    @Resource
    BrandService brandService;

    @Resource
    CustomerService customerService;

    @Resource
    ShopDetailService shopDetailService;

    @Autowired
    WeChatService weChatService;

    private static final Logger LOGGER = LoggerFactory.getLogger(QCodeInterceptor.class);

    @Value("#{configProperties['jump.host']}")
    private String jumpHost;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        HttpSession session = request.getSession();
        Brand brand = (Brand) session.getAttribute(SessionKey.CURRENT_BRAND);
        String requestURL = request.getRequestURL().toString();
        if (brand == null) {
            String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
            brand = brandService.selectBySign(brandSign);
            request.getSession().setAttribute(SessionKey.CURRENT_BRAND, brand);
            request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID, brand.getId());
        }

        DataSourceTarget.setDataSourceName(brand.getId());

        WechatConfig wConfig = brand.getWechatConfig();
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (code == null) {
            state = System.currentTimeMillis() + "";
            String query = request.getQueryString();
            query = query == null ? "" : "?" + query;
            String authorizeUrl = weChatService.getUserAuthorizeUrl(state, request.getRequestURL() + query, wConfig.getAppid());
            response.sendRedirect(authorizeUrl);
            return false;
        } else {
            String accessJson = weChatService.getUrlAccessToken(code, wConfig.getAppid(), wConfig.getAppsecret());
            JSONObject access = new JSONObject(accessJson);
            String token = access.optString("access_token");
            String openid = access.optString("openid");
            Customer customer = customerService.login(openid);
            String accessToken = weChatService.getAccessToken(wConfig.getAppid(), wConfig.getAppsecret());
            String customInfoJson = weChatService.getUserInfoSubscribe(accessToken, openid);

            JSONObject cusInfo = new JSONObject(customInfoJson);
            Integer subscribe = 1;
            try{
                subscribe = cusInfo.getInt("subscribe");
            }catch (Exception e){
                LOGGER.error("未获取到公众号是否关注，默认关注！" + e.getMessage());
            }
            if(0 == subscribe){
                customInfoJson = weChatService.getUserInfo(token, openid);
                cusInfo = new JSONObject(customInfoJson);
            }
            if (customer == null) {
                if (cusInfo.has("openid")) {
                    //customerNew 的 相关信息
                    customer = new Customer();
                    String shareCustomer = request.getParameter("shareCustomer");
                    customer.setShareCustomer(shareCustomer);
                    customer.setWechatId(cusInfo.getString("openid"));
                    customer.setNickname(EmojiFilter.filterEmoji(cusInfo.getString("nickname")));
                    customer.setSex(cusInfo.getInt("sex"));
                    customer.setProvince(cusInfo.getString("province"));
                    customer.setCity(cusInfo.getString("city"));
                    customer.setCountry(cusInfo.getString("country"));
                    customer.setHeadPhoto(cusInfo.getString("headimgurl"));
                    customer.setBrandId(brand.getId());
                    customer.setSubscribe(subscribe);
//                    customer.setIsNowRegister(cusInfoSubscribe.getInt("subscribe"));
                    customer = customerService.register(customer);
                } else {
                    return false;
                }
            } else {
                customer.setWechatId(cusInfo.getString("openid"));
                customer.setNickname(EmojiFilter.filterEmoji(cusInfo.getString("nickname")));
                customer.setSex(cusInfo.getInt("sex"));
                customer.setProvince(cusInfo.getString("province"));
                customer.setCity(cusInfo.getString("city"));
                customer.setCountry(cusInfo.getString("country"));
                customer.setHeadPhoto(cusInfo.getString("headimgurl"));
                customer.setBrandId(brand.getId());
                customer.setSubscribe(subscribe);
//                customer.setIsNowRegister(cusInfoSubscribe.getInt("subscribe"));
                customerService.updateCustomer(customer);
            }
            session.setAttribute(SessionKey.CURRENT_CUSTOMER, customer);
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
