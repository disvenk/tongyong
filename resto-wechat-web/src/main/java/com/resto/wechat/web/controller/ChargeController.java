package com.resto.wechat.web.controller;

import com.resto.api.customer.entity.Customer;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.WeChatPayUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.WechatChargeConfigService;
import com.resto.brand.web.service.WxServerConfigService;
import com.resto.shop.web.model.ChargeOrder;
import com.resto.shop.web.model.ChargeSetting;
import com.resto.shop.web.service.ChargeOrderService;
import com.resto.shop.web.service.ChargeSettingService;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.MobilePackageBean;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@RequestMapping("charge")
@RestController
public class ChargeController extends GenericController {

    @Resource
    private ChargeSettingService chargesettingService;
    @Resource
    private ChargeOrderService chargeOrderService;
    @Resource
    NewCustomerService newCustomerService;

    @Autowired
    private WechatChargeConfigService wechatChargeConfigService;

    @Autowired
    private BrandSettingService brandSettingService;

    @Autowired
    WxServerConfigService wxServerConfigService;

    @RequestMapping("rulesList")
    public Result rulesList() {
        return getSuccessResult(chargesettingService.selectListByShopId());
    }

    @RequestMapping("/new/rulesList")
    public void rulesListNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("success", true);
        json.put("message", "请求成功");
      // List<ChargeSetting> chargeSettings = (List<ChargeSetting>) RedisUtil.get(getCurrentBrandId()+"chargeList");
       //判断该人是否是首次充值
        List<ChargeOrder> chargeOrderList = chargeOrderService.selectByCustomerIdAndBrandId(getCurrentCustomerId(),getCurrentBrandId());
        int flag = 0;//默认不是首冲
        if(chargeOrderList.isEmpty()){
            flag=1;
        }
//        if(chargeSettings == null){
//            //chargeSettings = chargesettingService.selectListByShopId();
//            chargeSettings = chargesettingService.selectListByShopIdAndType(flag);
//           RedisUtil.set(getCurrentBrandId()+"chargeList",chargeSettings);
//        }
         List<ChargeSetting> settingList = new ArrayList<>();
         List<ChargeSetting> chargeSettings = chargesettingService.selectListByShopId();
         for(ChargeSetting setting:chargeSettings){
                if(flag==1){//把首冲都放进去
                    if("1".equals(setting.getFirstCharge())){
                        settingList.add(setting);
                    }else if("1,2".equals(setting.getFirstCharge())){
                        settingList.add(setting);
                    }
                }else if(flag==0){
                    if("2".equals(setting.getFirstCharge())){
                        settingList.add(setting);
                    }else if("1,2".equals(setting.getFirstCharge())){
                        settingList.add(setting);
                    }
                }
         }

      //  List<ChargeSetting>  chargeSettings = chargesettingService.selectListByShopIdAndType(flag);
        json.put("data", new JSONArray(settingList));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("chargeWx")
    public Result chargeWx(String settingId) throws UnknownHostException, DocumentException {
        //创建微信充值订单
        ChargeOrder chargeOrder = chargeOrderService.createChargeOrder(settingId, getCurrentCustomerId(), getCurrentShopId(), getCurrentBrandId());
        String body = "ChargeAccount" + chargeOrder.getChargeMoney() + "YUAN";
        String out_trade_no = chargeOrder.getId();
        int total_fee = chargeOrder.getChargeMoney().multiply(new BigDecimal(100)).intValue();
        String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();  //终端IP String(16)
        String openid = getCurrentCustomer().getWechatId();
        String notifyUrl = getBaseUrl() + "pay/chargepay_notify";//回调路径
        WechatConfig config = getCurrentBrand().getWechatConfig();//得到微信配置
        log.info("获取充值config成功:" + config);
        Map<String, String> apiReqeust = WeChatPayUtils.createJSAPIPayRequest(body, out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, config.getAppid(), config.getMchid(), config.getMchkey(), getCurrentBrandId());
        JSONResult<Map<String, String>> result = new JSONResult<>();
        if (apiReqeust.containsKey("ERROR")) {
            result.setMessage(apiReqeust.get("ERROR"));
            result.setSuccess(false);
        } else {
            result.setSuccess(true);
            result.setData(apiReqeust);
        }
        return result;
    }


