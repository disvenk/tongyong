package com.resto.pos.web.controller;

import com.alibaba.fastjson.JSON;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.PlatformKey;
import com.resto.brand.core.meituanUtils.MeiTuanUtil;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.dto.MeiTuanOrderDto;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.pos.web.rpcinterceptors.DataSourceTarget;
import com.resto.pos.web.websocket.SystemWebSocketHandler;
import com.resto.shop.web.model.OrderPaymentItem;
import com.resto.shop.web.model.PlatformOrder;
import com.resto.shop.web.service.OrderPaymentItemService;
import com.resto.shop.web.service.PlatformOrderService;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 美团 业务 Controller
 * 文档：http://developer.meituan.com/openapi
 * 1: 接入团购&闪惠业务    2: 接入外卖业务
 * <p>
 * 【统一实现接口】
 * 1、门店映射（https://open-erp.meituan.com/storemap?developerId=100562&businessId=1&ePoiId=db8822e4195d43728cfc0fc9478eed51&signKey=x0ur5ae3gigj3l7h&callbackUrl=https://mrlmx.localtunnel.me/pos/meituan/shanhuicallback）
 * 2、云端心跳回调URL     eg：https://mrlmx.localtunnel.me/pos/meituan/heartbeat
 * 3、心跳上报接口（pos自行实现）
 * <p>
 * 【闪惠】接入步骤
 * 1、Resto+Shop后台  设置大众点评的店铺ID
 * 2、（由于业务逻辑改动已废弃 于2017年3月21日 14:37:30）美团商户后台设置      闪惠买单回调地址    eg：https://mrlmx.localtunnel.me/pos/meituan/shanhuiNewOrder
 * <p>
 * 【外卖】接入步骤
 * 1、美团商户后台设置      订单已确认的回调URL	eg：https://mrlmx.localtunnel.me/pos/meituan/waimaiConfirmOrder
 * <p>
 * Created by Lmx on 2017/3/14.
 */
@Controller
@RequestMapping("/meituan")
public class MeiTuanController extends GenericController {

    @Resource
    private OrderPaymentItemService orderPaymentItemService;
    @Resource
    private ShopDetailService shopDetailService;
    @Resource
    private PlatformOrderService platformOrderService;


    /**
     * 门店映射 回调地址
     *
     * @param ePoiId
     * @param appAuthToken
     * @return
     */
    @RequestMapping("/shanhuicallback")
    @ResponseBody
    public String shanhuiCallBack(String ePoiId, String appAuthToken) {
        ShopDetail shopDetail = new ShopDetail();
        shopDetail.setId(ePoiId);
        shopDetail.setDazhongAppAuthToken(appAuthToken);
        shopDetailService.update(shopDetail);
        System.out.println("ePoiId：" + ePoiId);
        System.out.println("appAuthToken：" + appAuthToken);
        return "{\"data\":\"success\"}";
    }

    /**
     * 云端心跳回调URL
     *
     * @return
     */
    @RequestMapping("/heartbeat")
    @ResponseBody
    public String heartbeat() {
//        System.out.println("heartbeat~~~");
        return "{\"data\":\"OK\"}";
    }


    /**
     * Pos端主动向美团上报程序运行情况
     *
     * @return
     */
    @RequestMapping("/reportHeartbeat")
    @ResponseBody
    public Result reportHeartbeat() {
        //ERP厂商门店id     （以  resto 店铺ID为准）
        String ePoiId = getCurrentShopId();
        //终端唯一标识        （以当前服务器 IP 为准）
        String posId = getLocalIP();
        MeiTuanUtil.reportHeartbeat(ePoiId, posId);
        return getSuccessResult();
    }

    /**
     * 【闪惠】 新订单推送接口
     * http://developer.meituan.com/openapi#6.6
     *
     * @return
     */
    @RequestMapping("/shanhuiNewOrder")
    @ResponseBody
    public String shanhuiNewOrder(String developerId, String params) {
        //好像没有什么用了~~ 2017年3月21日 14:46:29

        System.out.println(developerId);
        System.out.println(params);

        return "{\"data\":\"OK\"}";
    }



    /**
     * 【闪惠】 订单退款
     *
     * @param shanhuiOrderId
     * @return
     */
    @RequestMapping("/shanhuiOrderRefund")
    @ResponseBody
    public Result shanhuiOrderRefund(String shanhuiOrderId) {
        return getSuccessResult(MeiTuanUtil.shanhuiOrderRefund(shanhuiOrderId, getCurrentShop().getDazhongAppAuthToken()));
    }

