package com.resto.wechat.web.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.*;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.*;
import com.resto.wechat.web.config.Config;
import com.resto.wechat.web.rpcinterceptors.DataSourceTarget;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.MobilePackageBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@RequestMapping("pay")
@RestController
public class PayController extends GenericController {
    Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    OrderService orderService;
    @Resource
    BrandService brandService;

    @Autowired
    BrandSettingService brandSettingService;

    @Autowired
    OrderPaymentItemService orderPaymentItemService;

    @Resource
    ChargeOrderService chargeOrderService;

    @Autowired
    ShopDetailService shopDetailService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    WxServerConfigService wxServerConfigService;

    @Autowired
    WechatConfigService wechatConfigService;

    @Autowired
    private WechatChargeConfigService wechatChargeConfigService;

    @Autowired
    RedisService redisService;

    @Autowired
    SvipChargeOrderService svipChargeOrderService;

    @RequestMapping("orderpay_notify")
    @ResponseBody
    public String orderpay_notify(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> resultMap = getResultMap(request);
        Map<String, String> wxResult = new HashMap<>();
        wxResult.put("return_code", "SUCCESS");
        if ("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))) {
            log.info("定位目标数据库:" + DataSourceTarget.getDataSourceName());

            String mchKey;
//            log.info("结果集:" + resultMap);
//            log.info("结果集:" + resultMap.get("out_trade_no"));
//            try {
//                Thread thread = new Thread();
//                thread.sleep(60000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            //DataSourceTarget.setDataSourceName((String) RedisUtil.get(resultMap.get("attach") + "WxPayBrandId"));
            //DataSourceTarget.setDataSourceName(getCurrentBrandId());

            Order order = orderService.selectOrderInfo(resultMap.get("attach"));
            log.info("---------微信端订单状态-1-----------"+order.getOrderState());
           /* if(order.getOrderState() >= OrderState.PAYMENT && order.getOrderState() != OrderState.CANCEL){
                //已付
                String wxResultXml = WeChatPayUtils.mapToXml(wxResult);
                log.info("微信支付返回成功信息 id:" + resultMap.get("transaction_id") + " 返回信息：" + wxResultXml);
                return wxResultXml;
            }*/

            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
            WechatConfig config = wechatConfigService.selectByBrandId(shopDetail.getBrandId());
            Brand brand = brandService.selectByPrimaryKey(order.getBrandId());
            if (shopDetail.getWxServerId() == null){
                mchKey = config.getMchkey();
            }else{
                WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
                mchKey = wxServerConfig.getMchkey();
            }
            log.info("得到支付密钥:"+mchKey);
            if (WeChatPayUtils.validSign(resultMap, mchKey)) {
                try {
                    //标识当前订单微信支付完成
                    redisService.set(resultMap.get("attach") + "WxPay", false, 60L);
                    redisService.set(resultMap.get("attach") + "wxPaySuccess", 2);
//                    log.info("接收到微信支付通知:" + resultMap);
                    OrderPaymentItem item = new OrderPaymentItem();
                    item.setOrderId(resultMap.get("attach"));
                    item.setId(resultMap.get("transaction_id"));
                    item.setPaymentModeId(PayMode.WEIXIN_PAY);
                    item.setPayTime(new Date());
                    item.setPayValue(new BigDecimal(resultMap.get("total_fee")).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                    item.setRemark("来自微信支付，支付金额为" + item.getPayValue() + "元" + " 订单为:" + item.getOrderId());
                    item.setResultData(new JSONObject(resultMap).toString());
//                    log.info("111:" + resultMap);
                    log.info("--------开始进入orderWxPaySuccess方法--------");
                    JSONResult o = orderService.orderWxPaySuccess(item);
                    orderService.orderWxPaySuccessAspect(o);
                    log.info("--------开始退出orderWxPaySuccess方法--------");
                    Map orderMap = new HashMap(4);
                    orderMap.put("brandName", brand.getBrandName());
                    orderMap.put("fileName", order.getId());
                    orderMap.put("type", "orderAction");
                    orderMap.put("content", "订单:"+order.getId()+"收到微信支付回调,支付金额为" + item.getPayValue() + "元,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(orderMap);
                    Map customerMap = new HashMap(4);
                    customerMap.put("brandName", brand.getBrandName());
                    customerMap.put("fileName", order.getCustomerId());
                    customerMap.put("type", "UserAction");
                    customerMap.put("content", "用户:"+order.getCustomerId()+"收到微信支付回调,支付金额为" + item.getPayValue() + "元订单Id:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(customerMap);
//                    log.info("保存订单支付信息成功:" + resultMap);
                } catch (Exception e) {
                    wxResult.put("return_code", "FAIL");
                    wxResult.put("return_msg", e.toString());
//                    log.error("接受微信支付请求失败:" + e.getMessage());
                    Map orderMap = new HashMap(4);
                    orderMap.put("brandName", brand.getBrandName());
                    orderMap.put("fileName", order.getId());
                    orderMap.put("type", "orderAction");
                    orderMap.put("content", "订单:"+order.getId()+"收到微信支付回调，接受微信支付请求失败:" + resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(orderMap);
                    Map customerMap = new HashMap(4);
                    customerMap.put("brandName", brand.getBrandName());
                    customerMap.put("fileName", order.getCustomerId());
                    customerMap.put("type", "UserAction");
                    customerMap.put("content", "用户:"+order.getCustomerId()+"收到微信支付回调，接受微信支付请求失败:" + resultMap.toString()+"订单Id:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(customerMap);
                }
            } else {
                wxResult.put("return_code", "FAIL");
                wxResult.put("return_msg", "签名失败");
                Map orderMap = new HashMap(4);
                orderMap.put("brandName", brand.getBrandName());
                orderMap.put("fileName", order.getId());
                orderMap.put("type", "orderAction");
                orderMap.put("content", "订单:"+order.getId()+"收到微信支付回调，接受微信支付请求失败: 签名失败"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(orderMap);
                Map customerMap = new HashMap(4);
                customerMap.put("brandName", brand.getBrandName());
                customerMap.put("fileName", order.getCustomerId());
                customerMap.put("type", "UserAction");
                customerMap.put("content", "用户:"+order.getCustomerId()+"收到微信支付回调，接受微信支付请求失败: 签名失败"+resultMap.toString()+"订单Id:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc( customerMap);
            }
        }
        String wxResultXml = WeChatPayUtils.mapToXml(wxResult);
        log.info("微信支付返回成功信息 id:" + resultMap.get("transaction_id") + " 返回信息：" + wxResultXml);
        return wxResultXml;
    }


    @RequestMapping("/houfu/orderpay_notify")
    @ResponseBody
    public String houfu_orderpay_notify(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> resultMap = getResultMap(request);
        Map<String, String> wxResult = new HashMap<>();
        wxResult.put("return_code", "SUCCESS");
        if ("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))) {
//            log.info("定位目标数据库:" + DataSourceTarget.getDataSourceName());
            Order order = orderService.selectById(resultMap.get("attach"));
            WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
            Brand brand = brandService.selectByPrimaryKey(order.getBrandId());
            if (WeChatPayUtils.validSign(resultMap, config.getMchkey())) {
                try {
                    log.info("接收到微信支付通知:" + resultMap);
                    OrderPaymentItem item = new OrderPaymentItem();
                    item.setOrderId(resultMap.get("attach"));
                    item.setId(resultMap.get("transaction_id"));
                    item.setPaymentModeId(PayMode.WEIXIN_PAY);
                    item.setPayTime(new Date());
                    item.setPayValue(new BigDecimal(resultMap.get("total_fee")).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                    item.setRemark("来自微信支付，支付金额为" + item.getPayValue() + "元" + " 订单为:" + item.getOrderId());
                    item.setResultData(new JSONObject(resultMap).toString());
//                    log.info("111:" + resultMap);
                    JSONResult o = orderService.orderWxPaySuccess(item);
                    orderService.orderWxPaySuccessAspect(o);
                    Map orderMap = new HashMap(4);
                    orderMap.put("brandName", brand.getBrandName());
                    orderMap.put("fileName", order.getId());
                    orderMap.put("type", "orderAction");
                    orderMap.put("content", "订单:"+order.getId()+"收到微信支付回调,支付金额为" + item.getPayValue() + "元,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(orderMap);
                    Map customerMap = new HashMap(4);
                    customerMap.put("brandName", brand.getBrandName());
                    customerMap.put("fileName", getCurrentCustomer().getId());
                    customerMap.put("type", "UserAction");
                    customerMap.put("content", "用户:"+order.getCustomerId()+"收到微信支付回调,支付金额为" + item.getPayValue() + "元订单Id:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(customerMap);
//                    log.info("保存订单支付信息成功:" + resultMap);
                } catch (Exception e) {
                    wxResult.put("return_code", "FAIL");
                    wxResult.put("return_msg", e.toString());
                    Map orderMap = new HashMap(4);
                    orderMap.put("brandName", brand.getBrandName());
                    orderMap.put("fileName", order.getId());
                    orderMap.put("type", "orderAction");
                    orderMap.put("content", "订单:"+order.getId()+"收到微信支付回调，接受微信支付请求失败:" + e.getMessage()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(orderMap);
                    Map customerMap = new HashMap(4);
                    customerMap.put("brandName", brand.getBrandName());
                    customerMap.put("fileName", order.getCustomerId());
                    customerMap.put("type", "UserAction");
                    customerMap.put("content", "用户:"+order.getCustomerId()+"收到微信支付回调，接受微信支付请求失败:" + e.getMessage()+"订单Id:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(customerMap);
//                  log.error("接受微信支付请求失败:" + e.getMessage());
                }
            } else {
                wxResult.put("return_code", "FAIL");
                wxResult.put("return_msg", "签名失败");
                Map orderMap = new HashMap(4);
                orderMap.put("brandName", brand.getBrandName());
                orderMap.put("fileName", order.getId());
                orderMap.put("type", "orderAction");
                orderMap.put("content", "订单:"+order.getId()+"收到微信支付回调，接受微信支付请求失败: 签名失败,请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc( orderMap);
                Map customerMap = new HashMap(4);
                customerMap.put("brandName", brand.getBrandName());
                customerMap.put("fileName", order.getCustomerId());
                customerMap.put("type", "UserAction");
                customerMap.put("content", "用户:"+order.getCustomerId()+"收到微信支付回调，接受微信支付请求失败: 签名失败订单Id:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc( customerMap);
            }
        }
        String wxResultXml = WeChatPayUtils.mapToXml(wxResult);
        log.info("微信支付返回成功信息 id:" + resultMap.get("transaction_id") + " 返回信息：" + wxResultXml);
        return wxResultXml;
    }

    @RequestMapping("chargepay_notify")
    @ResponseBody
    public String chargepay_notify(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> resultMap = getResultMap(request);
        Map<String, String> wxResult = new HashMap<>();
        wxResult.put("return_code", "SUCCESS");
        if ("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))) {
//            log.info("充值回调:定位目标数据库，" + DataSourceTarget.getDataSourceName());
            String mchKey = "";
            log.info("shopId:" + resultMap.get("attach"));
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey( resultMap.get("attach"));
            BrandSetting setting = brandSettingService.selectByBrandId(shopDetail.getBrandId());
            WechatChargeConfig wechatChargeConfig = wechatChargeConfigService.selectById(setting.getWechatChargeConfigId());
            WechatConfig config = wechatConfigService.selectByBrandId(shopDetail.getBrandId());
            Brand brand = brandService.selectByPrimaryKey(shopDetail.getBrandId());
            if (shopDetail.getWxServerId() == null){
                mchKey = config.getMchkey();
            }else{
                if(wechatChargeConfig == null){
                    WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
                    mchKey = wxServerConfig.getMchkey();
                }else{
                    WxServerConfig wxServerConfig = wxServerConfigService.selectById(wechatChargeConfig.getWxServerId());
                    mchKey = wxServerConfig.getMchkey();
                }
            }
            log.info("mchKey:" + mchKey);
            if (WeChatPayUtils.validSign(resultMap,mchKey)) {
                try {
                    String orderId = resultMap.get("out_trade_no");
                    ChargePayment cp = new ChargePayment();
                    cp.setId(ApplicationUtils.randomUUID());
                    cp.setCreateTime(new Date());
                    cp.setChargeOrderId(orderId);
                    cp.setPaymentMoney(new BigDecimal(resultMap.get("total_fee")).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                    cp.setPayData(new JSONObject(resultMap).toString());
                    chargeOrderService.chargeorderWxPaySuccess(cp);

                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", getCurrentCustomerId());
                    map.put("type", "UserAction");
                    map.put("content", "用户:"+getCurrentCustomer().getNickname()+"微信充值金额成功:"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc( map);
                } catch (Exception e) {
                    wxResult.put("result_code", "FALL");
                    wxResult.put("result_msg", e.toString());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", getCurrentCustomerId());
                    map.put("type", "UserAction");
                    map.put("content", "用户:"+getCurrentCustomer().getNickname()+"微信充值金额失败:"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }

            } else {
                wxResult.put("return_code", "FALL");
                wxResult.put("return_msg", "签名失败");
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", getCurrentCustomerId());
                map.put("type", "UserAction");
                map.put("content", "用户:"+getCurrentCustomer().getNickname()+"微信充值金额失败(签名失败):"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc( map);
            }
        }
        String wxResultXml = WeChatPayUtils.mapToXml(wxResult);
        log.info("charge result xml callback:" + wxResultXml);

        return wxResultXml;
    }


    //超级会员回调
    @RequestMapping("svip_chargepay_notify")
    @ResponseBody
    public String svip_chargepay_notify(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> resultMap = getResultMap(request);
        Map<String, String> wxResult = new HashMap<>();
        wxResult.put("return_code", "SUCCESS");
        if ("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))) {
//            log.info("充值回调:定位目标数据库，" + DataSourceTarget.getDataSourceName());
            String mchKey = "";
            log.info("shopId:" + resultMap.get("attach"));
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey( resultMap.get("attach"));
            BrandSetting setting = brandSettingService.selectByBrandId(shopDetail.getBrandId());
            WechatChargeConfig wechatChargeConfig = wechatChargeConfigService.selectById(setting.getWechatChargeConfigId());
            WechatConfig config = wechatConfigService.selectByBrandId(shopDetail.getBrandId());
            Brand brand = brandService.selectByPrimaryKey(shopDetail.getBrandId());
            if (shopDetail.getWxServerId() == null){
                mchKey = config.getMchkey();
            }else{
                if(wechatChargeConfig != null){
                    WxServerConfig wxServerConfig = wxServerConfigService.selectById(wechatChargeConfig.getWxServerId());
                    mchKey = wxServerConfig.getMchkey();
                }else{
                    WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
                    mchKey = wxServerConfig.getMchkey();
                }
            }
            log.info("mchKey:" + mchKey);
            if (WeChatPayUtils.validSign(resultMap,mchKey)) {
                try {
                    String orderId = resultMap.get("out_trade_no");
                    SvipChargeItem cp = new SvipChargeItem();
                    cp.setId(ApplicationUtils.randomUUID());
                    cp.setCreateTime(new Date());
                    cp.setChargeOrderId(orderId);
                    cp.setPaymentMoney(new BigDecimal(resultMap.get("total_fee")).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                    cp.setPayData(new JSONObject(resultMap).toString());
                    //更新订单状态和svip状态
                    svipChargeOrderService.chargeorderWxPaySuccess(cp);
/*                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", getCurrentCustomerId());
                    map.put("type", "UserAction");
                    map.put("content", "用户:"+getCurrentCustomer().getNickname()+"微信付费会员充值金额成功:"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc( map);*/
                } catch (Exception e) {
                    wxResult.put("result_code", "FALL");
                    wxResult.put("result_msg", e.toString());
//                    Map map = new HashMap(4);
//                    map.put("brandName", brand.getBrandName());
//                    map.put("fileName", getCurrentCustomerId());
//                    map.put("type", "UserAction");
//                    map.put("content", "用户:"+getCurrentCustomer().getNickname()+"微信付费会员充值金额失败:"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
//                    doPostAnsc(map);
                }

            } else {
                wxResult.put("return_code", "FALL");
                wxResult.put("return_msg", "签名失败");
//                Map map = new HashMap(4);
//                map.put("brandName", brand.getBrandName());
//                map.put("fileName", getCurrentCustomerId());
//                map.put("type", "UserAction");
//                map.put("content", "用户:"+getCurrentCustomer().getNickname()+"微信付费会员充值金额失败(签名失败):"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
//                doPostAnsc( map);
            }
        }
        String wxResultXml = WeChatPayUtils.mapToXml(wxResult);
        log.info("charge result xml callback:" + wxResultXml);

        return wxResultXml;
    }

    @RequestMapping("sum_of_money")
    @ResponseBody
    public String sum_of_money(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> resultMap = getResultMap(request);
        Map<String, String> wxResult = new HashMap<>();
        wxResult.put("return_code", "SUCCESS");
        if ("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))) {
//            log.info("充值回调:定位目标数据库，" + DataSourceTarget.getDataSourceName());
            WechatConfig config = getCurrentBrand().getWechatConfig();
            String mchKey = "";
            log.info("brandId:" + resultMap.get("attach"));
            BrandSetting setting = brandSettingService.selectByBrandId(resultMap.get("attach"));
            WechatChargeConfig wechatChargeConfig = wechatChargeConfigService.selectById(setting.getWechatChargeConfigId()); //得到充值的账户信息
            if (wechatChargeConfig.getWxServerId() == null){
                mchKey = config.getMchkey();
            }else{
                WxServerConfig wxServerConfig = wxServerConfigService.selectById(wechatChargeConfig.getWxServerId());
                mchKey = wxServerConfig.getMchkey();
            }
            log.info("mchKey:" + mchKey);
            if (WeChatPayUtils.validSign(resultMap,mchKey)) {
                try {
                    String orderId = resultMap.get("out_trade_no");
                    ChargeOrder co = chargeOrderService.selectById(orderId);

                    ChargePayment cp = new ChargePayment();
                    cp.setId(ApplicationUtils.randomUUID());
                    cp.setCreateTime(new Date());
                    cp.setChargeOrderId(orderId);
                    cp.setPaymentMoney(new BigDecimal(resultMap.get("total_fee")).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                    cp.setPayData(new JSONObject(resultMap).toString());
                    cp.setShopDetailId(co.getShopDetailId());
                    chargeOrderService.chargeorderWxPaySuccess(cp);
//                    Map map = new HashMap(4);
//                    map.put("brandName", getCurrentBrand().getBrandName());
//                    map.put("fileName", getCurrentCustomerId());
//                    map.put("type", "UserAction");
//                    map.put("content", "用户:"+getCurrentCustomer().getNickname()+"微信充值金额成功:"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
//                    doPostAnsc(url, map);
                } catch (Exception e) {
                    wxResult.put("result_code", "FALL");
                    wxResult.put("result_msg", e.toString());
//                    Map map = new HashMap(4);
//                    map.put("brandName", getCurrentBrand().getBrandName());
//                    map.put("fileName", getCurrentCustomerId());
//                    map.put("type", "UserAction");
//                    map.put("content", "用户:"+getCurrentCustomer().getNickname()+"微信充值金额失败:"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
//                    doPostAnsc(url, map);
                }

            } else {
                wxResult.put("return_code", "FALL");
                wxResult.put("return_msg", "签名失败");
                Map map = new HashMap(4);
                map.put("brandName", getCurrentBrand().getBrandName());
                map.put("fileName", getCurrentCustomerId());
                map.put("type", "UserAction");
                map.put("content", "用户:"+getCurrentCustomer().getNickname()+"微信充值金额失败(签名失败):"+resultMap.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            }
        }
        String wxResultXml = WeChatPayUtils.mapToXml(wxResult);
        log.info("charge result xml callback:" + wxResultXml);

        return wxResultXml;
    }

    private Map<String, String> getResultMap(HttpServletRequest request) throws IOException, DocumentException {
        InputStream input = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        StringBuffer xmlData = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            xmlData.append(line);
        }
        log.info("receive weixin pay nofity :" + xmlData);
        return WeChatPayUtils.xmlToMap(xmlData.toString());
    }

    @RequestMapping("/alipay")
    public void alipay(HttpServletResponse response, String orderId) throws Exception {
        //订单
        Order order = orderService.selectById(orderId);
        //品牌设置
        BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
        //店铺
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        //品牌
        Brand brand = brandService.selectByPrimaryKey(order.getBrandId());
        if (brandSetting.getAliPay() == Config.OPEN) {
            AliPayUtils.connection(StringUtils.isEmpty(shopDetail.getAliAppId()) ?  brandSetting.getAliAppId() : shopDetail.getAliAppId().trim() ,
                    StringUtils.isEmpty(shopDetail.getAliPrivateKey()) ?  brandSetting.getAliPrivateKey().trim() : shopDetail.getAliPrivateKey().trim(),
                    StringUtils.isEmpty(shopDetail.getAliPublicKey()) ?  brandSetting.getAliPublicKey().trim() : shopDetail.getAliPublicKey().trim(),
                    shopDetail.getAliEncrypt());
            Map map = new HashMap();


            StringBuilder msg = new StringBuilder();
            msg.append("订单编号:" + order.getSerialNumber() + "\n");
            msg.append("桌号：" + order.getTableNumber() + "\n");
            msg.append("店铺名：" + shopDetailService.selectById(order.getShopDetailId()).getName() + "\n");
            msg.append("订单时间：" + DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm") + "\n");
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            if (setting.getIsUseServicePrice() == 1 && shopDetail.getIsUseServicePrice() == 1) {
                msg.append(shopDetail.getServiceName() + "：" + order.getServicePrice() + "\n");
            }
            BigDecimal sum = order.getOrderMoney();
            List<Order> orders = orderService.selectByParentId(order.getId(),order.getPayType()); //得到子订单
            for (Order child : orders) { //遍历子订单
                sum = sum.add(child.getOrderMoney());
            }
            msg.append("订单明细：\n");
            Map<String, String> param = new HashMap<>();
            param.put("orderId", order.getId());
            List<OrderItem> orderItem = orderItemService.listByOrderId(param);
            for (OrderItem item : orderItem) {
                msg.append("  " + item.getArticleName() + "x" + item.getCount() + "\n");
            }
            msg.append("订单金额：" + sum + "\n");

            BigDecimal payBefore = orderService.selectPayBefore(orderId);
            if(order.getAmountWithChildren().doubleValue() > 0 && payBefore != null){
                order.setAmountWithChildren(order.getAmountWithChildren().subtract(payBefore));
            }


            //用户发起支付宝支付记录UserAction日志
            Map customerMap = new HashMap(4);
            customerMap.put("brandName", brand.getBrandName());
            customerMap.put("fileName", order.getCustomerId());
            customerMap.put("type", "UserAction");
            customerMap.put("content", "用户:"+order.getCustomerId()+"使用支付宝发起支付，订单Id:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc( customerMap);

            map.put("out_trade_no", orderId);
            map.put("total_amount",order.getAmountWithChildren().doubleValue() > 0 ? order.getAmountWithChildren() : order.getPaymentAmount());
//            map.put("subject",order.getId());
            map.put("subject",shopDetail.getName() + "---消费");
            map.put("seller_id", StringUtils.isEmpty(shopDetail.getAliSellerId()) ? brandSetting.getAliSellerId() : shopDetail.getAliSellerId());
            map.put("product_code", brandSetting.getAliProductCode());
            String notifyUrl = getBaseUrl() + "pay/alipay_notify";
            AliPayUtils.phonePay(response, map, null, notifyUrl);
        }


    }


    @RequestMapping("/alipay_notify")
    @ResponseBody
    public String alipay_notify(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException, AlipayApiException {

        request.setCharacterEncoding("UTF-8");
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map resultMap = (Map) mobile.getContent();
        log.info("支付宝异步信息:" + resultMap.toString());
        Order order = orderService.selectById(resultMap.get("out_trade_no").toString());
        BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
        Brand brand = brandService.selectByPrimaryKey(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        String aliPublicKey = StringUtils.isEmpty(shopDetail.getAliPrivateKey()) ? brandSetting.getAliPublicKey() : shopDetail.getAliPublicKey();
        String result = "success";
        if ("TRADE_SUCCESS".equals(resultMap.get("trade_status")) || "TRADE_FINISHED".equals(resultMap.get("trade_status"))) {
            log.info("支付宝回调:定位目标数据库，" + DataSourceTarget.getDataSourceName());
            log.info("请求字符集"+request.getCharacterEncoding());

            boolean signVerified = false;
            if(shopDetail.getAliEncrypt() == 0){
                signVerified = AlipaySignature.rsaCheckV1(resultMap, aliPublicKey, "UTF-8");
            }else if(shopDetail.getAliEncrypt() == 1){
                signVerified = true;
            }
//			调用SDK验证签名
            if (signVerified) {
                log.info("验签成功");
                try {
                    String orderId = String.valueOf(resultMap.get("out_trade_no"));
                    if (order == null) {
                        return "false";
                    }else{
                        String tradeNo = String.valueOf(resultMap.get("trade_no"));
                        OrderPaymentItem historyItem = orderPaymentItemService.selectById(tradeNo);
                        if(historyItem == null){
                            log.info("接收到支付宝支付通知:" + resultMap);
                            OrderPaymentItem item = new OrderPaymentItem();
                            item.setOrderId(orderId);
                            item.setId(tradeNo);
                            item.setPaymentModeId(PayMode.ALI_PAY);
                            item.setPayTime(new Date());
                            item.setPayValue(new BigDecimal(Double.valueOf(resultMap.get("total_amount").toString())).divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP));
                            item.setRemark("来自支付宝支付，支付金额为" + item.getPayValue() + "元" + " 订单为:" + item.getOrderId());
                            item.setResultData(new JSONObject(resultMap).toString());
                            JSONResult o = orderService.orderWxPaySuccess(item);
                            orderService.orderWxPaySuccessAspect(o);

                            Map orderMap = new HashMap(4);
                            orderMap.put("brandName", brand.getBrandName());
                            orderMap.put("fileName", order.getId());
                            orderMap.put("type", "orderAction");
                            orderMap.put("content", "订单:" +item.getOrderId()+"接收到支付宝支付回调，支付金额为" + item.getPayValue() + "元,请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(orderMap);
                            Map customerMap = new HashMap(4);
                            customerMap.put("brandName",brand.getBrandName());
                            customerMap.put("fileName", order.getCustomerId());
                            customerMap.put("type", "UserAction");
                            customerMap.put("content", "用户:"+order.getCustomerId()+"接收到支付宝支付回调，支付金额为" + item.getPayValue() + "元 订单为:" + item.getOrderId()+",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(customerMap);
                        }
                    }
                } catch (Exception e) {
                    result = "false";
                    log.error(e.getMessage());
                    Map orderMap = new HashMap(4);
                    orderMap.put("brandName", brand.getBrandName());
                    orderMap.put("fileName", order.getId());
                    orderMap.put("type", "orderAction");
                    orderMap.put("content", "订单:" +order.getId()+"接收到支付宝支付回调，保存订单支付信息失败: " + resultMap+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(orderMap);
                    Map customerMap = new HashMap(4);
                    customerMap.put("brandName", brand.getBrandName());
                    customerMap.put("fileName", order.getCustomerId());
                    customerMap.put("type", "UserAction");
                    customerMap.put("content", "用户:"+order.getCustomerId()+"接收到支付宝支付回调，保存订单支付信息失败: " + resultMap+" 订单为:" + order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(customerMap);
                }
            } else {
                result = "false";
                log.info("验签失败");
                Map orderMap = new HashMap(4);
                orderMap.put("brandName", brand.getBrandName());
                orderMap.put("fileName", order.getId());
                orderMap.put("type", "orderAction");
                orderMap.put("content", "订单:" +order.getId()+"接收到支付宝支付回调，保存订单支付信息失败: 验签失败,请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(orderMap);
                Map customerMap = new HashMap(4);
                customerMap.put("brandName", brand.getBrandName());
                customerMap.put("fileName", order.getCustomerId());
                customerMap.put("type", "UserAction");
                customerMap.put("content", "用户:"+order.getCustomerId()+"接收到支付宝支付回调，保存订单支付信息失败: 验签失败 订单为:" + order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(customerMap);
            }
        } else {
            result = "false";
        }
        return result;
    }


    @RequestMapping("/new/aliPayOrder")
    public void aliPayOrder(HttpServletRequest request, HttpServletResponse response,String tableNumber,String shopId){
        AppUtils.unpack(request, response);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();

        ShopDetail shopDetail = shopDetailService.selectById(shopId);
        if(shopDetail.getAllowFirstPay() == 0){
            json.put("code",100);
            json.put("message","先付门店不允许使用此功能！");
            ApiUtils.setBackInfo(response, json);
            return;
        }
        DataSourceTarget.setDataSourceName(shopDetail.getBrandId());

        log.debug("获取支付宝订单-------------------------");
        Order order =  orderService.getLastOrderByTableNumber(tableNumber,shopId);
        if(order == null){
            json.put("code",100);
            json.put("message","未找到满足条件的订单");
        }else{
            //获取订单信息
            Order orderInfo = orderService.getOrderInfo(order.getId());
            //获取子订单信息
            List<Order> childs = orderService.getChildItem(order.getId());
            if(!CollectionUtils.isEmpty(childs)){
                for(Order child : childs){
                    //获得菜品信息
                    List<OrderItem> item = child.getOrderItems();
                    orderInfo.getOrderItems().addAll(item);
                    orderInfo.setMealAllNumber(orderInfo.getMealAllNumber() + child.getMealAllNumber());
                }
            }
            BigDecimal payBefore = orderService.selectPayBefore(order.getId());
            if(payBefore != null){
                if(orderInfo.getAmountWithChildren().doubleValue() > 0){
                    orderInfo.setAmountWithChildren(orderInfo.getAmountWithChildren().subtract(payBefore));
                }
            }

            BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
            json.put("shopName",shopDetail.getName());
            json.put("shopDetail",shopDetail);
            json.put("order",orderInfo);
            json.put("code",200);
            json.put("brandSetting",brandSetting);
            json.put("message","请求成功");
        }
        ApiUtils.setBackInfo(response, json);
    }


    @RequestMapping("/closeOrder")
    @ResponseBody
    public Result closeOrder(String orderId) throws UnknownHostException, DocumentException {
        String out_trade_no = orderId;
        WechatConfig config = getCurrentBrand().getWechatConfig();
        Map<String, String> apiReqeust = WeChatPayUtils.closeJSAPIPayRequest(out_trade_no, config.getAppid(), config.getMchid(), config.getMchkey());
        log.info("获取关闭订单返回结果成功:" + apiReqeust);
        JSONResult<Map<String, String>> result = new JSONResult<>();
        if (apiReqeust.containsKey("ERROR")) {
            result.setMessage(apiReqeust.get("ERROR"));
            log.error(result.getMessage());
            result.setSuccess(false);
        } else {
            result.setSuccess(true);
            result.setData(apiReqeust);
        }
        return result;
    }


    @RequestMapping("/fixedRefund")
    public String fixedRefund(String brandId,String shopId,int total,int refund,String transaction_id,String mchid,String id){
        DataSourceTarget.setDataSourceName(brandId);
        String result = orderService.fixedRefund(brandId,shopId,total,refund,transaction_id,mchid,id);
        return result;
    }


    @RequestMapping("/aliPayRefund")
    public void fixedRefundsss(String brandId, String orderId, BigDecimal money){
        DataSourceTarget.setDataSourceName(brandId);
        orderService.alipayRefund(orderId,money);
    }

    @RequestMapping("/okok")
    public String okok(){
        return "ok";
    }

    @RequestMapping("/okokok")
    public String okokok(String brandId){
        DataSourceTarget.setDataSourceName(brandId);
        Brand brand = brandService.selectByPrimaryKey(brandId);
        return brand.getBrandName();
    }

    @RequestMapping("/releaseTableNumber")
    public void releaseTableNumber(String shopId, String tableNumber){
        redisService.remove(shopId + tableNumber + "status");
    }


}