    @RequestMapping("/new/chargeWx")
    public void chargeWxNew(String settingId, String customerId, HttpServletRequest request, HttpServletResponse response) throws UnknownHostException, DocumentException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        //创建微信充值订单
        ChargeOrder chargeOrder = chargeOrderService.createChargeOrder(settingId, getCurrentCustomerId(), getCurrentShopId(), getCurrentBrandId());
        String body = "ChargeAccount" + chargeOrder.getChargeMoney() + "YUAN";
        String out_trade_no = chargeOrder.getId();
        int total_fee = chargeOrder.getChargeMoney().multiply(new BigDecimal(100)).intValue();
        String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();  //终端IP String(16)
        Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),customerId);  //获取用户会员
        String openid = customer.getWechatId();
        //String openid = getCurrentCustomer().getWechatId();
        String notifyUrl = getBaseUrl() + "pay/chargepay_notify";//回调路径
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
            if (wechatChargeConfig == null) {
                apiReqeust = WeChatPayUtils.createJSAPIPayRequestNew(shopDetail.getAppid(), body,
                        out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, wxServerConfig.getAppid(),
                        wxServerConfig.getMchid(), wxServerConfig.getMchkey(), getCurrentShopId(), StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), shopDetail.getIsSub());
            } else {
                apiReqeust = WeChatPayUtils.createJSAPIPayRequestNew(wechatChargeConfig.getAppid(), body,
                        out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, wxServerConfig.getAppid(),
                        wxServerConfig.getMchid(), wxServerConfig.getMchkey(), getCurrentShopId(), StringUtils.isEmpty(wechatChargeConfig.getMchid()) ? config.getMchid() : wechatChargeConfig.getMchid(), wechatChargeConfig.getIsSub());
            }

        }

        JSONObject json = new JSONObject();
        if (apiReqeust.containsKey("ERROR")) {
            json.put("message", apiReqeust.get("ERROR"));
            json.put("success", false);
        } else {
            //Brand brand = brandService.selectById(order.getBrandId());  //获取品牌
            // result.setSuccess(true);
            // result.setData(apiReqeust);
            json.put("statusCode", "200");
            json.put("message", "");
            json.put("success", true);
            json.put("data", new JSONObject(apiReqeust));
        }
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("/new/chargeWxSumOfMoney")
    public void chargeWxSumOfMoney(BigDecimal money, String customerId, HttpServletRequest request, HttpServletResponse response) throws UnknownHostException, DocumentException {
        byte orderState = 0;
        ChargeOrder chargeOrder = new ChargeOrder(ApplicationUtils.randomUUID(), money,
                new BigDecimal(0), orderState, new Date(), customerId, getCurrentShopId(), getCurrentBrandId());
        //chargeOrder.setChargeBalance(BigDecimal.ZERO);
        chargeOrder.setChargeBalance(money);
        chargeOrder.setRewardBalance(BigDecimal.ZERO);
        chargeOrder.setTotalBalance(BigDecimal.ZERO);
        chargeOrder.setNumberDayNow(0);
        chargeOrder.setType(1);
        chargeOrderService.insert(chargeOrder);

        String body = "ChargeAccount" + money + "YUAN";
        String out_trade_no = chargeOrder.getId();
        int total_fee = money.multiply(new BigDecimal(100)).intValue();
        String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();  //终端IP String(16)
        Customer customer =  newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),customerId);  //获取用户会员
        String openid = customer.getWechatId();
        String notifyUrl = getBaseUrl() + "pay/sum_of_money";//回调路径
        WechatConfig config = getCurrentBrand().getWechatConfig();//得到微信配置
        log.info("获取充值config成功:" + config);
        Map<String, String> apiReqeust = new HashMap<>();

        BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
        WechatChargeConfig wechatChargeConfig = wechatChargeConfigService.selectById(setting.getWechatChargeConfigId()); //得到充值的账户信息
        JSONObject json = new JSONObject();
        String attach = getCurrentBrandId();
        if (wechatChargeConfig == null) {
            json.put("message", apiReqeust.get("ERROR"));
            json.put("success", false);
            ApiUtils.setBackInfo(response, json);
        } else {
            //普通商户模式
            if(wechatChargeConfig.getWxServerId() == null){
                apiReqeust = WeChatPayUtils.createJSAPIPayRequest(body,
                        out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, wechatChargeConfig.getAppid(),
                        wechatChargeConfig.getMchid(), wechatChargeConfig.getMchkey(), attach);
            }else{
                WxServerConfig wxServerConfig = wxServerConfigService.selectById(wechatChargeConfig.getWxServerId());
                //服务商+特约商模式
                apiReqeust = WeChatPayUtils.createJSAPIPayRequestNew(wechatChargeConfig.getAppid(), body,
                        out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, wxServerConfig.getAppid(),
                        wxServerConfig.getMchid(), wxServerConfig.getMchkey(), attach, wechatChargeConfig.getMchid(), wechatChargeConfig.getIsSub());
            }
        }



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

