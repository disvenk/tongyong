package com.resto.shop.web.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.util.*;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.*;
import com.resto.shop.web.dao.OrderPaymentItemMapper;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.*;
import com.resto.shop.web.producer.MQMessageProducer;
import com.resto.shop.web.producer.MQTTMQMessageProducer;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.DateTimeUtils;
import com.resto.shop.web.util.LogTemplateUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@Component
@Aspect
public class OrderAspect {

    Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    ShopCartService shopCartService;
    @Resource
    CustomerService customerService;
    @Resource
    WechatConfigService wechatConfigService;
    @Resource
    BrandSettingService brandSettingService;
    @Autowired
    OrderItemService orderItemService;
    @Resource
    ShopDetailService shopDetailService;
    @Resource
    ShareSettingService shareSettingService;
    @Resource
    OrderService orderService;
    @Resource
    NewCustomCouponService newcustomcouponService;
    @Resource
    BrandService brandService;
    @Resource
    AccountService accountService;
    @Resource
    AccountLogService accountLogService;
    @Resource
    TemplateService templateService;
    @Resource
    SmsLogService smsLogService;
    @Resource
    ParticipantService participantService;
    @Autowired
    OrderBeforeService orderBeforeService;
    @Autowired
    private BrandTemplateEditService brandTemplateEditService;
    @Autowired
    private OrderPaymentItemMapper orderPaymentItemMapper;


    @Autowired
    RedisService redisService;

    @Autowired
    MQMessageProducer mqMessageProducer;

    @Autowired
    MQTTMQMessageProducer mqttMQMessageProducer;

    @Autowired
    WeChatService weChatService;

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.createOrder(..))")
    public void createOrder() {
    }


    @Pointcut("execution(* com.resto.shop.web.service.OrderService.createOrderByEmployee(..))")
    public void createOrderByEmployee() {
    }


    @AfterReturning(value = "createOrderByEmployee()", returning = "jsonResult")
    public void createOrderByEmployeeAround(JSONResult jsonResult) throws Throwable {
        if (jsonResult.isSuccess() == true) {
            Order order = (Order) jsonResult.getData();
            shopCartService.clearShopCartGeekPos(String.valueOf(order.getEmployeeId()), order.getShopDetailId());
            //出单时减少库存
            Boolean updateStockSuccess = false;
            updateStockSuccess = orderService.updateStock(orderService.getOrderInfo(order.getId()));
            if (!updateStockSuccess) {
                log.info("库存变更失败:" + order.getId());
            }
        }
    }

    @AfterReturning(value = "createOrder()", returning = "jsonResult")
    public void createOrderAround(JSONResult jsonResult) throws Throwable {
        if(!jsonResult.isSuccess()){
            return;
        }
        Order order = (Order) jsonResult.getData();
        //gigtPush(order);
        //新版推送订单至POS
        pushCreateOrderToPos(order.getId());
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        if (order.getPayMode() != OrderPayMode.ALI_PAY) { //已支付
            Customer customer = customerService.selectById(order.getCustomerId());
            if(customer != null) {
                if(order.getOrderState()==2){
                    WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                    ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                    List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM414174151");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM414174151", TemplateSytle.TICKET);

                    if (templateFlowList != null && templateFlowList.size() != 0) {
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String baseUrl = UrlUtils.getIP(URI.create(setting.getWechatWelcomeUrl())).toString();
                        Integer ticketType = 0;
                        if(setting.getOpenTicket()==1){
                             ticketType = 1;
                        }
                        if(setting.getOpenTicket()==2){

                        }
                        String jumpUrl = baseUrl+"/restowechat/src/views/invoice.html?baseUrl="+baseUrl+"&invoiceType="+2+"&shopId="+shop.getId()+"&customerId="+customer.getId()+"&ticketType="+ticketType;

                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();
                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                first.put("value", brandTemplateEdit.getStartSign());
                            }else {
                                first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                            }
                        }else {
                            first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                        }
                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", shop.getName());
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", "餐饮");
                        keyword2.put("color", "#000000");
                        Map<String, Object> keyword3 = new HashMap<String, Object>();
                        BigDecimal money = orderPaymentItemMapper.selectEnableTicketMoney(order.getId());
                        if(money == null){
                            setting.setOpenTicket(0);
                            money = new BigDecimal(0);
                        }
                        keyword3.put("value", money.toString()+"(可开票金额)");
                        keyword3.put("color", "#000000");
                        Map<String, Object> keyword4 = new HashMap<String, Object>();
                        keyword4.put("value", "30天");
                        keyword4.put("color", "#000000");

                        Map<String, Object> remark = new HashMap<String, Object>();

                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                remark.put("value", brandTemplateEdit.getEndSign());
                            }else {
                                remark.put("value", "请点击详情进入订单列表开具发票");
                            }
                        }else {
                            remark.put("value", "请点击详情进入订单列表开具发票");
                        }

                        remark.put("color", "#173177");

                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("keyword3", keyword3);
                        content.put("keyword4", keyword4);
                        content.put("remark", remark);
                        //String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                        Brand brand = brandService.selectById(order.getBrandId());
                        if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                            if(setting.getOpenTicket()!=0){
                                weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", customer.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if (setting.getMessageSwitch() == 1) {
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shop.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            }

                        } else {
                            String orderId = order.getId();
                            if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
                                orderId = order.getParentOrderId();
                            }
                            List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                            for (Participant p : participants) {
                                if(setting.getOpenTicket()!=0){
                                    Customer c = customerService.selectById(p.getCustomerId());
                                    weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                    Map map = new HashMap(4);
                                    map.put("brandName", brand.getBrandName());
                                    map.put("fileName", c.getId());
                                    map.put("type", "UserAction");
                                    map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                    doPostAnsc(map);
                                    //发送短信
                                    if (setting.getMessageSwitch() == 1) {
                                        com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                        smsParam.put("key1", order.getSerialNumber());
                                        smsParam.put("key2", shop.getName());
                                        smsParam.put("key3", order.getTableNumber());
                                        com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                    }
                                }

                            }
                        }
                    } else {
                        Brand brand = brandService.selectById(order.getBrandId());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
                }

            }
        }

        log.error("哈哈哈哈哈哈哈----------");
        String time = DateUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss");
        if (jsonResult.isSuccess() == true) {

            //Order order = (Order) jsonResult.getData();
            if (order.getOrderBefore() != null) {
                OrderBefore orderBefore = new OrderBefore();
                orderBefore.setTableNumber(order.getTableNumber());
                orderBefore.setCustomerId(order.getCustomerId());
                orderBefore.setShopDetailId(order.getShopDetailId());
                orderBefore.setOrderId(order.getId());
                orderBeforeService.insert(orderBefore);
            }
            if (!org.apache.commons.lang3.StringUtils.isEmpty(order.getGroupId()) && order.getOrderState() == OrderState.PAYMENT) {
                //如果是多人点餐已支付

                redisService.remove(order.getShopDetailId() + order.getGroupId());
            }
            log.info("(createOrderAround)创建订单时候订单状态为：orderstate：" + order.getOrderState() + "production：" + order.getProductionStatus() + "订单id：" + order.getId() + "当前时间为：" + time);
            if (order.getCustomerId().equals("0")) {
                //pos端点餐
                mqMessageProducer.sendPlaceOrderMessage(order);
                String shopId = order.getShopDetailId();
                Integer orderCount = (Integer) redisService.get(shopId + "shopOrderCount");
                BigDecimal orderTotal = (BigDecimal) redisService.get(shopId + "shopOrderTotal");
                if (orderCount == null) {
                    orderCount = 0;
                }
                if (orderTotal == null) {
                    orderTotal = BigDecimal.valueOf(0);
                }
                orderCount++;
                orderTotal = orderTotal.add(order.getOrderMoney());
                redisService.set(shopId + "shopOrderCount", orderCount);
                redisService.set(shopId + "shopOrderTotal", orderTotal);
                mqMessageProducer.sendPrintSuccess(shopId);
                pushOrderMoneyAndCountToPos(orderCount, orderTotal, order.getShopDetailId());
                return;
            }
            if (!StringUtils.isEmpty(order.getBeforeId()) && order.getOrderState() == OrderState.PAYMENT) {
                orderBeforeService.updateState(order.getBeforeId(), 1);
                Order before = orderService.selectById(order.getBeforeId());
                before.setOrderState(OrderState.CANCEL);
                orderService.update(before);
                Map<String, String> param = new HashMap<>();
                param.put("orderId", order.getBeforeId());
                List<OrderItem> orderItems = orderItemService.listByOrderId(param);
                if (!CollectionUtils.isEmpty(orderItems)) {
                    for (OrderItem orderItem : orderItems) {
                        orderItem.setOrderId(order.getId());
                        orderItem.setStatus(2);
                        orderItemService.update(orderItem);
                    }
                }
            }
            if (order.getPayMode() != PayMode.WEIXIN_PAY && !StringUtils.isEmpty(order.getGroupId())) {
                //如果多人买的 一起清空购物车
                shopCartService.deleteByGroup(order.getGroupId());
            }


            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
            if (order.getPayMode() != PayMode.WEIXIN_PAY && StringUtils.isEmpty(order.getGroupId())) {
                shopCartService.clearShopCart(order.getCustomerId(), order.getShopDetailId());
            }
//            现金银联新美大支付应该在pos上确认订单已收款后在进行出单
            if (order.getPayMode() == OrderPayMode.YL_PAY || order.getPayMode() == OrderPayMode.XJ_PAY || order.getPayMode() == OrderPayMode.SHH_PAY || order.getPayMode() == OrderPayMode.JF_PAY) {

                mqMessageProducer.sendPlaceOrderNoPayMessage(order);
            }

            if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY) {
//                shopCartService.clearShopCart(order.getCustomerId(), order.getShopDetailId());
                mqMessageProducer.sendPlaceOrderMessage(order);
            }
            if (order.getDistributionModeId() == 2 && order.getOrderState() == OrderState.PAYMENT) {
                //BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
                mqMessageProducer.sendPlatformOrderMessage(order.getId(), PlatformType.R_VERSION, order.getBrandId(), order.getShopDetailId());
                mqMessageProducer.sendAutoConfirmOrder(order, setting.getAutoConfirmTime() * 1000);
            }

            //自动取消订单，大boss模式下  先付2小时未付款 自动取消订单
