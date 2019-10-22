package com.resto.wechat.web.controller;

import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.util.WeChatPayUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.WechatChargeConfigService;
import com.resto.brand.web.service.WxServerConfigService;
import com.resto.api.customer.entity.Customer;
import com.resto.shop.web.model.SvipChargeOrder;
import com.resto.shop.web.service.SvipChargeOrderService;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.MobilePackageBean;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-11-14 14:47
 */
@Controller
@RequestMapping("svipCharge")
public class SvipChargeController extends GenericController {

    @Autowired
    private BrandSettingService brandSettingService;

    @Autowired
    private WechatChargeConfigService wechatChargeConfigService;

    @Autowired
    WxServerConfigService wxServerConfigService;

    @Autowired
    SvipChargeOrderService svipChargeOrderService;

    @Autowired
    NewCustomerService newCustomerService;


    @RequestMapping("/svipChargeWx")
    public void svipChargeWx(String activityId, HttpServletRequest request, HttpServletResponse response) throws UnknownHostException, DocumentException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        //创建微信充值订单
        SvipChargeOrder svipChargeOrder = svipChargeOrderService.createChargeOrder(activityId, getCurrentCustomerId(), getCurrentShopId(), getCurrentBrandId());
        String body = "ChargeAccount" + svipChargeOrder.getChargeMoney() + "YUAN";
        String out_trade_no = svipChargeOrder.getId();
        int total_fee = svipChargeOrder.getChargeMoney().multiply(new BigDecimal(100)).intValue();
        String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();  //终端IP String(16)
        Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),getCurrentCustomerId());
        String openid = customer.getWechatId();
        String notifyUrl = getBaseUrl() + "pay/svip_chargepay_notify";//回调路径
        WechatConfig config = getCurrentBrand().getWechatConfig();//得到微信配置
        log.info("获取充值config成功:" + config);
        ShopDetail shopDetail = getCurrentShop();
        Map<String, String> apiReqeust = new HashMap<>();
        if (shopDetail.getWxServerId() == null) {
            apiReqeust = WeChatPayUtils.createJSAPIPayRequest(body, out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, config.getAppid(), config.getMchid(), config.getMchkey(), getCurrentShopId());
        } else {
            BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
            WechatChargeConfig wechatChargeConfig = wechatChargeConfigService.selectById(setting.getWechatChargeConfigId()); //得到充值的账户信息
            WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
            if (wechatChargeConfig != null) {
                apiReqeust = WeChatPayUtils.createJSAPIPayRequestNew(wechatChargeConfig.getAppid(), body,
                        out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, wxServerConfig.getAppid(),
                        wxServerConfig.getMchid(), wxServerConfig.getMchkey(), getCurrentShopId(), StringUtils.isEmpty(wechatChargeConfig.getMchid()) ? config.getMchid() : wechatChargeConfig.getMchid(), wechatChargeConfig.getIsSub());
            } else {
                apiReqeust = WeChatPayUtils.createJSAPIPayRequestNew(shopDetail.getAppid(), body,
                        out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, wxServerConfig.getAppid(),
                        wxServerConfig.getMchid(), wxServerConfig.getMchkey(), getCurrentShopId(), StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), shopDetail.getIsSub());
            }

        }
        JSONObject json = new JSONObject();
        if (apiReqeust.containsKey("ERROR")) {
            json.put("message", apiReqeust.get("ERROR"));
            json.put("success", false);
        } else {
            json.put("statusCode", "200");
            json.put("message", "");
            json.put("success", true);
            json.put("data", new JSONObject(apiReqeust));
        }
        ApiUtils.setBackInfo(response, json);
    }
}
