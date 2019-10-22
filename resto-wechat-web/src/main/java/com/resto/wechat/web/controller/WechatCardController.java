package com.resto.wechat.web.controller;

import com.resto.brand.web.model.*;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.WeChatService;
import com.resto.brand.web.service.WechatConfigService;
import com.resto.brand.web.service.WxServerConfigService;
import com.resto.wechat.web.config.SessionKey;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.WeChatCardUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by xielc on 2017/8/25.
 */
@RestController
@RequestMapping("wechatCard")
public class WechatCardController extends GenericController{

    @Resource
    BrandSettingService brandSettingService;

    @Autowired
    WxServerConfigService wxServerConfigService;

    @Resource
    WechatConfigService wechatConfigService;

    @Autowired
    WeChatService weChatService;

    @Value("#{configProperties['membercard.host']}")
    private String membercardHost;

    @RequestMapping("jssdkconfig")
    @ResponseBody
    public void jssdkconfig(String query, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        String URL = request.getRequestURL() + "";
        String URI = request.getRequestURI();
        String wechatURL = URL.substring(0, URL.length() - URI.length() + 1);
        HttpSession session = request.getSession();
        ShopDetail shopDetail = (ShopDetail) session.getAttribute(SessionKey.CURRENT_SHOP);
        Brand brand = getCurrentBrand();
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
        if (brandSetting.getOpenHttps() == 1) {
            wechatURL = wechatURL.replace("http", "https");
        }
        query = query == null ? "" : "?" + query;
        String href;
        href = wechatURL + membercardHost + query;  //计算域名
        String[] jsApiList = new String[]{
                WeChatService.addCard,WeChatService.chooseCard,WeChatService.openCard
        };
        WechatConfig currentConfig = wechatConfigService.selectByBrandId(brand.getId());
        String appId = null;
        String appsecret = null;
        String card_id = null;
        if (shopDetail == null) {
            appId = currentConfig.getAppid();
            appsecret = currentConfig.getAppsecret();
            card_id = currentConfig.getCardId();
        } else {
            if (shopDetail.getWxServerId() == null) {
                appId = currentConfig.getAppid();
                appsecret = currentConfig.getAppsecret();
                card_id = currentConfig.getCardId();
            } else {
                WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
                appId = wxServerConfig.getAppid();
                appsecret = wxServerConfig.getAppsecret();
                card_id = currentConfig.getCardId();
            }
        }
        log.info("获取微信返回地址的href路径：" + href);
        String jsapiTicket = weChatService.getJsAccessToken(appId,appsecret);

        JSONObject jsConfig = WeChatCardUtils.getJsSDKConfig(jsapiTicket, jsApiList, false, href, appId, appsecret, card_id);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", jsConfig);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("refresh_jssdkconfig")
    @ResponseBody
    public String refreshJSSDKconfig(HttpServletRequest request, HttpServletResponse response) {
        if(!WeChatCardUtils.appidCard.equals("")&&!WeChatCardUtils.appsecretCard.equals("")){
            String appid= WeChatCardUtils.appidCard;
            String secret=WeChatCardUtils.appsecretCard;
            WeChatCardUtils.api_ticket_map.remove(appid);
            WeChatCardUtils.getApiTicket(appid, secret);
            return WeChatCardUtils.api_ticket_map.get(appid);
        }
        return null;
    }
}