//            if (order.getOrderState().equals(OrderState.SUBMIT) && order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.PAY) {//未支付和未完全支付的订单，不包括后付款模式
//                long delay = 1000*60*60*2;//两个小时后自动取消订单
//                mqMessageProducer.sendAutoCloseMsg(order.getId(),order.getBrandId(),delay);
//            }

            //出单时减少库存
            Boolean updateStockSuccess = false;
            updateStockSuccess = orderService.updateStock(orderService.getOrderInfo(order.getId()));
            if (!updateStockSuccess) {
                log.info("库存变更失败:" + order.getId());
            }
        }
    }


    private void pushOrderMoneyAndCountToPos(Integer orderCount, BigDecimal orderMoney, String shopId){
        JSONObject data = new JSONObject();
        data.put("orderCount", orderCount);
        data.put("orderTotal", orderMoney);
        data.put("dataType", "money");
        data.put("shopId", shopId);
        mqttMQMessageProducer.sendOrderCompletedToPosMessage(data.toString());
    }

    /**
     * 采用第三方推送订单信息
     * @param orderId
     */
    private void pushCreateOrderToPos(String orderId){
        Order pushOrder = orderService.selectById(orderId);
        JSONObject orderObj = new JSONObject(JSON.toJSONString(pushOrder));
        orderObj.put("shopId", pushOrder.getShopDetailId());
        if (pushOrder.getDistributionModeId().equals(DistributionType.DELIVERY_MODE_ID)) {
            //R+外卖订单
            if (pushOrder.getOrderState().equals(OrderState.PAYMENT)) {
                orderObj.put("dataType", "platform");
                orderObj.put("type", PlatformType.R_VERSION);
                mqttMQMessageProducer.sendPlatformOrderMessage(orderObj.toString());
            }
        }else{
            //堂吃、外带订单
            orderObj.put("dataType", "current");
            if (pushOrder.getOrderMode().equals(ShopMode.BOSS_ORDER) && pushOrder.getPayType().equals(PayType.NOPAY)) {
                //后付直接推送订单信息
                mqttMQMessageProducer.sendCreateOrderToPosMessage(orderObj.toString());
            }else{
                //先付、电视叫号、美食广场
                if (pushOrder.getOrderState().equals(OrderState.PAYMENT) && !pushOrder.getOrderMode().equals(ShopMode.MANUAL_ORDER)) {
                    //订单已支付，执行打单
                    mqttMQMessageProducer.sendCreateOrderToPosMessage(orderObj.toString());
                    //修改菜品实际金额
                    orderItemService.updateOrderItemActualAmount(pushOrder, false);
                } else if (pushOrder.getOrderState().equals(OrderState.PAYMENT) && pushOrder.getOrderMode().equals(ShopMode.MANUAL_ORDER)
                        && StringUtils.isNotBlank(pushOrder.getParentOrderId())) {
                    //订单已支付，执行打单
                    mqttMQMessageProducer.sendCreateOrderToPosMessage(orderObj.toString());
                    //修改菜品实际金额
                    orderItemService.updateOrderItemActualAmount(pushOrder, false);
                }
                if (!pushOrder.getCustomerId().equalsIgnoreCase("0") && (pushOrder.getPayMode() == OrderPayMode.YL_PAY || pushOrder.getPayMode() == OrderPayMode.XJ_PAY
                        || pushOrder.getPayMode() == OrderPayMode.SHH_PAY || pushOrder.getPayMode() == OrderPayMode.JF_PAY)) {
                    //使用了现金、银联、积分、闪惠此时还是未支付
                    orderObj.put("dataType", "confirm");
                    mqttMQMessageProducer.sendConfirmOrderToPosMessage(orderObj.toString());
                }
            }
        }
    }

    /**
     * 先付模式下POS端确认支付后执行打单
     * @param orderId
     */
    private void pushConfirmOrderToPos(String orderId){
        Order pushOrder = orderService.selectById(orderId);
        JSONObject orderObj = new JSONObject(JSON.toJSONString(pushOrder));
        orderObj.put("dataType", "confirm");
        orderObj.put("shopId", pushOrder.getShopDetailId());
        if (pushOrder.getPayType().equals(PayType.PAY)) {
            mqttMQMessageProducer.sendConfirmOrderToPosMessage(orderObj.toString());
        }
        //修改菜品实际金额
        orderItemService.updateOrderItemActualAmount(pushOrder,false);
    }

    private void sendPaySuccessMsg(Order order) {
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        if (setting.getTemplateEdition() == 0) {
            Customer customer = customerService.selectById(order.getCustomerId());
            if (customer == null) {
                return;
            }
            WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            StringBuffer msg = new StringBuffer();
            msg.append("订单编号:\n" + order.getSerialNumber() + "\n");
            if (order.getOrderMode() != null) {
                switch (order.getOrderMode()) {
                    case ShopMode.TABLE_MODE:
                        msg.append("桌号:" + order.getTableNumber() + "\n");
                        break;
                    case ShopMode.BOSS_ORDER:
                        msg.append("桌号:" + order.getTableNumber() + "\n");
                        break;
                    default:
                        msg.append("消费码：" + order.getVerCode() + "\n");
                        break;
                }
            }
            Brand brand = brandService.selectById(order.getBrandId());
            ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
            if (order.getShopName() == null || "".equals(order.getShopName())) {
                order.setShopName(shopDetail.getName());
            }
            msg.append("店铺名：" + order.getShopName() + "\n");
            msg.append("订单时间：" + DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm") + "\n");

            //BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            if (order.getServicePrice().compareTo(BigDecimal.ZERO) > 0) { //如果产生了服务费
                if (order.getIsUseNewService().equals(Common.YES)){ //如果开通了新版服务费
                    if (order.getSauceFeeCount() != null && order.getSauceFeeCount() > 0){ //产生了餐具费
                        msg.append(shopDetail.getSauceFeeName() + "：" + order.getSauceFeePrice() + "\n");
                    }
                    if (order.getTowelFeeCount() != null && order.getTowelFeeCount() > 0){ //产生了纸巾费
                        msg.append(shopDetail.getTowelFeeName() + "：" + order.getTowelFeePrice() + "\n");
                    }
                    if (order.getTablewareFeeCount() != null && order.getTablewareFeeCount() > 0){ //产生了酱料费
                        msg.append(shopDetail.getTablewareFeeName() + "：" + order.getTablewareFeePrice() + "\n");
                    }
                }else { //旧版服务费
                    msg.append(shopDetail.getServiceName() + "：" + order.getServicePrice() + "\n");
                }
            }
            if (setting.getIsMealFee() == 1 && order.getMealFeePrice().compareTo(BigDecimal.ZERO) != 0 && order.getDistributionModeId() == 3 && shopDetail.getIsMealFee() == 1) {
                msg.append(shopDetail.getMealFeeName() + "：" + order.getMealFeePrice() + "\n");
            }
            msg.append("订单明细：\n");
            Map<String, String> param = new HashMap<>();
            param.put("orderId", order.getId());
            List<OrderItem> orderItem = orderItemService.listByOrderId(param);
            for (OrderItem item : orderItem) {
                msg.append("  " + item.getArticleName() + "x" + item.getCount() + "\n");
            }
            msg.append("订单金额：" + order.getOrderMoney() + "\n");
            if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getOrderBefore() == null) {
                String url = "";
                if (order.getParentOrderId() == null) {
                    url = setting.getWechatWelcomeUrl() + "?orderBossId=" + order.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                } else {
                    Order o = orderService.selectById(order.getParentOrderId());
                    url = setting.getWechatWelcomeUrl() + "?orderBossId=" + o.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                }
                msg.append("<a href='" + url + "'>点击这里进行\"加菜\"或\"买单\"</a> \n");
            }
            if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                String result = weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            } else {
                sendToGroupCustomerListMsg(order, msg.toString(), config, brand.getBrandName());
            }
        } else {
            if (order.getOrderMode() != null) {
                if (order.getOrderMode() == ShopMode.CALL_NUMBER) {
                    Customer customer = customerService.selectById(order.getCustomerId());
                    if(customer != null){
                        WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                        ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                        List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM405555608");
                        BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM405555608",TemplateSytle.CALL_NUMBER_validate);
                        if (templateFlowList != null && templateFlowList.size() != 0) {
                            String templateId = templateFlowList.get(0).getTemplateId();
                            String jumpUrl = "";
                            Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                            Map<String, Object> first = new HashMap<String, Object>();

                            if(brandTemplateEdit!=null){
                                if(brandTemplateEdit.getBigOpen()){
                                    first.put("value", brandTemplateEdit.getStartSign());
                                }else {
                                    first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                }
                            }else {
                                first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                            }
                            first.put("color", "#00DB00");

                            Map<String, Object> keyword1 = new HashMap<String, Object>();
                            keyword1.put("value", order.getSerialNumber());
                            keyword1.put("color", "#000000");
                            Map<String, Object> keyword2 = new HashMap<String, Object>();
                            if (order.getOrderMode() == 2) {
                                keyword2.put("value", order.getVerCode());
                            } else {
                                keyword2.put("value", order.getTableNumber());
                            }
                            keyword2.put("color", "#000000");
                            Map<String, Object> keyword3 = new HashMap<String, Object>();
                            keyword3.put("value", shop.getName());
                            keyword3.put("color", "#000000");
                            Map<String, Object> keyword4 = new HashMap<String, Object>();
                            Map<String, String> param = new HashMap<>();
                            param.put("orderId", order.getId());
                            List<OrderItem> orderItem = orderItemService.listByOrderId(param);
                            StringBuffer msg = new StringBuffer();
                            for (int i = 0; i < (orderItem.size() > 5 ? 6 : orderItem.size()); i++) {
                                OrderItem item = orderItem.get(i);
                                if (i == 0) {
                                    msg.append("\n\t\t\t" + item.getArticleName() + "×" + item.getCount() + "\n");
                                } else {
                                    msg.append("\t\t\t" + item.getArticleName() + "×" + item.getCount() + "\n");
                                }
                                if (orderItem.size() > 5) {
                                    msg.append("...");
                                }
                            }
                            keyword4.put("value", msg.toString());
                            keyword4.put("color", "#000000");
                            Map<String, Object> keyword5 = new HashMap<String, Object>();
                            keyword5.put("value", "￥" + order.getOrderMoney());
                            keyword5.put("color", "#000000");
                            Map<String, Object> remark = new HashMap<String, Object>();

                            if(brandTemplateEdit!=null){
                                if(brandTemplateEdit.getBigOpen()){
                                    remark.put("value", brandTemplateEdit.getEndSign());
                                }else {
                                    remark.put("value", "祝您用餐愉快！");
                                }
                            }else {
                                remark.put("value", "祝您用餐愉快！");
                            }
                            remark.put("color", "#173177");
                            content.put("first", first);
                            content.put("keyword1", keyword1);
                            content.put("keyword2", keyword2);
                            content.put("keyword3", keyword3);
                            content.put("keyword4", keyword4);
                            content.put("keyword5", keyword5);
                            content.put("remark", remark);
                            String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                            Brand brand = brandService.selectById(order.getBrandId());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                            //发送短信
                            if (setting.getMessageSwitch() == 1) {
                                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                smsParam.put("key1", order.getSerialNumber());
                                if (order.getOrderMode() == 2) {
                                    smsParam.put("key2", order.getVerCode());
                                } else {
                                    smsParam.put("key2", order.getTableNumber());
                                }
                                smsParam.put("key3", shop.getName());
                                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105805023");
                            }
                        } else {
                            Brand brand = brandService.selectById(order.getBrandId());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                        }
                    }
                } else if (order.getOrderMode() == ShopMode.MANUAL_ORDER) {
                    Customer customer = customerService.selectById(order.getCustomerId());
                    if(customer != null){
                        WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                        ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                        List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM405555608");
                        BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM405555608",TemplateSytle.CALL_NUMBER_validate);
                        if (templateFlowList != null && templateFlowList.size() != 0) {
                            String templateId = templateFlowList.get(0).getTemplateId();
                            String jumpUrl = "";
                            if (order.getParentOrderId() == null) {
                                jumpUrl = setting.getWechatWelcomeUrl() + "?orderBossId=" + order.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                            } else {
                                Order o = orderService.selectById(order.getParentOrderId());
                                jumpUrl = setting.getWechatWelcomeUrl() + "?orderBossId=" + o.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                            }
                            Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                            Map<String, Object> first = new HashMap<String, Object>();

                            if(brandTemplateEdit!=null){
                                if(brandTemplateEdit.getBigOpen()){
                                    first.put("value", brandTemplateEdit.getStartSign());
                                }else {
                                    first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                }
                            }else {
                                first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                            }
                            first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                            first.put("color", "#00DB00");
                            Map<String, Object> keyword1 = new HashMap<String, Object>();
                            keyword1.put("value", order.getSerialNumber());
                            keyword1.put("color", "#000000");
                            Map<String, Object> keyword2 = new HashMap<String, Object>();
                            if (order.getOrderMode() == 2) {
                                keyword2.put("value", order.getVerCode());
                            } else {
                                keyword2.put("value", order.getTableNumber());
                            }
                            keyword2.put("color", "#000000");
                            Map<String, Object> keyword3 = new HashMap<String, Object>();
                            keyword3.put("value", shop.getName());
                            keyword3.put("color", "#000000");
                            Map<String, Object> keyword4 = new HashMap<String, Object>();
                            Map<String, String> param = new HashMap<>();
                            param.put("orderId", order.getId());
                            List<OrderItem> orderItem = orderItemService.listByOrderId(param);
                            StringBuffer msg = new StringBuffer();
                            for (int i = 0; i < (orderItem.size() > 5 ? 6 : orderItem.size()); i++) {
                                OrderItem item = orderItem.get(i);
                                if (i == 0) {
                                    msg.append("\n\t\t\t" + item.getArticleName() + "×" + item.getCount() + "\n");
                                } else {
                                    msg.append("\t\t\t" + item.getArticleName() + "×" + item.getCount() + "\n");
                                }
                                if (orderItem.size() > 5) {
                                    msg.append("...");
                                }
                            }
                            keyword4.put("value", msg.toString());
                            keyword4.put("color", "#000000");
                            Map<String, Object> keyword5 = new HashMap<String, Object>();
                            keyword5.put("value", "￥" + order.getOrderMoney());
                            keyword5.put("color", "#000000");
                            Map<String, Object> remark = new HashMap<String, Object>();

                            if(brandTemplateEdit!=null){
                                if(brandTemplateEdit.getBigOpen()){
                                    remark.put("value", brandTemplateEdit.getEndSign());
                                }else {
                                    remark.put("value", "点击这里进行“加菜”");
                                }
                            }else {
                                remark.put("value", "点击这里进行“加菜”");
                            }

                            remark.put("color", "#173177");
                            content.put("first", first);
                            content.put("keyword1", keyword1);
                            content.put("keyword2", keyword2);
                            content.put("keyword3", keyword3);
                            content.put("keyword4", keyword4);
                            content.put("keyword5", keyword5);
                            content.put("remark", remark);
                            String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                            Brand brand = brandService.selectById(order.getBrandId());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                            //发送短信
                            if (setting.getMessageSwitch() == 1) {
                                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                smsParam.put("key1", order.getSerialNumber());
                                if (order.getOrderMode() == 2) {
                                    smsParam.put("key2", order.getVerCode());
                                } else {
                                    smsParam.put("key2", order.getTableNumber());
                                }
                                smsParam.put("key3", shop.getName());
                                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105805023");
                            }
                        } else {
                            Brand brand = brandService.selectById(order.getBrandId());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                        }
                    }
                } else {
                    Customer customer = customerService.selectById(order.getCustomerId());
                    if(customer != null){
                        WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                        ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                        List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM408705883");
                        BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM408705883",TemplateSytle.BIG_BOSS);

                        if (templateFlowList != null && templateFlowList.size() != 0) {
                            String templateId = templateFlowList.get(0).getTemplateId();
                            String jumpUrl = "";
                            if (order.getParentOrderId() == null) {
                                jumpUrl = setting.getWechatWelcomeUrl() + "?orderBossId=" + order.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                            } else {
                                Order o = orderService.selectById(order.getParentOrderId());
                                jumpUrl = setting.getWechatWelcomeUrl() + "?orderBossId=" + o.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                            }
                            Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                            Map<String, Object> first = new HashMap<String, Object>();

                            if (order.getParentOrderId() == null) {
                                if(brandTemplateEdit!=null){
                                    if(brandTemplateEdit.getBigOpen()){
                                        first.put("value", brandTemplateEdit.getStartSign());
                                    }else {
                                        first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                    }
                                }else {
                                    first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                }

                            } else {
                                if(brandTemplateEdit!=null){
                                    if(brandTemplateEdit.getBigOpen()){
                                        first.put("value", brandTemplateEdit.getStartSign());
                                    }else {
                                        first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                    }
                                }else {
                                    first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                }
                            }
                            first.put("color", "#00DB00");
                            Map<String, Object> keyword1 = new HashMap<String, Object>();
                            keyword1.put("value", order.getSerialNumber());
                            keyword1.put("color", "#000000");
                            Map<String, Object> keyword2 = new HashMap<String, Object>();
                            keyword2.put("value", shop.getName());
                            keyword2.put("color", "#000000");
                            Map<String, Object> keyword3 = new HashMap<String, Object>();
                            if (order.getOrderMode() == 2) {
                                keyword3.put("value", order.getVerCode());
                            } else {
                                keyword3.put("value", order.getTableNumber());
                            }
                            keyword3.put("color", "#000000");
                            Map<String, Object> keyword4 = new HashMap<String, Object>();
                            keyword4.put("value", "￥" + order.getOrderMoney());
                            keyword4.put("color", "#000000");
                            Map<String, Object> keyword5 = new HashMap<String, Object>();
                            Map<String, String> param = new HashMap<>();
                            param.put("orderId", order.getId());
                            List<OrderItem> orderItem = orderItemService.listByOrderId(param);
                            StringBuffer msg = new StringBuffer();
                            for (int i = 0; i < (orderItem.size() > 5 ? 6 : orderItem.size()); i++) {
                                OrderItem item = orderItem.get(i);
                                if (i == 0) {
                                    msg.append("\n\t\t\t" + item.getArticleName() + "×" + item.getCount() + "\n");
                                } else {
                                    msg.append("\t\t\t" + item.getArticleName() + "×" + item.getCount() + "\n");
                                }
                                if (orderItem.size() > 5) {
                                    msg.append("...");
                                }
                            }
                            keyword5.put("value", msg.toString());
                            keyword5.put("color", "#000000");
                            Map<String, Object> remark = new HashMap<String, Object>();
                            if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getOrderBefore() == null) {
                                if(brandTemplateEdit!=null){
                                    if(brandTemplateEdit.getBigOpen()){
                                        remark.put("value", brandTemplateEdit.getEndSign());
                                    }else {
                                        remark.put("value", "点击结果进行\"加菜\"或\"买单\"");
                                    }
                                }else {
                                    remark.put("value", "点击结果进行\"加菜\"或\"买单\"");
                                }
                                remark.put("color", "#173177");
                            }else{
                                remark.put("value", "");
                                remark.put("color", "#173177");
                            }
                            content.put("first", first);
                            content.put("keyword1", keyword1);
                            content.put("keyword2", keyword2);
                            content.put("keyword3", keyword3);
                            content.put("keyword4", keyword4);
                            content.put("keyword5", keyword5);
                            content.put("remark", remark);
                            //String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                            Brand brand = brandService.selectById(order.getBrandId());
                            if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                                weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", customer.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if(setting.getMessageSwitch()==1){
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shop.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            } else {
                                String orderId = order.getId();
                                if(order.getParentOrderId() != null && !"".equals(order.getParentOrderId())){
                                    orderId = order.getParentOrderId();
                                }
                                List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                                for (Participant p : participants) {
                                    Customer c = customerService.selectById(p.getCustomerId());
                                    weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                    Map map = new HashMap(4);
                                    map.put("brandName", brand.getBrandName());
                                    map.put("fileName", c.getId());
                                    map.put("type", "UserAction");
                                    map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                    doPostAnsc(map);
                                    //发送短信
                                    if(setting.getMessageSwitch()==1){
                                        com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                        smsParam.put("key1", order.getSerialNumber());
                                        smsParam.put("key2", shop.getName());
                                        smsParam.put("key3", order.getTableNumber());
                                        com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                    }
                                }
                            }
                        }else {
                            Brand brand = brandService.selectById(order.getBrandId());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                        }
                    }
                }
            }
        }