    /**
     * 【闪惠】 根据闪惠订单ID查询订单信息   校验
     *
     * @param orderId        Resto订单ID
     * @param shanhuiOrderId 大众点评订单ID
     * @return
     */
    @RequestMapping(value = "/checkShanHuiOrder", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String checkShanHuiOrder(String orderId, String shanhuiOrderId) {
        JSONObject result = new JSONObject(MeiTuanUtil.shanhuiOrderQueryById(shanhuiOrderId, getCurrentShop().getDazhongAppAuthToken()));
        if (result.has("data")) {
            JSONObject data = result.getJSONObject("data");
            OrderPaymentItem paymentItem = orderPaymentItemService.selectByShanhuiPayOrder(orderId);
            result.put("checkVal", paymentItem.getPayValue().compareTo(data.getBigDecimal("originalAmount")) == 0 ? true : false);
            result.put("payValue", paymentItem.getPayValue());
        }
        return result.toString();
    }


    /**
     * 确认支付
     *
     * @param orderId        Resto订单ID
     * @param shanhuiOrderId 闪惠订单ID
     * @return
     */
    @RequestMapping("/saveShanhuiOrder")
    @ResponseBody
    public Result saveShanhuiOrder(String orderId, String shanhuiOrderId) {
        String appAuthToken = getCurrentShop().getDazhongAppAuthToken();
        JSONObject shanhuiResult = new JSONObject(MeiTuanUtil.shanhuiOrderQueryById(shanhuiOrderId, appAuthToken));
        if (shanhuiResult.has("error")) {
            return new Result("请输入有效的大众点评订单号~", false);
        }
        JSONObject shanhuiObj = shanhuiResult.getJSONObject("data");
        if (shanhuiObj.has("vendorOrderId") && StringUtils.isNotEmpty(shanhuiObj.getString("vendorOrderId"))) {
            return new Result("订单已被消费", false);
        }
        //确认支付，并绑定Resot订单ID 和 大众点评订单ID
        MeiTuanUtil.shanhuiOrderConfirm(orderId, shanhuiObj.getString("dpOrderCode"), appAuthToken);
        orderPaymentItemService.updateByShanhuiPayOrder(orderId, shanhuiResult.toString());
        return getSuccessResult();
    }

    /**
     * 【外卖新订单】
     * 实际上线，不需要配置此回调链接，测试开发时使用 （需要通过此方法确认订单）。
     *
     * @return
     */
    @RequestMapping("/waimaiNewOrder")
    @ResponseBody
    public String waimaiNewOrder(String developerId, String ePoiId, String sign, String order) {
        System.out.println("\n\n新订单推送.....");
        System.out.println(sign);
        System.out.println(ePoiId);
        System.out.println(order);
        System.out.println(developerId);
        JSONObject orderInfo = new JSONObject(order);
        System.out.println("确认订单：" + MeiTuanUtil.waimaiOrderConfirm(MeiTuanUtil.testAppAuthToken, orderInfo.get("orderId").toString()));
        return "{\"data\":\"OK\"}";
    }


    /**
     * 【外卖确认订单】
     * 订单可能被推送多次，需判断一下，是否已被推送
     *
     * @return
     */
    @RequestMapping("/waimaiConfirmOrder")
    @ResponseBody
    public String waimaiConfirmOrder(String developerId, String ePoiId, String sign, String order) {
//        ePoiId = "f48a0a35e0be4dd8aaeb7cf727603958";
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(ePoiId);
        DataSourceTarget.setDataSourceName(shopDetail.getBrandId());
        MeiTuanOrderDto meiTuanOrderDto = JSON.parseObject(order, MeiTuanOrderDto.class);
        String meituanOrderId = meiTuanOrderDto.getOrderId();
        log.info("\n【美团】确认订单推送.....     developerId：" + developerId + "     ePoiId：" + ePoiId + "   orderId：" + meituanOrderId);
        PlatformOrder platformOrder = platformOrderService.selectByPlatformOrderId(meituanOrderId, PlatformKey.MEITUAN);
        if (platformOrder == null) {//如果数据库不存在此单，则插入此单
            meiTuanOrderDto.setSourceText(order);
            platformOrderService.meituanNewOrder(meiTuanOrderDto);
            try {
                SystemWebSocketHandler.sendPlatformOrderToPos(ePoiId, meituanOrderId, PlatformKey.MEITUAN);
            } catch (IOException e) {
                e.printStackTrace();
                log.info("\n【美团订单】发送至Pos端失败：orderId：" + meituanOrderId);
            }
        } else {
            log.info("\n【美团外卖】订单已存在 orderId：" + meituanOrderId);
        }
        return "{\"data\":\"OK\"}";
    }

    /**
     * 【测试】
     * 【外卖确认订单】
     *
     * @return
     */
    @RequestMapping("/testwaimaiConfirmOrder")
    @ResponseBody
    public String testwaimaiConfirmOrder() throws IOException {
        String ePoiId = "f48a0a35e0be4dd8aaeb7cf727603958";
        String order = "{\"avgSendTime\":2400.0,\"caution\":\" 别放辣 快点送\",\"cityId\":999999,\"ctime\":1490337549,\"daySeq\":\"7\",\"deliveryTime\":0,\"detail\":\"[{\\\"app_food_code\\\":\\\"1000\\\",\\\"box_num\\\":1,\\\"box_price\\\":1,\\\"food_discount\\\":1,\\\"food_name\\\":\\\"片鸭\\\",\\\"food_property\\\":\\\"\\\",\\\"price\\\":13,\\\"quantity\\\":1,\\\"sku_id\\\":\\\"1000_2\\\",\\\"spec\\\":\\\"小份\\\",\\\"unit\\\":\\\"份\\\"},{\\\"app_food_code\\\":\\\"1000\\\",\\\"box_num\\\":1,\\\"box_price\\\":1,\\\"food_discount\\\":1,\\\"food_name\\\":\\\"片鸭\\\",\\\"food_property\\\":\\\"\\\",\\\"price\\\":10,\\\"quantity\\\":1,\\\"sku_id\\\":\\\"1000_1\\\",\\\"spec\\\":\\\"大份\\\",\\\"unit\\\":\\\"份\\\"}]\",\"dinnersNumber\":0,\"ePoiId\":\"f48a0a35e0be4dd8aaeb7cf727603958\",\"extras\":\"[{}]\",\"hasInvoiced\":1,\"invoiceTitle\":\"上海餐加企业咨询管理\",\"isFavorites\":false,\"isPoiFirstOrder\":false,\"isThirdShipping\":0,\"latitude\":31.143873,\"logisticsCode\":\"0000\",\"longitude\":97.175621,\"orderId\":3635947282,\"orderIdView\":16651803006773950,\"originalPrice\":34.0,\"payType\":1,\"poiAddress\":\"南极洲04号站\",\"poiFirstOrder\":false,\"poiId\":1665180,\"poiName\":\"kfpttest_zl11_17\",\"poiPhone\":\"4009208801\",\"poiReceiveDetail\":\"{\\\"actOrderChargeByMt\\\":[],\\\"actOrderChargeByMtIterator\\\":{},\\\"actOrderChargeByMtSize\\\":0,\\\"actOrderChargeByPoi\\\":[],\\\"actOrderChargeByPoiIterator\\\":{\\\"$ref\\\":\\\"$.actOrderChargeByMtIterator\\\"},\\\"actOrderChargeByPoiSize\\\":0,\\\"foodShareFeeChargeByPoi\\\":0,\\\"logisticsFee\\\":900,\\\"onlinePayment\\\":3400,\\\"setActOrderChargeByMt\\\":true,\\\"setActOrderChargeByPoi\\\":true,\\\"setFoodShareFeeChargeByPoi\\\":true,\\\"setLogisticsFee\\\":true,\\\"setOnlinePayment\\\":true,\\\"setWmPoiReceiveCent\\\":true,\\\"wmPoiReceiveCent\\\":3400}\",\"recipientAddress\":\"西藏昌都市气象局 (300)@#西藏自治区昌都市卡若区城关镇林廓路286号\",\"recipientName\":\"李先生 (先生)\",\"recipientPhone\":\"13627626221\",\"shipperPhone\":\"\",\"shippingFee\":9.0,\"status\":2,\"total\":34.0,\"utime\":1490337549}";
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(ePoiId);
        DataSourceTarget.setDataSourceName(shopDetail.getBrandId());
        MeiTuanOrderDto meiTuanOrderDto = JSON.parseObject(order, MeiTuanOrderDto.class);
        String meituanOrderId = meiTuanOrderDto.getOrderId();
        PlatformOrder platformOrder = platformOrderService.selectByPlatformOrderId(meituanOrderId, PlatformKey.MEITUAN);
        if (platformOrder == null) {//如果数据库不存在此单，则插入此单
            meiTuanOrderDto.setSourceText(order);
            platformOrderService.meituanNewOrder(meiTuanOrderDto);
            SystemWebSocketHandler.sendPlatformOrderToPos(ePoiId, meituanOrderId, PlatformKey.MEITUAN);
        } else {
            log.info("\n【美团外卖】订单已存在 orderId：" + meituanOrderId);
        }
        return "{\"data\":\"OK\"}";
    }


}