//      String data = weChatService.delTemplate(templateId, config.getAppid(), config.getAppsecret());
    }

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.orderWxPaySuccessAspect(..))")
    public void orderWxPaySuccessAspect() {
    }

    ;


    @Pointcut("execution(* com.resto.shop.web.service.OrderService.afterPay(..))")
    public void afterPay() {

    }

    ;


    @Pointcut("execution(* com.resto.shop.web.service.OrderService.confirmOrderPos(..))")
    public void confirmOrderPos() {

    }

    ;


    @AfterReturning(value = "confirmOrderPos()", returning = "jsonResult")
    public void confirmOrderPos(JSONResult jsonResult) throws AppException {
        if(!jsonResult.isSuccess()){
            return;
        }
        Order order = (Order) jsonResult.getData();
        //gigtPush(order);
        pushConfirmOrderToPos(order.getId());
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        mqMessageProducer.sendNotAllowContinueMessage(order, 1000 * setting.getCloseContinueTime()); //延迟两小时，禁止继续加菜
        Order o = orderService.getOrderAccount(order.getShopDetailId());
        redisService.set(order.getShopDetailId() + "shopOrderCount", o.getOrderCount());
        redisService.set(order.getShopDetailId() + "shopOrderTotal", o.getOrderTotal());

        if (!StringUtils.isEmpty(order.getBeforeId()) && order.getOrderState() == OrderState.PAYMENT) {
            orderBeforeService.updateState(order.getBeforeId(), 1);
            Order before = orderService.selectById(order.getBeforeId());
            before.setOrderState(OrderState.CANCEL);
            orderService.update(before);
            Map<String, String> param = new HashMap<>();
            param.put("orderId", order.getBeforeId());
            List<OrderItem> orderItems = orderItemService.listByOrderId(param);
            if (!CollectionUtils.isEmpty(orderItems)) {
                for (OrderItem orderItem : orderItems) {
                    orderItem.setOrderId(order.getId());
                    orderItem.setStatus(2);
                    orderItemService.update(orderItem);
                }
            }
        }

        //在pos端确认的情况下无需再去修改订单了
//        if (order.getPayMode() == OrderPayMode.XJ_PAY || order.getPayMode() == OrderPayMode.YL_PAY || order.getPayMode() == OrderPayMode.SHH_PAY || order.getPayMode() == OrderPayMode.JF_PAY) {
//            orderService.pushOrder(order.getId());
//        }
        Customer customer = customerService.selectById(order.getCustomerId());
        if(customer != null) {
            if(order.getOrderState()==OrderState.PAYMENT || order.getOrderState()==OrderState.CONFIRM){
                WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM414174151");
                BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM414174151", TemplateSytle.TICKET);

                if (templateFlowList != null && templateFlowList.size() != 0) {
                    String templateId = templateFlowList.get(0).getTemplateId();
                    String baseUrl = UrlUtils.getIP(URI.create(setting.getWechatWelcomeUrl())).toString();

                    Integer ticketType = 0;
                    if(setting.getOpenTicket()==1){
                        ticketType = 1;
                    }
                    //微信发票推送
                    if(setting.getOpenTicket()==2){

                    }
                    String jumpUrl = baseUrl+"/restowechat/src/views/invoice.html?baseUrl="+baseUrl+"&invoiceType="+2+"&shopId="+shop.getId()+"&customerId="+customer.getId()+"&ticketType="+ticketType;

                    Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                    Map<String, Object> first = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            first.put("value", brandTemplateEdit.getStartSign());
                        }else {
                            first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                        }

                    }else {
                        first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                    }

                    first.put("color", "#00DB00");
                    Map<String, Object> keyword1 = new HashMap<String, Object>();
                    keyword1.put("value", shop.getName());
                    keyword1.put("color", "#000000");
                    Map<String, Object> keyword2 = new HashMap<String, Object>();
                    keyword2.put("value", "餐饮");
                    keyword2.put("color", "#000000");
                    Map<String, Object> keyword3 = new HashMap<String, Object>();
                    BigDecimal money = orderPaymentItemMapper.selectEnableTicketMoney(order.getId());
                    if(money==null){
                        setting.setOpenTicket(0);
                        money = BigDecimal.valueOf(0);
                    }
                    keyword3.put("value", money.toString()+"(可开票金额)");
                    keyword3.put("color", "#000000");
                    Map<String, Object> keyword4 = new HashMap<String, Object>();
                    keyword4.put("value", "30天");
                    keyword4.put("color", "#000000");

                    Map<String, Object> remark = new HashMap<String, Object>();
                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            remark.put("value", brandTemplateEdit.getEndSign());
                        }else {
                            remark.put("value", "请点击详情进入订单列表开具发票");
                        }
                    }else {
                        remark.put("value", "请点击详情进入订单列表开具发票");
                    }

                    remark.put("color", "#173177");

                    content.put("first", first);
                    content.put("keyword1", keyword1);
                    content.put("keyword2", keyword2);
                    content.put("keyword3", keyword3);
                    content.put("keyword4", keyword4);
                    content.put("remark", remark);
                    //String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                    Brand brand = brandService.selectById(order.getBrandId());
                    if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                        if(setting.getOpenTicket()!=0){
                            weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                            //发送短信
                            if (setting.getMessageSwitch() == 1) {
                                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                smsParam.put("key1", order.getSerialNumber());
                                smsParam.put("key2", shop.getName());
                                smsParam.put("key3", order.getTableNumber());
                                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                            }
                        }


                    } else {
                        String orderId = order.getId();
                        if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
                            orderId = order.getParentOrderId();
                        }
                        List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                        for (Participant p : participants) {
                            if(setting.getOpenTicket()!=0){
                                Customer c = customerService.selectById(p.getCustomerId());
                                weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", c.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if (setting.getMessageSwitch() == 1) {
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shop.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            }

                        }
                    }
                } else {
                    Brand brand = brandService.selectById(order.getBrandId());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
        }
        mqMessageProducer.sendPrintSuccess(order.getShopDetailId());
        pushOrderMoneyAndCountToPos(o.getOrderCount(), o.getOrderTotal(), order.getShopDetailId());
        if (order.getParentOrderId() == null) {
            mqMessageProducer.sendPlaceOrderMessage(order);
        }
    }

    @AfterReturning(value = "afterPay()", returning = "jsonResult")
    public void afterPay(JSONResult jsonResult) {
        if(!jsonResult.isSuccess()){
            return;
        }
        Order order = (Order) jsonResult.getData();
        //gigtPush(order);
        pushAfterPayOrderToPos(order.getId());
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        if (order.getPayMode() != OrderPayMode.ALI_PAY) { //已支付
            Customer customer = customerService.selectById(order.getCustomerId());
            if(customer != null) {
                if(order.getOrderState() == OrderState.PAYMENT){
                    WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                    ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                    List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM414174151");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM414174151", TemplateSytle.TICKET);

                    if (templateFlowList != null && templateFlowList.size() != 0) {
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String baseUrl = UrlUtils.getIP(URI.create(setting.getWechatWelcomeUrl())).toString();
                        Integer ticketType = 0;
                        if(setting.getOpenTicket()==1){
                            ticketType = 1;
                        }
                        if(setting.getOpenTicket()==2){

                        }
                        String jumpUrl = baseUrl+"/restowechat/src/views/invoice.html?baseUrl="+baseUrl+"&invoiceType="+2+"&shopId="+shop.getId()+"&customerId="+customer.getId()+"&ticketType="+ticketType;

                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();

                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                first.put("value", brandTemplateEdit.getStartSign());
                            }else {
                                first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                            }
                        }else {
                            first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                        }

                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", shop.getName());
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", "餐饮");
                        keyword2.put("color", "#000000");
                        Map<String, Object> keyword3 = new HashMap<String, Object>();
                        BigDecimal money = orderPaymentItemMapper.selectEnableTicketMoney(order.getId());
                        if(money==null){
                            setting.setOpenTicket(0);
                            money = BigDecimal.valueOf(0);
                        }
                        keyword3.put("value", money.toString()+"(可开票金额)");
                        keyword3.put("color", "#000000");
                        Map<String, Object> keyword4 = new HashMap<String, Object>();
                        keyword4.put("value", "30天");
                        keyword4.put("color", "#000000");

                        Map<String, Object> remark = new HashMap<String, Object>();
                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                remark.put("value", brandTemplateEdit.getEndSign());
                            }else {
                                remark.put("value", "请点击详情进入订单列表开具发票");
                            }
                        }else {
                            remark.put("value", "请点击详情进入订单列表开具发票");
                        }

                        remark.put("color", "#173177");

                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("keyword3", keyword3);
                        content.put("keyword4", keyword4);
                        content.put("remark", remark);
                        //String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                        Brand brand = brandService.selectById(order.getBrandId());
                        if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                            if(setting.getOpenTicket()!=0){
                                weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", customer.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if (setting.getMessageSwitch() == 1) {
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shop.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            }

                        } else {
                            String orderId = order.getId();
                            if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
                                orderId = order.getParentOrderId();
                            }
                            List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                            for (Participant p : participants) {
                                if(setting.getOpenTicket()!=0){
                                    Customer c = customerService.selectById(p.getCustomerId());
                                    weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                    Map map = new HashMap(4);
                                    map.put("brandName", brand.getBrandName());
                                    map.put("fileName", c.getId());
                                    map.put("type", "UserAction");
                                    map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                    doPostAnsc(map);
                                    //发送短信
                                    if (setting.getMessageSwitch() == 1) {
                                        com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                        smsParam.put("key1", order.getSerialNumber());
                                        smsParam.put("key2", shop.getName());
                                        smsParam.put("key3", order.getTableNumber());
                                        com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                    }
                                }

                            }
                        }
                    } else {
                        Brand brand = brandService.selectById(order.getBrandId());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
                }

            }
        }
        if (order.getPayMode() != OrderPayMode.ALI_PAY) { //已支付
            mqMessageProducer.sendPlaceOrderMessage(order);
        }
        if (order.getOrderState() == OrderState.PAYMENT) {
            String shopId = order.getShopDetailId();
            Integer orderCount = (Integer) redisService.get(shopId + "shopOrderCount");
            BigDecimal orderTotal = (BigDecimal) redisService.get(shopId + "shopOrderTotal");
            if (orderCount == null) {
                orderCount = 0;
            }
            if (orderTotal == null) {
                orderTotal = BigDecimal.valueOf(0);
            }
            if (order.getParentOrderId() == null) {
                orderCount++;
            }
            orderTotal = orderTotal.add(order.getOrderMoney());
            redisService.set(shopId + "shopOrderCount", orderCount);
            redisService.set(shopId + "shopOrderTotal", orderTotal);
            mqMessageProducer.sendPrintSuccess(shopId);
            pushOrderMoneyAndCountToPos(orderCount, orderTotal, order.getShopDetailId());
        }
        if(order.getPayMode() != OrderPayMode.WX_PAY && order.getPayMode() != OrderPayMode.ALI_PAY){
            log.info("后付买单时，除微信支付宝支付需向newPos端推送支付信息。 订单Id：" + order.getId());
            //BigDecimal money = orderPaymentItemMapper.sumOrderMoneyByOrderId(order.getId());
            //if((money == null ? new BigDecimal(0) : money).doubleValue() == 0 && order.getOrderMoney().doubleValue() > 0){
            if(order.getOrderState() != OrderState.PAYMENT && order.getOrderState() != OrderState.CONFIRM && order.getOrderMoney().doubleValue() > 0){
                //TODO 这里存在问题，暂未修复。 如果出现0元支付信息，并且订单总额不是0元，则不给newpos推送
            }else{
                sendInfoToNewPos(order, "orderPay");
            }
        }
    }

    /**
     * 后付模式已支付推送订单至pos
     * @param orderId
     */
    private void pushAfterPayOrderToPos(String orderId){
        Order pushOrder = orderService.selectById(orderId);
        JSONObject orderObj = new JSONObject(JSON.toJSONString(pushOrder));
        orderObj.put("dataType", "current");
        orderObj.put("shopId", pushOrder.getShopDetailId());
        if (pushOrder.getOrderState().equals(OrderState.PAYMENT)
                || (pushOrder.getPayMode().equals(OrderPayMode.XJ_PAY)
                || pushOrder.getPayMode().equals(OrderPayMode.YL_PAY)
                || pushOrder.getPayMode().equals(OrderPayMode.JF_PAY)
                || pushOrder.getPayMode().equals(OrderPayMode.SHH_PAY))) {
            //订单已支付，打印结账单
            mqttMQMessageProducer.sendCreateOrderToPosMessage(orderObj.toString());
            if (pushOrder.getOrderState().equals(OrderState.PAYMENT)) {
                //修改菜品实际金额
                orderItemService.updateOrderItemActualAmount(pushOrder, false);
            }
        }

    }

    @AfterReturning(value = "orderWxPaySuccessAspect()", returning = "jsonResult")
    public void orderPayAfter(JSONResult jsonResult) {
        if(!jsonResult.isSuccess()){
            return;
        }
        Order order = (Order) jsonResult.getData();
        pushWXOrAliPaySuccessOrderToPos(order.getId());
        if (!StringUtils.isEmpty(order.getBeforeId()) && order.getOrderState() == OrderState.PAYMENT) {
            orderBeforeService.updateState(order.getBeforeId(), 1);
            Order before = orderService.selectById(order.getBeforeId());
            before.setOrderState(OrderState.CANCEL);
            orderService.update(before);
            Map<String, String> param = new HashMap<>();
            param.put("orderId", order.getBeforeId());
            List<OrderItem> orderItems = orderItemService.listByOrderId(param);
            if (!CollectionUtils.isEmpty(orderItems)) {
                for (OrderItem orderItem : orderItems) {
                    orderItem.setOrderId(order.getId());
                    orderItem.setStatus(2);
                    orderItemService.update(orderItem);
                }
            }
        }
        //R+外卖走消息队列  (订单不为空 支付模式不为空  支付为微信或者支付宝支付  已支付  已下单 外卖模式)
        if (order != null && order.getPayMode() != null && (order.getPayMode() == OrderPayMode.WX_PAY || order.getPayMode() == OrderPayMode.ALI_PAY) &&
                order.getOrderState().equals(OrderState.PAYMENT) && !order.getProductionStatus().equals(ProductionStatus.PRINTED) && order.getDistributionModeId() == 2) {
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());

            mqMessageProducer.sendPlatformOrderMessage(order.getId(), PlatformType.R_VERSION, order.getBrandId(), order.getShopDetailId());
            mqMessageProducer.sendAutoConfirmOrder(order, setting.getAutoConfirmTime() * 1000);
            return;
        }

        //订单不为空 支付模式不为空  支付为支付宝支付  已支付  已下单
        if (order != null && order.getPayMode() != null && order.getPayMode() == OrderPayMode.ALI_PAY &&
                order.getOrderState().equals(OrderState.PAYMENT)
                && order.getProductionStatus().equals(ProductionStatus.HAS_ORDER)) {
            if (order.getPayType() == PayType.NOPAY) {
                order.setPrintTimes(1);//打印次数
                orderService.update(order);
            }
            /*if(order.getDistributionModeId()==2&&order.getOrderState()==OrderState.PAYMENT){
                mqMessageProducer.sendPlatformOrderMessage(order.getId(), 4, order.getBrandId(), order.getShopDetailId());
            }else{*/

            mqMessageProducer.sendPlaceOrderMessage(order);
            //}
        }

        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        //订单不为空  已支付   桌号不为空或者外带   坐下点餐模式或者大Boss模式
        if (order != null && order.getOrderState() == OrderState.PAYMENT
                && (order.getTableNumber() != null || (order.getDistributionModeId() == DistributionType.TAKE_IT_SELF && shopDetail.getContinueOrderScan().equals(Common.NO)))
                && (order.getOrderMode() == ShopMode.TABLE_MODE || order.getOrderMode() == ShopMode.BOSS_ORDER)) {
            if (order.getPayType() == PayType.NOPAY) {
                order.setPrintTimes(1);
                orderService.update(order);
            }
            mqMessageProducer.sendPlaceOrderMessage(order);
        }
        if (StringUtils.isEmpty(order.getGroupId())) {
            shopCartService.clearShopCart(order.getCustomerId(), order.getShopDetailId());
        } else {
            shopCartService.deleteByGroup(order.getGroupId());
        }


        //订单不为空  已支付  电视叫号模式
        if (order != null && order.getOrderState() == OrderState.PAYMENT
                && order.getOrderMode() == ShopMode.CALL_NUMBER) {
            mqMessageProducer.sendPlaceOrderMessage(order);
        }

        //稍后支付  大Boss模式  支付宝或微信支付
        if (order.getPayType() == PayType.NOPAY && order.getOrderMode() == ShopMode.BOSS_ORDER && (order.getPayMode() == OrderPayMode.WX_PAY || order.getPayMode() == OrderPayMode.ALI_PAY)) {
            /*if(order.getDistributionModeId()==2&&order.getOrderState()==OrderState.PAYMENT){
                mqMessageProducer.sendPlatformOrderMessage(order.getId(), 4, order.getBrandId(), order.getShopDetailId());
            }else{*/
            mqMessageProducer.sendAutoConfirmOrder(order, 5 * 1000);
            //}
            Order o = orderService.getOrderAccount(order.getShopDetailId());
            redisService.set(order.getShopDetailId() + "shopOrderCount", o.getOrderCount());
            redisService.set(order.getShopDetailId() + "shopOrderTotal", o.getOrderTotal());
            mqMessageProducer.sendPrintSuccess(order.getShopDetailId());
            pushOrderMoneyAndCountToPos(o.getOrderCount(), o.getOrderTotal(), order.getShopDetailId());
//            orderService.confirmOrder(order);
        }
        if(shopDetail.getPosVersion() == PosVersion.VERSION_2_0){
            if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY) {
                //后付情况下订单已经推送到NewPos服务器，则直接推送支付信息
                log.error("订单：" + order.getId() + "完成微信、支付宝支付，开始向NewPos推送订单信息");
                sendInfoToNewPos(order, "orderPay");
            } else {
                //先付模式订单未提前推送给NewPos，则推送创建订单的信息
                log.error("先付模式下支付完成推送创建订单信息，orderId为：" + order.getId());
                sendInfoToNewPos(order, "createOrder");
            }
        }
    }


    /**
     * 下单或者买单向NewPos推送订单
     * @param order
     * @param pushType
     */
    private void sendInfoToNewPos(Order order, String pushType) {
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        if (Common.YES.equals(shopDetail.getIsOpenEmqPush())) {
            //开启了新版推送
            switch (pushType) {
                case "createOrder":
                    mqttMQMessageProducer.sendCreateOrderToNewPosMessage(order, 5000L);
                    break;
                case "orderPay":
                    mqttMQMessageProducer.sendOrderWxOrAliPaySuccessToNewPosMessage(order);
                    break;
            }
        } else {
            switch (pushType) {
                case "createOrder":
                    mqMessageProducer.sendCreateOrderMessage(order, 5000L);
                    break;
                case "orderPay":
                    mqMessageProducer.sendOrderPay(order, 5000L);
                    break;
            }
        }
    }

    /**
     * 后付、先付模式下已支付向POS推送订单
     * @param orderId
     */
    private void pushWXOrAliPaySuccessOrderToPos(String orderId) {
        Order pushOrder = orderService.selectById(orderId);
        JSONObject orderObj = new JSONObject(JSON.toJSONString(pushOrder));
        orderObj.put("shopId", pushOrder.getShopDetailId());
        if (pushOrder.getDistributionModeId().equals(DistributionType.DELIVERY_MODE_ID)) {
            //R+外卖订单
            if (pushOrder.getOrderState().equals(OrderState.PAYMENT)) {
                orderObj.put("dataType", "platform");
                orderObj.put("type", PlatformType.R_VERSION);
                mqttMQMessageProducer.sendPlatformOrderMessage(orderObj.toString());
            }
        }else{
            //堂吃、外带订单
            orderObj.put("dataType", "current");
            if (pushOrder.getOrderState().equals(OrderState.PAYMENT) && !pushOrder.getOrderMode().equals(ShopMode.MANUAL_ORDER)) {
                //订单已支付，执行打单
                mqttMQMessageProducer.sendOrderWxOrAliPaySuccessToPosMessage(orderObj.toString());
                //修改订单菜品实际金额
                orderItemService.updateOrderItemActualAmount(pushOrder, false);
            } else if (pushOrder.getOrderState().equals(OrderState.PAYMENT) && pushOrder.getOrderMode().equals(ShopMode.MANUAL_ORDER)
                    && StringUtils.isNotBlank(pushOrder.getParentOrderId())) {
                //订单已支付，执行打单
                mqttMQMessageProducer.sendOrderWxOrAliPaySuccessToPosMessage(orderObj.toString());
                //修改菜品实际金额
                orderItemService.updateOrderItemActualAmount(pushOrder, false);
            }
        }
    }


    @Pointcut("execution(* com.resto.shop.web.service.OrderService.pushOrder(..))")
    public void pushOrder() {
    }

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.callNumber(..))")
    public void callNumber() {
    }

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.printSuccess(..))")
    public void printSuccess() {
    }

    @AfterReturning(value = "callNumber()", returning = "jsonResult")
    public void createCallMessage(JSONResult jsonResult) throws Throwable {
        if(!jsonResult.isSuccess()){
            return;
        }
        Order order = (Order) jsonResult.getData();
        Customer customer = customerService.selectById(order.getCustomerId());
        WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        //根据订单找到对应的店铺
        if (customer != null) {
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            if (setting.getTemplateEdition() == 0) {
                weChatService.sendCustomerMsgASync("您的餐品已经准备好了，请尽快到吧台取餐！", customer.getWechatId(), config.getAppid(), config.getAppsecret());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:您的餐品已经准备好了，请尽快到吧台取餐！,请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            } else {
                List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM411223846");
                BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM411223846",TemplateSytle.CALL_NUMBER_GET_FOOT);
                if (templateFlowList != null && templateFlowList.size() != 0) {
                    String templateId = templateFlowList.get(0).getTemplateId();
                    String jumpUrl = "";
                    Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                    Map<String, Object> first = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            first.put("value", brandTemplateEdit.getStartSign());
                        }else {
                            first.put("value", "您好，餐点已备齐，请取餐。");
                        }
                    }else {
                        first.put("value", "您好，餐点已备齐，请取餐。");
                    }

                    first.put("color", "#00DB00");
                    Map<String, Object> keyword1 = new HashMap<String, Object>();
                    keyword1.put("value", shopDetail.getName());
                    keyword1.put("color", "#000000");
                    Map<String, Object> keyword2 = new HashMap<String, Object>();
                    keyword2.put("value", order.getVerCode());
                    keyword2.put("color", "#000000");
                    Map<String, Object> keyword3 = new HashMap<String, Object>();
                    keyword3.put("value", order.getSerialNumber());
                    keyword3.put("color", "#000000");
                    Map<String, Object> remark = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            remark.put("value", brandTemplateEdit.getEndSign());
                        }else {
                            remark.put("value", "为了保证出品新鲜美味，请您请尽快到吧台取餐！");
                        }
                    }else {
                        remark.put("value", "为了保证出品新鲜美味，请您请尽快到吧台取餐！");
                    }

                    remark.put("color", "#173177");
                    content.put("first", first);
                    content.put("keyword1", keyword1);
                    content.put("keyword2", keyword2);
                    content.put("keyword3", keyword3);
                    content.put("remark", remark);
                    String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                    //发送短信
                    if (setting.getMessageSwitch() == 1) {
                        com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                        smsParam.put("key1", shopDetail.getName());
                        smsParam.put("key2", order.getVerCode());
                        smsParam.put("key3", order.getSerialNumber());
                        com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105785023");
                    }
                } else {
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
        }

//        weChatService.sendCustomerWaitNumberMsg("您的餐品已经准备好了，请尽快到吧台取餐！", customer.getWechatId(), config.getAppid(), config.getAppsecret());
//		mqMessageProducer.sendCallMessage(order.getBrandId(),order.getId(),order.getCustomerId());

        LogTemplateUtils.getCallNumber(brand.getBrandName(), order.getId());
        if (shopDetail.getIsPush().equals(Common.YES)) { //开启就餐提醒
            mqMessageProducer.sendRemindMsg(order, shopDetail.getPushTime() * 1000);
        }

    }

    @AfterReturning(value = "pushOrder()||callNumber()", returning = "jsonResult")
    public void pushOrderAfter(JSONResult jsonResult) throws Throwable {
        if(!jsonResult.isSuccess()){
            return;
        }
        Order order = (Order) jsonResult.getData();
        if(ProductionStatus.PRINTED == order.getProductionStatus()){
            return;
        }
        if (order != null) {
            if (ProductionStatus.HAS_ORDER == order.getProductionStatus()) {
                if (order.getPayMode() != null && order.getPayMode() == OrderPayMode.ALI_PAY && order.getOrderState().equals(OrderState.SUBMIT)) {
                    return;
                }
//                BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
                log.info("客户下单,发送成功下单通知" + order.getId());

                if (order.getEmployeeId() == null) {
                    if (order.getPrintOrderTime() == null) {
                        if (order.getPayMode() == OrderPayMode.YL_PAY || order.getPayMode() == OrderPayMode.XJ_PAY || order.getPayMode() == OrderPayMode.SHH_PAY || order.getPayMode() == OrderPayMode.JF_PAY) {
                            mqMessageProducer.sendPlaceOrderNoPayMessage(order);
                        } else {
                            mqMessageProducer.sendPlaceOrderMessage(order);
                        }
                    }
                } else {
                    if (order.getOrderState().equals(OrderState.PAYMENT)) {
                        if (order.getPayMode() == OrderPayMode.YL_PAY || order.getPayMode() == OrderPayMode.XJ_PAY || order.getPayMode() == OrderPayMode.SHH_PAY || order.getPayMode() == OrderPayMode.JF_PAY) {
                            mqMessageProducer.sendPlaceOrderNoPayMessage(order);
                        } else {
                            mqMessageProducer.sendPlaceOrderMessage(order);
                        }
                    }
                }

//				log.info("客户下单，添加自动拒绝5分钟未打印的订单");
//				mqMessageProducer.sendNotPrintedMessage(order,1000*60*5); //延迟五分钟，检测订单是否已经打印
//                if ((order.getOrderMode() == ShopMode.TABLE_MODE || order.getOrderMode() == ShopMode.BOSS_ORDER) && order.getEmployeeId() == null) {  //坐下点餐在立即下单的时候，发送支付成功消息通知
//                    log.info("坐下点餐在立即下单的时候，发送支付成功消息通知:" + order.getId());
//                    sendPaySuccessMsg(order);
//                }
                log.info("检查打印异常");
            } else if (ProductionStatus.HAS_CALL == order.getProductionStatus()) {
                log.info("发送叫号信息");
                mqMessageProducer.sendPlaceOrderMessage(order);
            }
        }
    }

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.confirmOrder(..))")
    public void confirmOrder() {
    }

    ;

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.confirmWaiMaiOrder(..))")
    public void confirmWaiMaiOrder() {
    }

    ;

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.confirmBossOrder(..))")
    public void confirmBossOrder() {
    }

    ;

    @AfterReturning(value = "printSuccess()", returning = "jsonResult")
    public void pushContent(JSONResult jsonResult) {
        if(!jsonResult.isSuccess()){
            return;
        }
        Order order = (Order) jsonResult.getData();
        log.info("订单：" + order.getId() + "已打印成功，开始发送推送信息");
        if(order != null ){
            if(order.getPayType() == PayType.PAY && order.getOrderState() == OrderState.SUBMIT){
                return;
            }
            ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
            Brand brand = brandService.selectById(order.getBrandId());
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            log.info("发送禁止加菜:" + setting.getCloseContinueTime() + "s 后发送");
            if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.PAY) {
                if (setting.getAutoConfirmTime() <= setting.getCloseContinueTime()) {    //加菜时间跟领取红包时间对比
                    if (order.getOrderState() == OrderState.PAYMENT) {
                        mqMessageProducer.sendNotAllowContinueMessage(order, 1000 * setting.getCloseContinueTime()); //延迟禁止继续加菜
                        mqMessageProducer.sendBossOrder(order, setting.getAutoConfirmTime() * 1000);
                    }
                } else {
                    mqMessageProducer.sendNotAllowContinueMessage(order, 1000 * setting.getCloseContinueTime()); //延迟禁止继续加菜
                    mqMessageProducer.sendAutoConfirmOrder(order, setting.getAutoConfirmTime() * 1000);
                }
            } else if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY) {
                if(!org.apache.commons.lang3.StringUtils.isEmpty(order.getBeforeId())){
                    //如果这个后付订单之前存在预点餐的餐品
                    Order before = orderService.selectById(order.getBeforeId());
                    //取消预点餐订单      ------后付
                    before.setOrderState(OrderState.CANCEL);
                    orderService.update(before);
                    //更新预点餐的orderItmer到新的订单项内
                    orderItemService.updateOrderIdByBeforeId(order.getId(), before.getId());
                }
                mqMessageProducer.sendNotAllowContinueMessage(order, 1000 * setting.getCloseContinueTime()); //延迟禁止继续加菜
            } else if (order.getOrderMode() != ShopMode.HOUFU_ORDER) {
                mqMessageProducer.sendNotAllowContinueMessage(order, 1000 * setting.getCloseContinueTime()); //延迟禁止继续加菜
                mqMessageProducer.sendPlaceOrderMessage(order);
                mqMessageProducer.sendAutoConfirmOrder(order, setting.getAutoConfirmTime() * 1000);
            }
//            else {
//                if (order.getOrderState() == OrderState.PAYMENT) {
//                    mqMessageProducer.sendAutoConfirmOrder(order, setting.getAutoConfirmTime() * 1000);
//                    mqMessageProducer.sendModelFivePaySuccess(order);
//                    if (order.getPrintTimes() == 0) {
//                        order.setPrintTimes(order.getPrintTimes() + 1);
//                        orderService.update(order);
//                        mqMessageProducer.sendPlaceOrderMessageAgain(order, 6000);
//                    }
//                }
//            }
            if (order.getPrintTimes() == 0) {
                sendPaySuccessMsg(order);
            }
            if (order.getOrderMode() != null) {
                switch (order.getOrderMode()) {
                    case ShopMode.CALL_NUMBER:
                        log.info("叫号模式,发送取餐码信息:" + order.getId());
                        sendVerCodeMsg(order);
                    break;
                    default:
                        break;
                }
            }
            log.info("发送打印信息");
            log.info("打印成功后，发送自动确认订单通知！" + setting.getAutoConfirmTime() + "s 后发送" + ",orderId:" + order.getId());

            //
            if (order.getPayType() == PayType.PAY && (order.getPayMode() == OrderPayMode.YUE_PAY || order.getPayMode() == OrderPayMode.WX_PAY
                    || order.getPayMode() == OrderPayMode.ALI_PAY)) {
                String shopId = order.getShopDetailId();
//                if (!MemcachedUtils.add(order.getId(), 1)) {

                if (!redisService.setnx(order.getId(), 1)) {
                    Integer orderCount = (Integer) redisService.get(shopId + "shopOrderCount");
                    BigDecimal orderTotal = (BigDecimal) redisService.get(shopId + "shopOrderTotal");
                    if (orderCount == null) {
                        orderCount = 0;
                    }
                    if (orderTotal == null) {
                        orderTotal = BigDecimal.valueOf(0);
                    }
                    if (order.getParentOrderId() == null) {
                        orderCount++;
                    }
                    orderTotal = orderTotal.add(order.getOrderMoney());
                    redisService.set(shopId + "shopOrderCount", orderCount);
                    redisService.set(shopId + "shopOrderTotal", orderTotal);
                    mqMessageProducer.sendPrintSuccess(shopId);
                    pushOrderMoneyAndCountToPos(orderCount, orderTotal, order.getShopDetailId());
                }
            }


            //发送推送
            Customer customer = customerService.selectById(order.getCustomerId());
            if(customer != null){
                WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
                if (order.getOrderMode() == ShopMode.HOUFU_ORDER
                        && order.getOrderState() == OrderState.SUBMIT
                        && order.getProductionStatus() == ProductionStatus.PRINTED) {
                    if (setting.getTemplateEdition() == 0) {
                        StringBuffer msg = new StringBuffer();
                        if (order.getParentOrderId() == null) {
                            msg.append("下单成功!" + "\n");
                        } else {
                            msg.append("加菜成功!" + "\n");
                        }
                        msg.append("订单编号:" + order.getSerialNumber() + "\n");
                        msg.append("桌号：" + order.getTableNumber() + "\n");
                        msg.append("店铺名：" + shopDetail.getName() + "\n");
                        msg.append("订单时间：" + DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm") + "\n");
                        //BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
                        if (order.getServicePrice().compareTo(BigDecimal.ZERO) > 0) { //如果产生了服务费
                            if (order.getIsUseNewService().equals(Common.YES)){ //如果开通了新版服务费
                                if (order.getSauceFeeCount() != null && order.getSauceFeeCount() > 0){ //产生了餐具费
                                    msg.append(shopDetail.getSauceFeeName() + "：" + order.getSauceFeePrice() + "\n");
                                }
                                if (order.getTowelFeeCount() != null && order.getTowelFeeCount() > 0){ //产生了纸巾费
                                    msg.append(shopDetail.getTowelFeeName() + "：" + order.getTowelFeePrice() + "\n");
                                }
                                if (order.getTablewareFeeCount() != null && order.getTablewareFeeCount() > 0){ //产生了酱料费
                                    msg.append(shopDetail.getTablewareFeeName() + "：" + order.getTablewareFeePrice()  + "\n");
                                }
                            }else { //旧版服务费
                                msg.append(shopDetail.getServiceName() + "：" + order.getServicePrice() + "\n");
                            }
                        }
                        if (setting.getIsMealFee() == 1 && order.getDistributionModeId() == 3 && shopDetail.getIsMealFee() == 1) {
                            msg.append(shopDetail.getMealFeeName() + "：" + order.getMealFeePrice() + "\n");
                        }
                        BigDecimal sum = order.getOrderMoney();
                        List<Order> orders = orderService.selectByParentId(order.getId(), order.getPayType()); //得到子订单
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
                        if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayMode() != 3 && order.getPayMode() != 4 && order.getOrderBefore() == null) {
                            String url = "";
                            if (order.getParentOrderId() == null) {
                                url = setting.getWechatWelcomeUrl() + "?orderBossId=" + order.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                            } else {
                                Order o = orderService.selectById(order.getParentOrderId());
                                url = setting.getWechatWelcomeUrl() + "?orderBossId=" + o.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                            }
                            msg.append("<a href='" + url + "'>点击这里进行\"加菜\"或\"买单\"</a> \n");
                        }
                        if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                            String result = weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                        } else {
                            sendToGroupCustomerListMsg(order, msg.toString(), config, brand.getBrandName());
                        }
                    } else {
                        List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM408705883");
                        BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM408705883",TemplateSytle.BIG_BOSS);
                        if (templateFlowList != null && templateFlowList.size() != 0) {
                            String templateId = templateFlowList.get(0).getTemplateId();
                            String jumpUrl = "";
                            if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayMode() != 3 && order.getPayMode() != 4) {
                                if (order.getParentOrderId() == null) {
                                    jumpUrl = setting.getWechatWelcomeUrl() + "?orderBossId=" + order.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                                } else {
                                    Order o = orderService.selectById(order.getParentOrderId());
                                    jumpUrl = setting.getWechatWelcomeUrl() + "?orderBossId=" + o.getId() + "&articleBefore=1&dialog=closeRedPacket&shopId=" + order.getShopDetailId();
                                }
                            }
                            Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                            Map<String, Object> first = new HashMap<String, Object>();

                            if (order.getParentOrderId() == null) {
                                if(brandTemplateEdit!=null){
                                    if(brandTemplateEdit.getBigOpen()){
                                        first.put("value", brandTemplateEdit.getStartSign());
                                    }else {
                                        first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                    }
                                }else {
                                    first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                }

                            } else {
                                if(brandTemplateEdit!=null){
                                    if(brandTemplateEdit.getBigOpen()){
                                        first.put("value", brandTemplateEdit.getStartSign());
                                    }else {
                                        first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                    }
                                }else {
                                    first.put("value", "下单成功！\n您于" + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "的订单已下厨，请稍候~");
                                }
                            }
                            first.put("color", "#00DB00");
                            Map<String, Object> keyword1 = new HashMap<String, Object>();
                            keyword1.put("value", order.getSerialNumber());
                            keyword1.put("color", "#000000");
                            Map<String, Object> keyword2 = new HashMap<String, Object>();
                            keyword2.put("value", shopDetail.getName());
                            keyword2.put("color", "#000000");
                            Map<String, Object> keyword3 = new HashMap<String, Object>();
                            if (order.getOrderMode() == 2) {
                                keyword3.put("value", order.getVerCode());
                            } else {
                                keyword3.put("value", order.getTableNumber());
                            }
                            keyword3.put("color", "#000000");
                            BigDecimal sum = order.getOrderMoney();
                            List<Order> orders = orderService.selectByParentId(order.getId(), order.getPayType()); //得到子订单
                            for (Order child : orders) { //遍历子订单
                                sum = sum.add(child.getOrderMoney());
                            }
                            Map<String, Object> keyword4 = new HashMap<String, Object>();
                            keyword4.put("value", "￥" + sum);
                            keyword4.put("color", "#000000");
                            Map<String, Object> keyword5 = new HashMap<String, Object>();
                            Map<String, String> param = new HashMap<>();
                            param.put("orderId", order.getId());
                            List<OrderItem> orderItem = orderItemService.listByOrderId(param);
                            StringBuffer msg = new StringBuffer();
                            for (int i = 0; i < (orderItem.size() > 5 ? 6 : orderItem.size()); i++) {
                                OrderItem item = orderItem.get(i);
                                if (i == 0) {
                                    msg.append("\n\t\t\t" + item.getArticleName() + "×" + item.getCount() + "\n");
                                } else {
                                    msg.append("\t\t\t" + item.getArticleName() + "×" + item.getCount() + "\n");
                                }
                                if (orderItem.size() > 5) {
                                    msg.append("...");
                                }
                            }
                            keyword5.put("value", msg.toString());
                            keyword5.put("color", "#000000");
                            Map<String, Object> remark = new HashMap<String, Object>();
                            if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getOrderBefore() == null) {
                                if(brandTemplateEdit!=null){
                                    if(brandTemplateEdit.getBigOpen()){
                                        remark.put("value", brandTemplateEdit.getEndSign());
                                    }else {
                                        remark.put("value", "点击结果进行\"加菜\"或\"买单\"");
                                    }
                                }else {
                                    remark.put("value", "点击结果进行\"加菜\"或\"买单\"");
                                }

                                remark.put("color", "#173177");
                            }else{
                                remark.put("value", "");
                                remark.put("color", "#173177");
                            }
                            content.put("first", first);
                            content.put("keyword1", keyword1);
                            content.put("keyword2", keyword2);
                            content.put("keyword3", keyword3);
                            content.put("keyword4", keyword4);
                            content.put("keyword5", keyword5);
                            content.put("remark", remark);
                            if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                                String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", customer.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                smsParam.put("key1", order.getSerialNumber());
                                smsParam.put("key2", shopDetail.getName());
                                smsParam.put("key3", order.getTableNumber());
                                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                            } else {
                                String orderId = order.getId();
                                if(order.getParentOrderId() != null && !"".equals(order.getParentOrderId())){
                                    orderId = order.getParentOrderId();
                                }
                                List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                                for (Participant p : participants) {
                                    Customer c = customerService.selectById(p.getCustomerId());
                                    String result = weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                    Map map = new HashMap(4);
                                    map.put("brandName", brand.getBrandName());
                                    map.put("fileName", c.getId());
                                    map.put("type", "UserAction");
                                    map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                    doPostAnsc(map);
                                    //发送短信
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shopDetail.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            }
                        }
                    }
                }

                if (setting.getTemplateEdition() == 1 && setting.getOpenSmallProgramPush() == 1) {
                    List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM411055841");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM411055841",TemplateSytle.GIFT);

                    if (templateFlowList != null && templateFlowList.size() != 0) {
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String jumpUrl = "";

                        Map<String, Object> miniprogram = new HashMap<String, Object>();
                        miniprogram.put("appid",setting.getProductCouponPushUrl());
                        miniprogram.put("pagepath","pages/index/index?id=" + ApplicationUtils.randomUUID() + "&ts=" + (System.currentTimeMillis() / 1000));

                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();

                        if (brandTemplateEdit != null) {
                            if (brandTemplateEdit.getBigOpen()) {
                                first.put("value", brandTemplateEdit.getStartSign());
                            } else {
                                first.put("value", "召唤好友一起来【" + brand.getBrandName() + "】吧，我们等你很久啦！");
                            }
                        } else {
                            first.put("value", "召唤好友一起来【" + brand.getBrandName() + "】吧，我们等你很久啦！");
                        }
                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", "【"+brand.getBrandName()+"】小程序");
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        keyword2.put("color", "#000000");
                        Map<String, Object> remark = new HashMap<String, Object>();

                        if (brandTemplateEdit != null) {
                            if (brandTemplateEdit.getBigOpen()) {
                                remark.put("value", brandTemplateEdit.getEndSign());
                            } else {
                                remark.put("value", "点击查看福袋详情>>");
                            }
                        } else {
                            remark.put("value", "点击查看福袋详情>>");
                        }

                        remark.put("color", "#173177");
                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("remark", remark);
                        if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                            log.error("推送微信消息:" + content.toString());
                            String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, miniprogram, content, config.getAppid(), config.getAppsecret());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                        } else {
                            String orderId = order.getId();
                            if(order.getParentOrderId() != null && !"".equals(order.getParentOrderId())){
                                orderId = order.getParentOrderId();
                            }
                            List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                            for (Participant p : participants) {
                                Customer c = customerService.selectById(p.getCustomerId());
                                log.error("推送微信消息:" + content.toString());
                                String result = weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", c.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                            }
                        }
                    }
                }
            }
        }
    }

    //推送分享领红包，跳转到我的二维码界面

    public void scanaQRcode(WechatConfig config, Customer customer, BrandSetting setting, Order order) {
        StringBuffer str = new StringBuffer();
        Brand brand = brandService.selectById(order.getBrandId());
        ShareSetting shareSetting = shareSettingService.selectByBrandId(customer.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        List<NewCustomCoupon> coupons = newcustomcouponService.selectListByCouponType(customer.getBrandId(), 1, order.getShopDetailId());
        if (coupons.size() > 0) {
            BigDecimal money = new BigDecimal("0.00");
            for (NewCustomCoupon coupon : coupons) {
                money = money.add(coupon.getCouponValue().multiply(new BigDecimal(coupon.getCouponNumber())));
            }
            str.append("邀请朋友扫一扫，");
            if (money.doubleValue() == 0.00 && shareSetting == null) {
                str.append("送他/她红包，朋友到店消费后，您将获得红包返利\n");
            } else if (money.doubleValue() == 0.00) {
                str.append("送他/她" + money + "元红包，朋友到店消费后，您将获得红包返利\n");
            } else if (shareSetting == null) {
                str.append("送他/她红包，朋友到店消费后，您将获得" + shareSetting.getMinMoney() + "元-" + shareSetting.getMaxMoney() + "元红包返利\n");
            } else {
                str.append("送他/她" + money + "元红包，朋友到店消费后，您将获得" + shareSetting.getMinMoney() + "元-" + shareSetting.getMaxMoney() + "元红包返利\n");
            }
            String jumpurl = setting.getWechatWelcomeUrl() + "?dialog=scanAqrCode&subpage=my&shopId=" + order.getShopDetailId();

            if (setting.getTemplateEdition() == 0) {
                str.append("<a href='" + jumpurl + "'>打开邀请二维码</a>");
                if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                    String result = weChatService.sendCustomerMsg(str.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + str.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                } else {
                    sendToGroupCustomerListMsg(order, str.toString(), config, brand.getBrandName());
                }
            } else {
                List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM207012446");
                BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM207012446",TemplateSytle.INVITE_FRIEND);
                if (templateFlowList != null && templateFlowList.size() != 0) {
                    String templateId = templateFlowList.get(0).getTemplateId();
                    Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                    Map<String, Object> first = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            first.put("value", brandTemplateEdit.getStartSign());
                        }else {
                            first.put("value", str.toString());
                        }
                    }else {
                        first.put("value", str.toString());
                    }

                    first.put("color", "#00DB00");
                    Map<String, Object> keyword1 = new HashMap<String, Object>();
                    keyword1.put("value", order.getSerialNumber());
                    keyword1.put("color", "#000000");
                    Map<String, Object> keyword2 = new HashMap<String, Object>();
                    keyword2.put("value", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    keyword2.put("color", "#000000");
                    Map<String, Object> remark = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            first.put("value", brandTemplateEdit.getStartSign());
                        }else {
                            remark.put("value", "打开邀请二维码");
                        }
                    }else {
                        remark.put("value", "打开邀请二维码");
                    }

                    remark.put("color", "#173177");
                    content.put("first", first);
                    content.put("keyword1", keyword1);
                    content.put("keyword2", keyword2);
                    content.put("remark", remark);
                    if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                        String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpurl, content, config.getAppid(), config.getAppsecret());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    } else {
                        String orderId = order.getId();
                        if(order.getParentOrderId() != null && !"".equals(order.getParentOrderId())){
                            orderId = order.getParentOrderId();
                        }
                        List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                        for (Participant p : participants) {
                            Customer c = customerService.selectById(p.getCustomerId());
                            String result = weChatService.sendTemplate(c.getWechatId(), templateId, jumpurl, content, config.getAppid(), config.getAppsecret());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", c.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                        }
                    }
                } else {
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
        }
    }

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.afterPayShareBenefits(..))")
    public void afterPayShareBenefits() {
    }

    @AfterReturning(value = "confirmOrder()||confirmBossOrder()||confirmWaiMaiOrder()||afterPayShareBenefits()", returning = "jsonResult")
    public void confirmOrderAfter(JSONResult jsonResult) {
        if(!jsonResult.isSuccess()){
            return;
        }
        Order order = (Order) jsonResult.getData();
        if (order != null) {
            log.info("确认订单成功后回调:" + order.getId());
            Customer customer = customerService.selectById(order.getCustomerId());
            if (customer == null) {
                return;
            }
            if (order.getOrderState() != OrderState.CONFIRM) {
                return;
            }
            WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            BrandSetting setting = brandSettingService.selectByBrandId(customer.getBrandId());
            Brand brand = brandService.selectById(customer.getBrandId());
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
//		RedConfig redConfig = redConfigService.selectListByShopId(order.getShopDetailId());
            if (order.getAllowAppraise()) {
                if (setting.getTemplateEdition() == 0) {
                    StringBuffer msg = new StringBuffer();
                    msg.append("您有一个红包未领取，红包是" + brand.getBrandName() + "送您的一片心意xoxo");
                    msg.append("<a href='" + setting.getWechatWelcomeUrl() + "?subpage=my&dialog=redpackage&orderId=" + order.getId() + "&shopId=" + order.getShopDetailId() + "'>点击领取</a>");
                    if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                        String result = weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    } else {
                        sendToGroupCustomerListMsg(order, msg.toString(), config, brand.getBrandName());
                    }
                } else {
                    List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM207012446");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM207012446",TemplateSytle.COMMENT);
                    if (templateFlowList != null && templateFlowList.size() != 0) {
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String jumpUrl = setting.getWechatWelcomeUrl() + "?subpage=my&dialog=redpackage&orderId=" + order.getId() + "&shopId=" + order.getShopDetailId();
                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();

                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                first.put("value", brandTemplateEdit.getStartSign());
                            }else {
                                first.put("value", "您有一个红包未领取，红包是" + brand.getBrandName() + "送您的一片心意xoxo");
                            }
                        }else {
                            first.put("value", "您有一个红包未领取，红包是" + brand.getBrandName() + "送您的一片心意xoxo");
                        }

                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", order.getSerialNumber());
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                        keyword2.put("color", "#000000");
                        Map<String, Object> remark = new HashMap<String, Object>();

                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                remark.put("value", brandTemplateEdit.getEndSign());
                            }else {
                                remark.put("value", "点击领取红包");
                            }
                        }else {
                            remark.put("value", "点击领取红包");
                        }

                        remark.put("color", "#173177");
                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("remark", remark);
                        if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                            String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                            //发送短信
                            if (setting.getMessageSwitch() == 1) {
                                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                smsParam.put("name", brand.getBrandName());
                                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105945069");
                            }
                        } else {
                            String orderId = order.getId();
                            if(order.getParentOrderId() != null && !"".equals(order.getParentOrderId())){
                                orderId = order.getParentOrderId();
                            }
                            List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                            for (Participant p : participants) {
                                Customer c = customerService.selectById(p.getCustomerId());
                                String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", customer.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if (setting.getMessageSwitch() == 1) {
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("name", brand.getBrandName());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105945069");
                                }
                            }
                        }
                    }else{
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
//            log.info("发送评论通知成功:" + msg + result);

                    scanaQRcode(config, customer, setting, order);
                }
                try {
                    if (customer.getFirstOrderTime() == null) { //分享判定
                        customerService.updateFirstOrderTime(customer.getId());
                        if (customer.getShareCustomer() != null) {
                            Customer shareCustomer = customerService.selectById(customer.getShareCustomer());
                            if (shareCustomer != null) {
                                ShareSetting shareSetting = shareSettingService.selectValidSettingByBrandId(customer.getBrandId());
                                if (shareSetting != null) {
                                    log.info("是被分享用户，并且分享设置已启用:" + customer.getId() + " oid:" + order.getId() + " setting:" + shareSetting.getId());
                                    BigDecimal rewardMoney = customerService.rewareShareCustomer(shareSetting, order, shareCustomer, customer);
                                    log.info("准备发送返利通知");
                                    sendRewardShareMsg(shareCustomer, customer, config, setting, rewardMoney, order);
                                } else {
                                    log.info("准备发送返利通知  but品牌没有设置返利  so返利0元");
                                    sendRewardShareMsg(shareCustomer, customer, config, setting, BigDecimal.ZERO, order);
                                }
                            }
                        }
                    } else {
                        if (customer.getShareCustomer() != null) {
                            Customer shareCustomer = customerService.selectById(customer.getShareCustomer());
                            if (shareCustomer != null) {
                                ShareSetting shareSetting = shareSettingService.selectValidSettingByBrandId(customer.getBrandId());
                                if (shareSetting != null && shareSetting.getOpenMultipleRebates() == 1) {
                                    BigDecimal rewardMoney = customerService.rewareShareCustomerAgain(shareSetting, order, shareCustomer, customer);
                                    log.info("准备发送返利通知");
                                    sendRewardShareMsg(shareCustomer, customer, config, setting, rewardMoney, order);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("分享功能出错:" + e.getMessage());
                    e.printStackTrace();
                }
                Date now = new Date();
                //判断这比订单是否属于   1:1 消费返利的订单
                if (setting.getConsumptionRebate() == 1 && shopDetail.getConsumptionRebate() == 1
                        && shopDetail.getRebateTime().compareTo(now) == 1) {
                    Order o = orderService.selectById(order.getId());
                    o.setIsConsumptionRebate(1);
                    orderService.update(o);
                    Account account = accountService.selectById(customer.getAccountId());
                    account.setFrozenRemain(account.getFrozenRemain().add((o.getAmountWithChildren().doubleValue() > 0 ? o.getAmountWithChildren() : o.getOrderMoney())));
                    accountService.update(account);
                    AccountLog acclog = new AccountLog();
                    acclog.setCreateTime(new Date());
                    acclog.setId(ApplicationUtils.randomUUID());
                    acclog.setMoney(o.getAmountWithChildren().doubleValue() > 0 ? o.getAmountWithChildren() : o.getOrderMoney());
                    acclog.setRemain(account.getRemain());
                    acclog.setPaymentType(AccountLogType.FROZEN);
                    acclog.setRemark("1:1消费返利冻结余额");
                    acclog.setAccountId(account.getId());
                    acclog.setSource(AccountLog.FREEZE_RED_MONEY);
                    acclog.setShopDetailId(shopDetail.getId());
                    acclog.setOrderId(o.getId());
                    acclog.setFreezeReturnDate(shopDetail.getRebateTime());
                    accountLogService.insert(acclog);

                    StringBuffer msg = new StringBuffer();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(acclog.getFreezeReturnDate());
                    String date = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
                    msg.append("太好啦，" + shopDetail.getName() + "送给您" + (o.getAmountWithChildren().doubleValue() > 0 ? o.getAmountWithChildren() : o.getOrderMoney()) + "元的返利红包，" + date + "后即可使用！");
                    msg.append("<a href='" + setting.getWechatWelcomeUrl() + "?subpage=my&dialog=myYue&shopId=" + order.getShopDetailId() + "'>查看余额</a>");

                    String result = weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
        }
    }

    private void sendRewardShareMsg(Customer shareCustomer, Customer customer, WechatConfig config,
                                    BrandSetting setting, BigDecimal rewardMoney, Order order) {
        StringBuffer msg = new StringBuffer();
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        if (rewardMoney.compareTo(BigDecimal.ZERO) != 0) {
            rewardMoney = rewardMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (setting.getTemplateEdition() == 0) {
            msg.append("您邀请的好友" + customer.getNickname() + "已到店消费，您已获得" + rewardMoney + "元红包返利\n<a href='" + setting.getWechatWelcomeUrl() + "?subpage=my&dialog=myYue'>点击查看余额！</a>");
//        msg.append("<a href='" + setting.getWechatWelcomeUrl() + "?subpage=my&dialog=myYue'>")
//                .append("您邀请的好友").append(customer.getNickname()).append("已到店消费，您已获得")
//                .append(rewardMoney).append("元红包返利").append("</a>");
            String result = weChatService.sendCustomerMsg(msg.toString(), shareCustomer.getWechatId(), config.getAppid(), config.getAppsecret());
            //logBaseService.insertLogBaseInfoState(shopDetailService.selectById(order.getShopDetailId()),customer,shareCustomer.getId(),LogBaseState.FIRST_SHARE_PAY);
//        log.info("发送返利通知成功:" + shareCustomer.getId() + " MSG: " + msg + result);
//                "订单发送推送：" + msg.toString());
        } else {
            List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM207012446");
            BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM207012446",TemplateSytle.PAY_MONEY);
            if (templateFlowList != null && templateFlowList.size() != 0) {
                String templateId = templateFlowList.get(0).getTemplateId();
                String jumpUrl = setting.getWechatWelcomeUrl() + "?subpage=my&dialog=myYue";
                Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                Map<String, Object> first = new HashMap<String, Object>();

                if(brandTemplateEdit!=null){
                    if(brandTemplateEdit.getBigOpen()){
                        first.put("value", brandTemplateEdit.getStartSign());
                    }else {
                        first.put("value", "您邀请的好友" + customer.getNickname() + "已到店消费，您已获得" + rewardMoney + "元红包返利");
                    }
                }else {
                    first.put("value", "您邀请的好友" + customer.getNickname() + "已到店消费，您已获得" + rewardMoney + "元红包返利");
                }

                first.put("color", "#00DB00");
                Map<String, Object> keyword1 = new HashMap<String, Object>();
                keyword1.put("value", order.getSerialNumber());
                keyword1.put("color", "#000000");
                Map<String, Object> keyword2 = new HashMap<String, Object>();
                keyword2.put("value", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                keyword2.put("color", "#000000");
                Map<String, Object> remark = new HashMap<String, Object>();

                if(brandTemplateEdit!=null){
                    if(brandTemplateEdit.getBigOpen()){
                        remark.put("value", brandTemplateEdit.getEndSign());
                    }else {
                        remark.put("value", "查看余额");
                    }
                }else {
                    remark.put("value", "查看余额");
                }

                remark.put("color", "#173177");
                content.put("first", first);
                content.put("keyword1", keyword1);
                content.put("keyword2", keyword2);
                content.put("remark", remark);
                String result1 = weChatService.sendTemplate(shareCustomer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", shareCustomer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:" + shareCustomer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                //发送短信
                if (setting.getMessageSwitch() == 1) {
                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                    smsParam.put("name", customer.getNickname());
                    smsParam.put("money", rewardMoney);
                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105945071");
                }
            } else {
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            }
        }
    }

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.cancelOrderPos(..))")
    public void cancelOrderPos() {
    }

    ;

    @AfterReturning(value = "cancelOrderPos()", returning = "order")
    public void cancelOrderPosAfter(Order order) throws Throwable {
        if (order != null) {
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            if (setting.getTemplateEdition() == 0) {
                Customer customer = customerService.selectById(order.getCustomerId());
                if (customer != null) {
                    WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
                    ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
                    Brand brand = brandService.selectById(order.getBrandId());
                    StringBuffer msg = new StringBuffer();
                    msg.append("您好，您 " + DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm") + " 的订单" + "已被商家取消\n");
                    msg.append("订单编号:\n" + order.getSerialNumber() + "\n");
                    if (order.getOrderMode() != null) {
                        switch (order.getOrderMode()) {
                            case ShopMode.TABLE_MODE:
                                msg.append("桌号:" + order.getTableNumber() + "\n");
                                break;
                            case ShopMode.BOSS_ORDER:
                                msg.append("桌号:" + order.getTableNumber() + "\n");
                                break;
                            default:
                                msg.append("消费码：" + order.getVerCode() + "\n");
                                break;
                        }
                    }
                    if (order.getShopName() == null || "".equals(order.getShopName())) {
                        order.setShopName(shopDetail.getName());
                    }
                    msg.append("店铺名：" + order.getShopName() + "\n");
                    msg.append("订单时间：" + DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm") + "\n");
                    //BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());

                    if (setting.getIsUseServicePrice() == 1 && shopDetail.getIsUseServicePrice() == 1 && order.getDistributionModeId() == 1) {
                        msg.append(shopDetail.getServiceName() + "：" + order.getServicePrice() + "\n");
                    }
                    if (setting.getIsMealFee() == 1 && order.getDistributionModeId() == 3 && shopDetail.getIsMealFee() == 1) {
                        msg.append(shopDetail.getMealFeeName() + "：" + order.getMealFeePrice() + "\n");
                    }
                    msg.append("订单明细：\n");
                    Map<String, String> param = new HashMap<>();
                    param.put("orderId", order.getId());
                    List<OrderItem> orderItem = orderItemService.listByOrderId(param);
                    for (OrderItem item : orderItem) {
                        if (item.getCount() > 0) {
                            msg.append("  " + item.getArticleName() + "x" + item.getCount() + "\n");
                        }
                    }
                    msg.append("订单金额：" + order.getOrderMoney() + "\n");

                    String result = weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                    //            log.info("发送订单取消通知成功:" + msg + result);
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            } else {
                DecimalFormat df = new DecimalFormat("0.00");
                Customer customer = customerService.selectById(order.getCustomerId());
                if (customer != null) {
                    WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
                    ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
                    Brand brand = brandService.selectById(order.getBrandId());
                    List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM402104200");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM402104200",TemplateSytle.REFUSE_ORDER);
                    if (templateFlowList != null && templateFlowList.size() != 0) {
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String jumpUrl = "";
                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();

                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                first.put("value", brandTemplateEdit.getStartSign());
                            }else {
                                first.put("value", "您好，您的订单已被商家取消");
                            }
                        }else {
                            first.put("value", "您好，您的订单已被商家取消");
                        }

                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", df.format(order.getOrderMoney()));
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm"));
                        keyword2.put("color", "#000000");
                        Map<String, Object> keyword3 = new HashMap<String, Object>();
                        keyword3.put("value", "已取消");
                        keyword3.put("color", "#000000");
                        Map<String, Object> remark = new HashMap<String, Object>();

                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                remark.put("value", brandTemplateEdit.getEndSign());
                            }else {
                                remark.put("value", "请您确认新的订单情况");
                            }
                        }else {
                            remark.put("value", "请您确认新的订单情况");
                        }

                        remark.put("color", "#173177");
                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("keyword3", keyword3);
                        content.put("remark", remark);
                        String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    } else {
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
                }
            }
            mqMessageProducer.sendNoticeOrderMessage(order);

            if (order.getParentOrderId() != null) {  //子订单
                orderService.updateOrderChild(order.getId());
            }
//			//拒绝订单后还原库存
//			Boolean addStockSuccess  = false;
//			addStockSuccess	= orderService.addStock(orderService.getOrderInfo(order.getId()));
//			if(!addStockSuccess){
//				log.info("库存还原失败:"+order.getId());
//			}

            Order o = orderService.getOrderAccount(order.getShopDetailId());
            redisService.set(order.getShopDetailId() + "shopOrderCount", o.getOrderCount());
            if(o.getOrderTotal() != null){
                redisService.set(order.getShopDetailId() + "shopOrderTotal", o.getOrderTotal());
            }
            mqMessageProducer.sendPrintSuccess(order.getShopDetailId());
            pushOrderMoneyAndCountToPos(o.getOrderCount(), o.getOrderTotal(), order.getShopDetailId());
        }
    }

    private void sendVerCodeMsg(Order order) {
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            Brand brand = brandService.selectById(order.getBrandId());
            StringBuffer msg = new StringBuffer();
            msg.append("交易码:" + order.getVerCode() + "\n");
            msg.append("<a href='" + setting.getWechatWelcomeUrl() + "?subpage=my&waitQueue=" + order.getId() + "&shopId=" + order.getShopDetailId() + "'>请留意叫号信息</a>");
            String result = weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
            Map map = new HashMap(4);
            map.put("brandName", brand.getBrandName());
            map.put("fileName", customer.getId());
            map.put("type", "UserAction");
            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
//        log.info("发送取餐信息成功:" + result);
        }
    }

    @Pointcut("execution(* com.resto.shop.web.service.OrderPaymentItemService.insertByBeforePay(..))")
    public void insertByBeforePay() {

    }

    ;

    @AfterReturning(value = "insertByBeforePay()", returning = "orderPaymentItem")
    public void insertByBeforePay(OrderPaymentItem orderPaymentItem) {
        Order order = orderService.selectById(orderPaymentItem.getOrderId());
        mqMessageProducer.sendPlaceOrderNoPayMessage(order);
    }

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.colseOrder(..))")
    public void colseOrder() {

    }

    ;

    @AfterReturning(value = "colseOrder()", returning = "order")
    public void colseOrder(Order order) {
        Brand brand = brandService.selectById(order.getBrandId());
        Customer customer = customerService.selectById(order.getCustomerId());
        WechatConfig config = wechatConfigService.selectByBrandId(brand.getId());
        StringBuilder sb = new StringBuilder("亲,今日未完成支付的订单已被系统自动取消,欢迎下次再来本店消费\n");
        sb.append("订单编号:" + order.getSerialNumber() + "\n");
        if (order.getOrderMode() != null) {
            switch (order.getOrderMode()) {
                case ShopMode.TABLE_MODE:
                    sb.append("桌号:" + (order.getTableNumber() != null ? order.getTableNumber() : "无") + "\n");
                    break;
                default:
                    sb.append("消费码：" + (order.getVerCode() != null ? order.getVerCode() : "无") + "\n");
                    break;
            }
        }
        if (order.getShopName() == null || "".equals(order.getShopName())) {
            order.setShopName(shopDetailService.selectById(order.getShopDetailId()).getName());
        }
        sb.append("店铺名：" + order.getShopName() + "\n");
        sb.append("订单时间：" + DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm") + "\n");
        sb.append("订单明细：\n");
        Map<String, String> param = new HashMap<>();
        param.put("orderId", order.getId());
        List<OrderItem> orderItem = orderItemService.listByOrderId(param);
        for (OrderItem item : orderItem) {
            sb.append("  " + item.getArticleName() + "x" + item.getCount() + "\n");
        }
        sb.append("订单金额：" + order.getOrderMoney() + "\n");
        weChatService.sendCustomerMsgASync(sb.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
    }

    /**
     * 切到评论方法
     */
    @Pointcut("execution(* com.resto.shop.web.service.AppraiseService.saveAppraise(..))")
    public void saveAppraise() {
    }


    /**
     * 订单评论完成后执行， 如是差评则发送消息队列执行打单操作
     *
     * @param appraise
     */
    @AfterReturning(value = "saveAppraise()", returning = "appraise")
    public void saveAppraise(com.resto.api.appraise.entity.Appraise  appraise) {
        if (appraise != null) {
            log.info("订单评论完成");
            ShopDetail shopDetail = shopDetailService.selectById(appraise.getShopDetailId());
            Brand brand = brandService.selectById(shopDetail.getBrandId());
            //判断是否开启差评打单且满足差评条件则打印订单
            if (shopDetail.getOpenBadAppraisePrintOrder() && appraise.getLevel() <= 4) {
                log.info("订单评论满足差评推送消息队列");
                //发送队列消息
                mqMessageProducer.sendBadAppraisePrintOrderMessage(appraise.getOrderId(), appraise.getShopDetailId());
                pushBadAppraiseToPos(appraise.getOrderId(), shopDetail.getId());
            }
            //差评预警
            if (shopDetail.getOpenBadWarning().equals(Common.YES)) {
                badWarning(brand, shopDetail, appraise);
            }
        }
    }


    private void pushBadAppraiseToPos(String orderId, String shopId){
        JSONObject data = new JSONObject();
        data.put("id", orderId);
        data.put("dataType", "badAppraisePrintOrder");
        data.put("shopId", shopId);
        mqttMQMessageProducer.sendBadAppraiseToPosMessage(data.toString());
    }


    /**
     * 差评预警
     *
     * @param shopDetail
     * @param appraise
     */
    private final void badWarning(Brand brand, ShopDetail shopDetail, com.resto.api.appraise.entity.Appraise  appraise) {
        //得到所有差评预警的关键字
        String[] warningKeys = shopDetail.getWarningKey().split(",");
        //标记评价内容当中是否产生预警关键字
        Boolean flg = false;
        //评价当中存在的关键字
        StringBuffer useWarningKeys = new StringBuffer();
        for (String warningKey : warningKeys) {
            //判断评价内容当中是否存在关键字
            if (appraise.getContent().indexOf(warningKey) != -1) {
                flg = true;
                useWarningKeys.append("\"" + warningKey + "\"").append("、");
            }
        }
        if (flg) {
            //发送信息
            sendNotificationMessage(brand, shopDetail, appraise, useWarningKeys.toString());
        }
    }

    /**
     * 发送通知信息
     *
     * @param shopDetail
     * @param appraise
     */
    private final void sendNotificationMessage(Brand brand, ShopDetail shopDetail, com.resto.api.appraise.entity.Appraise  appraise, String warningKey) {
        //信息模板
        String sendMessage = "餐加提醒您：${customer}为${telephone}的${sex}用户，评价了${datetime}在${shopname}消费的订单，包含${warningkey}关键词。评价内容：${appraise}请及时处理！";
        //存储关键信息
        com.alibaba.fastjson.JSONObject stringMap = new com.alibaba.fastjson.JSONObject();
        //查询相关信息
        Customer customer = customerService.selectById(appraise.getCustomerId());
        Order order = orderService.selectById(appraise.getOrderId());
        WechatConfig wechatConfig = wechatConfigService.selectByBrandId(shopDetail.getBrandId());
        //封装数据
        stringMap.put("customer", customer.getTelephone() != null ? "手机号" : "微信昵称");
        stringMap.put("telephone", customer.getTelephone() != null ? customer.getTelephone() : customer.getNickname());
        if (customer.getSex().equals(1)) {
            stringMap.put("sex", "男性");
        } else if (customer.getSex().equals(2)) {
            stringMap.put("sex", "女性");
        } else {
            stringMap.put("sex", "未知");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        stringMap.put("datetime", format.format(order.getCreateTime()));
        stringMap.put("shopname", shopDetail.getName());
        stringMap.put("warningkey", warningKey);
        stringMap.put("appraise", "\""+appraise.getContent()+"\"");
        //格式转换
        //模板转换工具类
        StrSubstitutor substitutor = new StrSubstitutor(stringMap);
        sendMessage = substitutor.replace(sendMessage);
        //获取结店录入的电话号码
        if (StringUtils.isNotBlank(shopDetail.getNoticeTelephone())) {
            //将各个电话号码截取出来
            String[] telephones = shopDetail.getNoticeTelephone().split(",");
            //如果开通了预警微信推送
            if (Common.YES.equals(shopDetail.getWarningWechat())) {
                //将电话号码转换为JSON字符串
                String jsonString = JSON.toJSONString(telephones);
                //得到电话号码在系统所对应的微信用户
                List<String> customerTelephones = JSON.parseObject(jsonString, new TypeReference<List<String>>() {
                });
                List<Customer> customers = customerService.selectByTelePhones(customerTelephones);
                for (Customer shopCustomer : customers) {
                    //发送推送
                    weChatService.sendCustomerMsg(sendMessage, shopCustomer.getWechatId(), wechatConfig.getAppid(), wechatConfig.getAppsecret());
                }
            }
            //如果开通了预警短信推送
            if (Common.YES.equals(shopDetail.getWarningSms())) {
                for (String telephone : telephones) {
                    smsLogService.sendMessage(brand.getId(), shopDetail.getId(), SmsLogType.WARNING, SMSUtils.SIGN, SMSUtils.SMS_BAD_WARNING, telephone, stringMap);
                }
            }
        }
    }

    public void sendToGroupCustomerListMsg(Order order, String msg, WechatConfig config, String brandName) {
        String orderId = order.getId();
        if(order.getParentOrderId() != null && !"".equals(order.getParentOrderId())){
            orderId = order.getParentOrderId();
        }
        List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
        for (Participant p : participants) {
            Customer c = customerService.selectById(p.getCustomerId());
            String result = weChatService.sendCustomerMsg(msg.toString(), c.getWechatId(), config.getAppid(), config.getAppsecret());
            Map map = new HashMap(4);
            map.put("brandName", brandName);
            map.put("fileName", c.getId());
            map.put("type", "UserAction");
            map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
        }
    }

    @Pointcut("execution(* com.resto.shop.web.service.impl.OrderServiceImpl.payOrderSuccess(..))")
    public void payOrderSuccessPushMsg() {

    }
    @AfterReturning(value = "payOrderSuccessPushMsg()", returning = "order")
    public void payOrderSuccessPushMsg(Order order){
        //gigtPush(order);
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        Customer customer = customerService.selectById(order.getCustomerId());
        if(customer != null) {
            if(order.getOrderState()==2){
                WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM414174151");
                BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM414174151", TemplateSytle.TICKET);

                if (templateFlowList != null && templateFlowList.size() != 0) {
                    String templateId = templateFlowList.get(0).getTemplateId();
                    String baseUrl = UrlUtils.getIP(URI.create(setting.getWechatWelcomeUrl())).toString();
                    Integer ticketType = 0;
                    if(setting.getOpenTicket()==1){
                        ticketType = 1;
                    }
                    if(setting.getOpenTicket()==2){

                    }
                    String jumpUrl = baseUrl+"/restowechat/src/views/invoice.html?baseUrl="+baseUrl+"&invoiceType="+2+"&shopId="+shop.getId()+"&customerId="+customer.getId()+"&ticketType="+ticketType;
                    Order o = orderService.selectById(order.getParentOrderId());

                    Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                    Map<String, Object> first = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            first.put("value", brandTemplateEdit.getStartSign());
                        }else {
                            first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                        }
                    }else {
                        first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                    }

                    first.put("color", "#00DB00");
                    Map<String, Object> keyword1 = new HashMap<String, Object>();
                    keyword1.put("value", shop.getName());
                    keyword1.put("color", "#000000");
                    Map<String, Object> keyword2 = new HashMap<String, Object>();
                    keyword2.put("value", "餐饮");
                    keyword2.put("color", "#000000");
                    Map<String, Object> keyword3 = new HashMap<String, Object>();
                    BigDecimal money = orderPaymentItemMapper.selectEnableTicketMoney(order.getId());
                    if(money==null){
                        setting.setOpenTicket(0);
                        money = BigDecimal.valueOf(0);
                    }
                    keyword3.put("value", money.toString()+"(可开票金额)");
                    keyword3.put("color", "#000000");
                    Map<String, Object> keyword4 = new HashMap<String, Object>();
                    keyword4.put("value", "30天");
                    keyword4.put("color", "#000000");

                    Map<String, Object> remark = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            remark.put("value", brandTemplateEdit.getEndSign());
                        }else {
                            remark.put("value", "请点击详情进入订单列表开具发票");
                        }
                    }else {
                        remark.put("value", "请点击详情进入订单列表开具发票");
                    }

                    remark.put("color", "#173177");

                    content.put("first", first);
                    content.put("keyword1", keyword1);
                    content.put("keyword2", keyword2);
                    content.put("keyword3", keyword3);
                    content.put("keyword4", keyword4);
                    content.put("remark", remark);
                    //String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                    Brand brand = brandService.selectById(order.getBrandId());
                    if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                        if(setting.getOpenTicket()!=0){
                            weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                            //发送短信
                            if (setting.getMessageSwitch() == 1) {
                                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                smsParam.put("key1", order.getSerialNumber());
                                smsParam.put("key2", shop.getName());
                                smsParam.put("key3", order.getTableNumber());
                                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                            }
                        }

                    } else {
                        String orderId = order.getId();
                        if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
                            orderId = order.getParentOrderId();
                        }
                        List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                        for (Participant p : participants) {
                            if(setting.getOpenTicket()!=0){
                                Customer c = customerService.selectById(p.getCustomerId());
                                weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", c.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if (setting.getMessageSwitch() == 1) {
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shop.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            }

                        }
                    }
                } else {
                    Brand brand = brandService.selectById(order.getBrandId());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
        }
    }

    @Pointcut("execution(* com.resto.shop.web.service.OrderService.posPayOrder(..))")
    public void posPayOrderPushMsg() {
    }

    @AfterReturning(value = "posPayOrderPushMsg()", returning = "order")
    public void posPayOrderPushMsg(Order order){
        //gigtPush(order);
        mqMessageProducer.sendPlaceOrderNoPayMessage(order);
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        Customer customer = customerService.selectById(order.getCustomerId());
        pushCreateOrderToPos(order.getId());
        if(customer != null) {
            if(order.getOrderState()==OrderState.PAYMENT){
                WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM414174151");
                BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM414174151", TemplateSytle.TICKET);

                if (templateFlowList != null && templateFlowList.size() != 0) {
                    String templateId = templateFlowList.get(0).getTemplateId();
                    String baseUrl = UrlUtils.getIP(URI.create(setting.getWechatWelcomeUrl())).toString();
                    Integer ticketType = 0;
                    if(setting.getOpenTicket()==1){
                        ticketType = 1;
                    }
                    if(setting.getOpenTicket()==2){

                    }
                    String jumpUrl = baseUrl+"/restowechat/src/views/invoice.html?baseUrl="+baseUrl+"&invoiceType="+2+"&shopId="+shop.getId()+"&customerId="+customer.getId()+"&ticketType="+ticketType;

                    Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                    Map<String, Object> first = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            first.put("value", brandTemplateEdit.getStartSign());
                        }else {
                            first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                        }
                    }else {
                        first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                    }

                    first.put("color", "#00DB00");
                    Map<String, Object> keyword1 = new HashMap<String, Object>();
                    keyword1.put("value", shop.getName());
                    keyword1.put("color", "#000000");
                    Map<String, Object> keyword2 = new HashMap<String, Object>();
                    keyword2.put("value", "餐饮");
                    keyword2.put("color", "#000000");
                    Map<String, Object> keyword3 = new HashMap<String, Object>();
                    BigDecimal money = orderPaymentItemMapper.selectEnableTicketMoney(order.getId());
                    if(money==null){
                        setting.setOpenTicket(0);
                        money = BigDecimal.valueOf(0);
                    }
                    keyword3.put("value", money.toString()+"(可开票金额)");
                    keyword3.put("color", "#000000");
                    Map<String, Object> keyword4 = new HashMap<String, Object>();
                    keyword4.put("value", "30天");
                    keyword4.put("color", "#000000");

                    Map<String, Object> remark = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            remark.put("value", brandTemplateEdit.getEndSign());
                        }else {
                            remark.put("value", "请点击详情进入订单列表开具发票");
                        }
                    }else {
                        remark.put("value", "请点击详情进入订单列表开具发票");
                    }

                    remark.put("color", "#173177");

                    content.put("first", first);
                    content.put("keyword1", keyword1);
                    content.put("keyword2", keyword2);
                    content.put("keyword3", keyword3);
                    content.put("keyword4", keyword4);
                    content.put("remark", remark);
                    //String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                    Brand brand = brandService.selectById(order.getBrandId());
                    if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                        if(setting.getOpenTicket()!=0){
                            weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                            //发送短信
                            if (setting.getMessageSwitch() == 1) {
                                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                smsParam.put("key1", order.getSerialNumber());
                                smsParam.put("key2", shop.getName());
                                smsParam.put("key3", order.getTableNumber());
                                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                            }
                        }

                    } else {
                        String orderId = order.getId();
                        if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
                            orderId = order.getParentOrderId();
                        }
                        List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                        for (Participant p : participants) {
                            if(setting.getOpenTicket()!=0){
                                Customer c = customerService.selectById(p.getCustomerId());
                                weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", c.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if (setting.getMessageSwitch() == 1) {
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shop.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            }

                        }
                    }
                } else {
                    Brand brand = brandService.selectById(order.getBrandId());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
        }
    }

    @Pointcut("execution(* com.resto.shop.web.service.PosService.confirmPayment(..))")
    public void confirmPaymentPushMsg() {
    }

    @AfterReturning(value = "confirmPaymentPushMsg()",returning = "data")
    public void confirmPaymentPushMsg(String data){
        JSONObject object = new JSONObject(data);
        Brand brand = brandService.selectById(object.getString("brandId"));
        //要修改的订单信息
        String oId = object.getString("orderId");
        Order order = orderService.selectById(oId);
        //gigtPush(order);
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        Customer customer = customerService.selectById(order.getCustomerId());
        if(customer != null) {
            if(order.getOrderState()==2){
                WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM414174151");
                BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM414174151", TemplateSytle.TICKET);

                if (templateFlowList != null && templateFlowList.size() != 0) {
                    String templateId = templateFlowList.get(0).getTemplateId();
                    String baseUrl = UrlUtils.getIP(URI.create(setting.getWechatWelcomeUrl())).toString();
                    Integer ticketType = 0;
                    if(setting.getOpenTicket()==1){
                        ticketType = 1;
                    }
                    if(setting.getOpenTicket()==2){

                    }
                    String jumpUrl = baseUrl+"/restowechat/src/views/invoice.html?baseUrl="+baseUrl+"&invoiceType="+2+"&shopId="+shop.getId()+"&customerId="+customer.getId()+"&ticketType="+ticketType;

                    Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                    Map<String, Object> first = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            first.put("value", brandTemplateEdit.getStartSign());
                        }else {
                            first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                        }
                    }else {
                        first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                    }

                    first.put("color", "#00DB00");
                    Map<String, Object> keyword1 = new HashMap<String, Object>();
                    keyword1.put("value", shop.getName());
                    keyword1.put("color", "#000000");
                    Map<String, Object> keyword2 = new HashMap<String, Object>();
                    keyword2.put("value", "餐饮");
                    keyword2.put("color", "#000000");
                    Map<String, Object> keyword3 = new HashMap<String, Object>();
                    BigDecimal money = orderPaymentItemMapper.selectEnableTicketMoney(order.getId());
                    if(money==null){
                        setting.setOpenTicket(0);
                        money = BigDecimal.valueOf(0);
                    }
                    keyword3.put("value", money.toString()+"(可开票金额)");
                    keyword3.put("color", "#000000");
                    Map<String, Object> keyword4 = new HashMap<String, Object>();
                    keyword4.put("value", "30天");
                    keyword4.put("color", "#000000");

                    Map<String, Object> remark = new HashMap<String, Object>();

                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            remark.put("value", brandTemplateEdit.getEndSign());
                        }else {
                            remark.put("value", "请点击详情进入订单列表开具发票");
                        }
                    }else {
                        remark.put("value", "请点击详情进入订单列表开具发票");
                    }

                    remark.put("color", "#173177");

                    content.put("first", first);
                    content.put("keyword1", keyword1);
                    content.put("keyword2", keyword2);
                    content.put("keyword3", keyword3);
                    content.put("keyword4", keyword4);
                    content.put("remark", remark);
                    //String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                    if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                        if(setting.getOpenTicket()!=0){
                            weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                            //发送短信
                            if (setting.getMessageSwitch() == 1) {
                                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                smsParam.put("key1", order.getSerialNumber());
                                smsParam.put("key2", shop.getName());
                                smsParam.put("key3", order.getTableNumber());
                                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                            }
                        }

                    } else {
                        String orderId = order.getId();
                        if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
                            orderId = order.getParentOrderId();
                        }
                        List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                        for (Participant p : participants) {
                            if(setting.getOpenTicket()!=0){
                                Customer c = customerService.selectById(p.getCustomerId());
                                weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", c.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if (setting.getMessageSwitch() == 1) {
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shop.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            }

                        }
                    }
                } else {
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
        }
    }

    @Pointcut("execution(* com.resto.shop.web.service.PosService.syncPosOrderPay(..))")
    public void syncPosOrderPayPushMsg() {
    }

    @AfterReturning(value = "syncPosOrderPayPushMsg()",returning = "data")
    public void syncPosOrderPayPushMsg(String data){
        try{
            JSONObject object = new JSONObject(data);
            Brand brand = brandService.selectById(object.getString("brandId"));
            //商户系统内部订单号
            //要修改的订单信息
            Order order = JSON.parseObject(object.get("order").toString(), Order.class);
            //gigtPush(order);
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            Customer customer = customerService.selectById(order.getCustomerId());
            if(customer != null) {
                if (order.getOrderState() == 2) {
                    WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                    ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                    List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM414174151");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM414174151", TemplateSytle.TICKET);

                    if (templateFlowList != null && templateFlowList.size() != 0) {
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String baseUrl = UrlUtils.getIP(URI.create(setting.getWechatWelcomeUrl())).toString();
                        Integer ticketType = 0;
                        if(setting.getOpenTicket()==1){
                            ticketType = 1;
                        }
                        if(setting.getOpenTicket()==2){

                        }
                        String jumpUrl = baseUrl+"/restowechat/src/views/invoice.html?baseUrl="+baseUrl+"&invoiceType="+2+"&shopId="+shop.getId()+"&customerId="+customer.getId()+"&ticketType="+ticketType;

                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();
                        if (brandTemplateEdit != null) {
                            if(brandTemplateEdit.getBigOpen()){
                                first.put("value", brandTemplateEdit.getStartSign());
                            }else {
                                first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                            }
                        } else {
                            first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                        }
                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", shop.getName());
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", "餐饮");
                        keyword2.put("color", "#000000");
                        Map<String, Object> keyword3 = new HashMap<String, Object>();
                        BigDecimal money = orderPaymentItemMapper.selectEnableTicketMoney(order.getId());
                        if (money == null) {
                            setting.setOpenTicket(0);
                            money = BigDecimal.valueOf(0);
                        }
                        keyword3.put("value", money.toString() + "(可开票金额)");
                        keyword3.put("color", "#000000");
                        Map<String, Object> keyword4 = new HashMap<String, Object>();
                        keyword4.put("value", "30天");
                        keyword4.put("color", "#000000");


                        Map<String, Object> remark = new HashMap<String, Object>();
                        if (brandTemplateEdit != null) {
                            if(brandTemplateEdit.getBigOpen()){
                                remark.put("value", brandTemplateEdit.getEndSign());
                            }else {
                                remark.put("value", "请点击详情进入订单列表开具发票");
                            }
                        } else {
                            remark.put("value", "请点击详情进入订单列表开具发票");
                        }
                        remark.put("color", "#173177");
                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("keyword3", keyword3);
                        content.put("keyword4", keyword4);
                        content.put("remark", remark);
                        //String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                        if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                            if(setting.getOpenTicket()!=0){
                                weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", customer.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if (setting.getMessageSwitch() == 1) {
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shop.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            }

                        } else {
                            String orderId = order.getId();
                            if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
                                orderId = order.getParentOrderId();
                            }
                            List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                            for (Participant p : participants) {
                                if(setting.getOpenTicket()!=0){
                                    Customer c = customerService.selectById(p.getCustomerId());
                                    weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                    Map map = new HashMap(4);
                                    map.put("brandName", brand.getBrandName());
                                    map.put("fileName", c.getId());
                                    map.put("type", "UserAction");
                                    map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                    doPostAnsc(map);
                                    //发送短信
                                    if (setting.getMessageSwitch() == 1) {
                                        com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                        smsParam.put("key1", order.getSerialNumber());
                                        smsParam.put("key2", shop.getName());
                                        smsParam.put("key3", order.getTableNumber());
                                        com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                    }
                                }

                            }
                        }
                    } else {
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
                }
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }


    @Pointcut("execution(* com.resto.shop.web.service.OrderService.orderWxPaySuccessAspect(..))")
    public void orderWxPaySuccessPushMsg() {
    }

    @AfterReturning(value = "orderWxPaySuccessPushMsg()", returning = "jsonResult")
    public void syncPosOrderPayPushMsg(JSONResult jsonResult){
        if(!jsonResult.isSuccess()){
            return;
        }
        Order order = (Order) jsonResult.getData();
        //gigtPush(order);
        Brand brand = brandService.selectById(order.getBrandId());
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        Customer customer = customerService.selectById(order.getCustomerId());
        if(customer != null) {
            if(order.getOrderState() == OrderState.PAYMENT){
                WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
                ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
                List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM414174151");
                BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM414174151", TemplateSytle.TICKET);

                if (templateFlowList != null && templateFlowList.size() != 0) {
                    String templateId = templateFlowList.get(0).getTemplateId();
                    String baseUrl = UrlUtils.getIP(URI.create(setting.getWechatWelcomeUrl())).toString();
                    Integer ticketType = 0;
                    if(setting.getOpenTicket()==1){
                        ticketType = 1;
                    }
                    if(setting.getOpenTicket()==2){

                    }
                    String jumpUrl = baseUrl+"/restowechat/src/views/invoice.html?baseUrl="+baseUrl+"&invoiceType="+2+"&shopId="+shop.getId()+"&customerId="+customer.getId()+"&ticketType="+ticketType;

                    Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                    Map<String, Object> first = new HashMap<String, Object>();
                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            first.put("value", brandTemplateEdit.getStartSign());
                        }else {
                            first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                        }
                    }else {
                        first.put("value", "尊敬的顾客您好，您有新的订单可开具电子发票");
                    }
                    first.put("color", "#00DB00");
                    Map<String, Object> keyword1 = new HashMap<String, Object>();
                    keyword1.put("value", shop.getName());
                    keyword1.put("color", "#000000");
                    Map<String, Object> keyword2 = new HashMap<String, Object>();
                    keyword2.put("value", "餐饮");
                    keyword2.put("color", "#000000");
                    Map<String, Object> keyword3 = new HashMap<String, Object>();
                    BigDecimal money = orderPaymentItemMapper.selectEnableTicketMoney(order.getId());
                    if(money == null){
                        setting.setOpenTicket(0);
                        money = BigDecimal.valueOf(0);
                    }
                    keyword3.put("value", money.toString()+"(可开票金额)");
                    keyword3.put("color", "#000000");
                    Map<String, Object> keyword4 = new HashMap<String, Object>();
                    keyword4.put("value", "30天");
                    keyword4.put("color", "#000000");

                    Map<String, Object> remark = new HashMap<String, Object>();
                    if(brandTemplateEdit!=null){
                        if(brandTemplateEdit.getBigOpen()){
                            remark.put("value", brandTemplateEdit.getEndSign());
                        }else {
                            remark.put("value", "请点击详情进入订单列表开具发票");
                        }
                    }else {
                        remark.put("value", "请点击详情进入订单列表开具发票");
                    }
                    remark.put("color", "#173177");

                    content.put("first", first);
                    content.put("keyword1", keyword1);
                    content.put("keyword2", keyword2);
                    content.put("keyword3", keyword3);
                    content.put("keyword4", keyword4);
                    content.put("remark", remark);
                    //String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                    if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                        if(setting.getOpenTicket()!=0){
                            weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", customer.getId());
                            map.put("type", "UserAction");
                            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                            //发送短信
                            if (setting.getMessageSwitch() == 1) {
                                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                smsParam.put("key1", order.getSerialNumber());
                                smsParam.put("key2", shop.getName());
                                smsParam.put("key3", order.getTableNumber());
                                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
                            }
                        }

                    } else {
                        String orderId = order.getId();
                        if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
                            orderId = order.getParentOrderId();
                        }
                        List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                        for (Participant p : participants) {
                            if(setting.getOpenTicket()!=0){
                                Customer c = customerService.selectById(p.getCustomerId());
                                weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", c.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                //发送短信
                                if (setting.getMessageSwitch() == 1) {
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    smsParam.put("key1", order.getSerialNumber());
                                    smsParam.put("key2", shop.getName());
                                    smsParam.put("key3", order.getTableNumber());
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
                                }
                            }

                        }
                    }
                } else {
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }

        }
    }

    //礼物推送
    public  void gigtPush(Order order){
        Brand brand = brandService.selectById(order.getBrandId());
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        if(setting.getOpenSmallProgramPush()==0){
            return;
        }

        Customer customer = customerService.selectById(order.getCustomerId());
        if(customer == null) {
            return;
        }

        if(order.getOrderState() != OrderState.PAYMENT){
            return;
        }

        WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
        ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
        List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM411055841");
        BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM411055841", TemplateSytle.GIFT);

        if (templateFlowList == null || templateFlowList.size() == 0) {
            Map map = new HashMap(4);
            map.put("brandName", brand.getBrandName());
            map.put("fileName", customer.getId());
            map.put("type", "UserAction");
            map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            return;
        }

        String templateId = templateFlowList.get(0).getTemplateId();
        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
        Map<String, Object> first = new HashMap<String, Object>();
        if (brandTemplateEdit != null) {
            if (brandTemplateEdit.getBigOpen()) {
                first.put("value", brandTemplateEdit.getStartSign());
            } else {
                first.put("value", "召唤好友一起来【" + brand.getBrandName() + "】吧，我们等你很久啦！");
            }
        } else {
            first.put("value", "召唤好友一起来【" + brand.getBrandName() + "】吧，我们等你很久啦！");
        }
        first.put("color", "#00DB00");
        Map<String, Object> keyword1 = new HashMap<String, Object>();
        keyword1.put("value", "【" + brand.getBrandName() + "】小程序");
        keyword1.put("color", "#000000");
        Map<String, Object> keyword2 = new HashMap<String, Object>();
        keyword2.put("value", DateTimeUtils.dateToString(new Date()));
        keyword2.put("color", "#000000");
        Map<String, Object> remark = new HashMap<String, Object>();
        if (brandTemplateEdit != null) {
            if (brandTemplateEdit.getBigOpen()) {
                remark.put("value", brandTemplateEdit.getEndSign());
            } else {
                remark.put("value", "点击查看福袋详情>>");
            }
        } else {
            remark.put("value", "点击查看福袋详情>>");
        }
        remark.put("color", "#173177");

        content.put("first", first);
        content.put("keyword1", keyword1);
        content.put("keyword2", keyword2);
        content.put("remark", remark);

        if (order.getGroupId() == null || "".equals(order.getGroupId())) {
            if (setting.getOpenTicket() == 0) {
                return;
            }
            weChatService.sendTemplate(customer.getWechatId(), templateId, setting.getProductCouponPushUrl(), content, config.getAppid(), config.getAppsecret());
            Map map = new HashMap(4);
            map.put("brandName", brand.getBrandName());
            map.put("fileName", customer.getId());
            map.put("type", "UserAction");
            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            //发送短信
            if (setting.getMessageSwitch() == 1) {
                com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                smsParam.put("key1", order.getSerialNumber());
                smsParam.put("key2", shop.getName());
                smsParam.put("key3", order.getTableNumber());
                com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105880019");
            }
            return;
        }

        String orderId = order.getId();
        if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
            orderId = order.getParentOrderId();
        }
        List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
        for (Participant p : participants) {
            if (setting.getOpenTicket() == 0) {
                continue;
            }
            Customer c = customerService.selectById(p.getCustomerId());
            weChatService.sendTemplate(c.getWechatId(), templateId, setting.getProductCouponPushUrl(), content, config.getAppid(), config.getAppsecret());
            Map map = new HashMap(4);
            map.put("brandName", brand.getBrandName());
            map.put("fileName", c.getId());
            map.put("type", "UserAction");
            map.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            //发送短信
            if (setting.getMessageSwitch() != 1) {
                continue;
            }
            com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
            smsParam.put("key1", order.getSerialNumber());
            smsParam.put("key2", shop.getName());
            smsParam.put("key3", order.getTableNumber());
            com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(), smsParam, "餐加", "SMS_105880019");
            }
    }
}
