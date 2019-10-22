package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.resto.brand.core.util.AliPayUtils;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.*;
import com.resto.shop.web.dao.OrderMapper;
import com.resto.shop.web.dao.PosMapper;
import com.resto.shop.web.dao.SyncOrderLogMapper;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.*;
import com.resto.shop.web.posDto.*;
import com.resto.shop.web.producer.MQMessageProducer;
import com.resto.shop.web.service.*;
import eleme.openapi.sdk.api.json.gson.JsonArray;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.resto.brand.core.util.WeChatPayUtils.*;
import static com.resto.shop.web.service.impl.OrderServiceImpl.generateString;

/**
 * Created by KONATA on 2017/8/9.
 */
@Component
@Service
public class PosServiceImpl implements PosService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleService articleService;

    @Autowired
    MQMessageProducer mqMessageProducer;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderPaymentItemService orderPaymentItemService;

    @Autowired
    private PlatformOrderService platformOrderService;

    @Autowired
    private PlatformOrderDetailService platformOrderDetailService;

    @Autowired
    private PlatformOrderExtraService platformOrderExtraService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandSettingService brandSettingService;

    @Autowired
    private AccountSettingService accountSettingService;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private ShopDetailService shopDetailService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ArticlePriceService articlePriceService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PosMapper posMapper;

    @Autowired
    private OrderRefundRemarkService orderRefundRemarkService;

    @Autowired
    private CloseShopService closeShopService;

    @Autowired
    private NewCustomCouponService newCustomCouponService;

    @Autowired
    private CouponService couponService;

    @Autowired
    WechatConfigService wechatConfigService;

    @Autowired
    SmsLogService smsLogService;

    @Autowired
    AccountService accountService;

    @Autowired
    private WxServerConfigService wxServerConfigService;

    @Autowired
    ToCollectPostDataService toCollectPostDataService;

    @Autowired
    SyncOrderLogMapper syncOrderLogMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    WeChatService weChatService;

    @Override
    public String syncArticleStock(String shopId) {
        Map<String, Object> result = new HashMap<>();
        result.put("dataType", "article");

        List<Article> articleList = articleService.selectList(shopId);
        List<ArticleStockDto> articleStockDtoList = new ArrayList<>();
        for (Article article : articleList) {
//            Integer count = (Integer) RedisUtil.get(article.getId() + Common.KUCUN);
            Integer count = (Integer) redisService.get(article.getId() + Common.KUCUN);
            if (count != null) {
                article.setCurrentWorkingStock(count);
            }
            ArticleStockDto articleStockDto = new ArticleStockDto(article.getId(), article.getCurrentWorkingStock());
            articleStockDtoList.add(articleStockDto);
        }
        result.put("articleList", articleStockDtoList);
        return new JSONObject(result).toString();
    }

    @Override
    public void shopMsgChange(ShopMsgChangeDto shopMsgChangeDto) {
        mqMessageProducer.sendShopChangeMessage(shopMsgChangeDto);
    }

    @Override
    public String syncOrderCreated(String orderId) {
        Order order = orderService.selectById(orderId);
        if (order == null) {
            log.error("syncOrderCreated     未查到订单信息：" + orderId);
//            if (RedisUtil.get(orderId + "orderCreated") != null && (Integer) RedisUtil.get(orderId + "orderCreated") >= 5) {
            if (redisService.get(orderId + "orderCreated") != null && (Integer) redisService.get(orderId + "orderCreated") >= 5) {
                return "";
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            if (RedisUtil.get(orderId + "orderCreated") == null) {
//                RedisUtil.set(orderId + "orderCreated", 1);
//            } else {
//                RedisUtil.set(orderId + "orderCreated", ((Integer) RedisUtil.get(orderId + "orderCreated")) + 1);
//            }
            if (redisService.get(orderId + "orderCreated") == null) {
                redisService.set(orderId + "orderCreated", 1);
            } else {
                redisService.set(orderId + "orderCreated", ((Integer) redisService.get(orderId + "orderCreated")) + 1);
            }
            syncOrderCreated(orderId);
            return "";
        }
        OrderDto orderDto = new OrderDto(order);
        JSONObject jsonObject = new JSONObject(orderDto);
        jsonObject.put("dataType", "orderCreated");
        Map map = new HashMap();
        map.put("orderId", orderId);
        map.put("count", "count > 0");
        List<OrderItem> orderItems = orderItemService.selectOrderItemByOrderId(map);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDto orderItemDto = new OrderItemDto(orderItem);
            orderItemDtos.add(orderItemDto);
        }
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            jsonObject.put("customer", new JSONObject(new CustomerDto(customer)));
        }
        jsonObject.put("orderItem", orderItemDtos);
        //无论什么支付模式此时有支付记录则发送
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        if (!CollectionUtils.isEmpty(payItemsList)) {
            List<OrderPaymentDto> orderPaymentDtos = new ArrayList<>();
            for (OrderPaymentItem orderPaymentItem : payItemsList) {
                OrderPaymentDto orderPaymentDto = new OrderPaymentDto(orderPaymentItem);
                if (orderPaymentDto.getPaymentModeId() == PayMode.WEIXIN_PAY || orderPaymentDto.getPaymentModeId() == PayMode.ALI_PAY) {
                    orderPaymentDto.setResultData("请在服务器查看");
                }
                if (orderPaymentDto.getPaymentModeId() == PayMode.ACCOUNT_PAY) {
                    orderPaymentDto.setResultData("手机端完成的余额支付");
                }
                if (orderPaymentDto.getPaymentModeId() == PayMode.COUPON_PAY) {
                    orderPaymentDto.setResultData("手机端完成的优惠券支付");
                }
                if (orderPaymentDto.getPaymentModeId() == PayMode.REWARD_PAY) {
                    orderPaymentDto.setResultData("手机端完成的充值赠送金额支付");
                }
                orderPaymentDtos.add(orderPaymentDto);
            }
            jsonObject.put("orderPayment", orderPaymentDtos);
        }
        CustomerAddress customerAddress = customerAddressService.selectByPrimaryKey(order.getCustomerAddressId());

        if (customerAddress != null) {
            CustomerAddressDto customerAddressDto = new CustomerAddressDto(customerAddress);
            jsonObject.put("customerAddress", new JSONObject(customerAddressDto));
        }
        return jsonObject.toString();
    }

    @Override
    public String syncOrderPay(String orderId) {
        Order order = orderService.selectById(orderId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dataType", "orderPay");
        jsonObject.put("orderId", order.getId());
        jsonObject.put("payMode", order.getPayMode());
        jsonObject.put("isPosPay", order.getIsPosPay());
        jsonObject.put("customerId", order.getCustomerId());
        jsonObject.put("requestId", ApplicationUtils.randomUUID());
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        List<OrderPaymentDto> orderPaymentDtos = new ArrayList<>();
        for (OrderPaymentItem paymentItem : payItemsList) {
            OrderPaymentDto orderPaymentDto = new OrderPaymentDto(paymentItem);
            if (orderPaymentDto.getPaymentModeId() == PayMode.WEIXIN_PAY || orderPaymentDto.getPaymentModeId() == PayMode.ALI_PAY) {
                orderPaymentDto.setResultData("请在服务器查看");
            }
            if (orderPaymentDto.getPaymentModeId() == PayMode.ACCOUNT_PAY) {
                orderPaymentDto.setResultData("手机端完成的余额支付");
            }
            if (orderPaymentDto.getPaymentModeId() == PayMode.COUPON_PAY) {
                orderPaymentDto.setResultData("手机端完成的优惠券支付");
            }
            if (orderPaymentDto.getPaymentModeId() == PayMode.REWARD_PAY) {
                orderPaymentDto.setResultData("手机端完成的充值赠送金额支付");
            }
            orderPaymentDtos.add(orderPaymentDto);
        }
        jsonObject.put("orderPayment", orderPaymentDtos);
        return jsonObject.toString();
    }

    @Override
    public String syncPlatform(String orderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("dataType", "platform");
        PlatformOrder platformOrder = platformOrderService.selectByPlatformOrderId(orderId, null);
        if (platformOrder == null) {
            return null;
        }
        List<PlatformOrderDetail> platformOrderDetails = platformOrderDetailService.selectByPlatformOrderId(orderId);
        List<PlatformOrderDetailDto> platformOrderDetailDtos = new ArrayList<>();
        for (PlatformOrderDetail platformOrderDetail : platformOrderDetails) {
            PlatformOrderDetailDto detailDto = new PlatformOrderDetailDto(platformOrderDetail);
            platformOrderDetailDtos.add(detailDto);
        }
        List<PlatformOrderExtra> platformOrderExtras = platformOrderExtraService.selectByPlatformOrderId(orderId);
        List<PlatformOrderExtraDto> extraDtos = new ArrayList<>();
        for (PlatformOrderExtra platformOrderExtra : platformOrderExtras) {
            PlatformOrderExtraDto extraDto = new PlatformOrderExtraDto(platformOrderExtra);
            extraDtos.add(extraDto);
        }
        result.put("order", new PlatformOrderDto(platformOrder));
        result.put("orderDetail", platformOrderDetailDtos);
        result.put("orderExtra", extraDtos);
        return new JSONObject(result).toString();
    }

    @Override
    public void articleActived(String articleId, Integer actived) {
        articleService.setActivated(articleId, actived);
    }

    @Override
    public String articleEmpty(String articleId) {
        Article article = articleService.selectById(articleId);
        String isEmpty=null;
        if (articleId.indexOf("@") > -1) { //老规格下的子品
//            RedisUtil.set(articleId + Common.KUCUN, 0);
            redisService.set(articleId + Common.KUCUN, 0);
            String aid = articleId.substring(0, articleId.indexOf("@"));
            //得到这个老规格下的所有属性
            List<ArticlePrice> articlePrices = articlePriceService.selectByArticleId(aid);
            int sum = 0;
            if (!CollectionUtils.isEmpty(articlePrices)) {
                for (ArticlePrice articlePrice : articlePrices) {
//                    Integer ck = (Integer) RedisUtil.get(articlePrice.getId() + Common.KUCUN);
                    Integer ck = (Integer) redisService.get(articlePrice.getId() + Common.KUCUN);
                    if (ck != null) {
                        sum += ck;
                    } else {
                        sum += articlePrice.getCurrentWorkingStock();
                    }
                }
//                RedisUtil.set(aid + Common.KUCUN, sum);
                redisService.set(aid + Common.KUCUN, sum);
                if (sum == 0) {
                    articleService.setEmpty(aid);
                    isEmpty=String.valueOf(sum);
                } else {
                    articleService.setEmptyFail(aid);
                    isEmpty=String.valueOf(sum);
                }
            }
        } else {
//            RedisUtil.set(articleId + Common.KUCUN, 0);
            redisService.set(articleId + Common.KUCUN, 0);
            articleService.setEmpty(articleId);
            isEmpty="0";
            List<ArticlePrice> articlePrices = articlePriceService.selectByArticleId(articleId);
            if (!CollectionUtils.isEmpty(articlePrices)) {
                for (ArticlePrice articlePrice : articlePrices) {
//                    RedisUtil.set(articlePrice.getId() + Common.KUCUN, 0);
                    redisService.set(articlePrice.getId() + Common.KUCUN, 0);
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        if (isEmpty.equals("0")){
            jsonObject.put("type","empty");
            jsonObject.put("id",articleId);
            jsonObject.put("isEmpty",1);
        }else {
            jsonObject.put("type","empty");
            jsonObject.put("id",articleId);
            jsonObject.put("isEmpty",0);
        }
        return jsonObject.toString();
    }

    @Override
    public void articleEdit(String articleId, Integer count) {
        String shopId;
        if (articleId.indexOf("@") > -1) { //老规格下的子品
            String aid = articleId.substring(0, articleId.indexOf("@"));
            Article article = articleService.selectById(aid);
            shopId = article.getShopDetailId();
        } else {
            Article article = articleService.selectById(articleId);
            shopId = article.getShopDetailId();
        }

        articleService.editStock(articleId, count, shopId);
    }

    @Override
    public void printSuccess(String orderId) {
        Order order = orderService.selectById(orderId);
        if (order != null) {
            Brand brand = brandService.selectById(order.getBrandId());
            BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
            AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());

            orderService.printSuccess(orderId, brandSetting.getOpenBrandAccount() == 1, accountSetting);
        } else {
            log.error("Pos2.0 打印失败：为找到相应订单；orderId：" + orderId);
        }
    }

    @Override
    public String syncPosCreateOrder(String data) {
        //定义返回值
        JSONObject returnObject = new JSONObject();
        //拿到请求参数
        JSONObject json = new JSONObject(data);
        OrderDto orderDto = JSON.parseObject(json.get("order").toString(), OrderDto.class);
        Order serverDataBaseOrder = orderMapper.selectByPrimaryKey(orderDto.getId());
        try {
            if (serverDataBaseOrder != null) {  //  判断服务器数据库是否已经存在此订单
                log.error("Pos2.0   创建订单失败：数据库已存在此订单");
                returnObject.put("success", false);
                return returnObject.toString();
            }
            if (StringUtils.isNotEmpty(orderDto.getParentOrderId())) {
                Order o = orderService.selectById(orderDto.getParentOrderId());
                if (o!=null) {
                    orderDto.setCustomerId(o.getCustomerId());
                }else {
                    log.error("Pos2.0   创建订单失败：主订单不存在，子订单加菜失败"+"\n"+orderDto.getShopDetailId());
                    returnObject.put("success", false);
                    return returnObject.toString();
                }
            }
            orderDto.setShopDetailId(json.getString("shopId"));
            Order order = new Order(orderDto);
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(json.getString("shopId"));
            order.setOrderMode(shopDetail.getShopMode());
            order.setReductionAmount(BigDecimal.valueOf(0));
            order.setBrandId(json.getString("brandId"));
            order.setDataOrigin(orderDto.getDataOrigin());
            //  电视叫号，下单会走 pushOrder 切面。需要先设置一个值。
            if (order.getPayMode() == null) {
                order.setPayMode(0);
            }
            //  订单项
            List<OrderItemDto> orderItemDtos = orderDto.getOrderItem();
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDto orderItemDto : orderItemDtos) {
                OrderItem orderItem = new OrderItem(orderItemDto);
                orderItems.add(orderItem);
            }
            order.setOrderItems(orderItems);

            //  订单支付项
            List<OrderPaymentDto> orderPaymentDtos = orderDto.getOrderPayment();
            List<OrderPaymentItem> orderPaymentItems = new ArrayList<>();
            if (orderPaymentDtos != null) {
                for (OrderPaymentDto orderPaymentDto : orderPaymentDtos) {
                    OrderPaymentItem orderPaymentItem = new OrderPaymentItem(orderPaymentDto);
                    orderPaymentItems.add(orderPaymentItem);
                }
            }
            if (StringUtils.isNotEmpty(orderDto.getParentOrderId())) {    //  子订单
                Order parent = orderService.selectById(order.getParentOrderId());
                if (order.getNeedConfirmOrderItem() == 1) {
                    //NewPos加菜订单， 如果有称重菜品则主订单needConfirmOrderItem需要改成1
                    parent.setNeedConfirmOrderItem(1);
                    orderService.update(parent);
                }
                order.setVerCode(parent.getVerCode());
            } else {  //  主订单
                if (StringUtils.isEmpty(order.getVerCode())) {
                    order.setVerCode(generateString(5));
                }
            }
            //newpos创建订单的时候传递的posDiscount为null的时候则默认100
            if(order.getPosDiscount() == null){
                order.setPosDiscount(new BigDecimal(1));
            }else{
                order.setPosDiscount(order.getPosDiscount().divide(new BigDecimal(100)));
            }
            order.setBaseOrderMoney(order.getOrderMoney());
            //  插入订单信息
            orderMapper.insertSelective(order);
            orderItemService.insertItems(orderItems);
            orderPaymentItemService.insertItems(orderPaymentItems);
            //  更新主订单
            if (StringUtils.isNotEmpty(orderDto.getParentOrderId())) {
                updateParent(order);
            }
            // 更新库存
            Boolean updateStockSuccess = false;
            try {
                updateStockSuccess = orderService.updateStock(orderService.getOrderInfo(order.getId()));
            } catch (AppException e) {
                e.printStackTrace();
            }
            if (!updateStockSuccess) {
                log.info("库存变更失败:" + order.getId());
            }
            returnObject.put("success", true);
            returnObject.put("synSuccess", true);
        } catch (JSONException e) {
            e.printStackTrace();
            returnObject.put("success", false);
            returnObject.put("synSuccess", false);
        }
        returnObject.put("orderId", orderDto.getId());
        return returnObject.toString();
    }

    @Override
    public String syncPosOrderPay(String data) {
        log.info("来自NewPos的支付申请：" + data);
        JSONObject json = new JSONObject(data);
        JSONObject returnParam = new JSONObject();
        Order order = orderService.selectById(json.getString("orderId"));
        log.info(">>>>>>>>>>>>>>>>>>开始进入syncPosOrderPay方法,同步机制>>>>>>>>>>>>>>>>>>>>");
        try {
            if (order != null && order.getOrderState() == OrderState.SUBMIT) {
                Customer customer = customerService.selectById(order.getCustomerId());
                ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
                Brand brand = brandService.selectById(order.getBrandId());
                returnParam.put("shopId", shopDetail.getId());
                returnParam.put("brandId", shopDetail.getBrandId());
                log.info("接收到的支付项JSON信息：" + json.get("orderPayment").toString());
                List<OrderPaymentDto> orderPaymentDtos = JSON.parseObject(json.get("orderPayment").toString(), new TypeReference<List<OrderPaymentDto>>() {
                });
                log.info("转换后的数组长度：" + orderPaymentDtos.size());
                for (OrderPaymentDto orderPaymentDto : orderPaymentDtos) {
                    log.info("进入循环，插入支付项支付模式为：" + orderPaymentDto.getPaymentModeId());
                    OrderPaymentItem orderPaymentItem = new OrderPaymentItem(orderPaymentDto);
                    if (orderPaymentDto.getPaymentModeId() == PayMode.ACCOUNT_PAY) {
                        //如果是余额支付
                        accountService.payOrder(order, orderPaymentItem.getPayValue(), customer, brand, shopDetail);
                    } else {
                        if (orderPaymentDto.getPaymentModeId() == PayMode.COUPON_PAY) {
                            //如果是优惠券支付
                            Coupon coupon = couponService.selectById(orderPaymentDto.getToPayId());
                            if(coupon != null){
                                coupon.setIsUsed(true);
                                coupon.setUsingTime(new Date());
                                couponService.update(coupon);

                                List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectByOrderId(order.getId());
                                boolean flag = false;
                                OrderPaymentItem orderPI = null;
                                if(orderPaymentItems == null){
                                    orderPaymentItemService.insert(orderPaymentItem);
                                }else{
                                    for(OrderPaymentItem opi : orderPaymentItems){
                                        if(opi.getPaymentModeId() == PayMode.COUPON_PAY){
                                            orderPI = opi;
                                            flag = true;
                                        }
                                    }
                                    if(flag){
                                        if(!orderPI.getToPayId().equals(orderPaymentDto.getToPayId())){
                                            //如果线上跟newpos使用的优惠券不一样
                                            //则归还线上优惠券，使用newpos优惠券      --因为一个订单只能使用一个优惠券。

                                            //先归还优惠券
                                            orderPaymentItemService.delete(orderPI.getId());
                                            Coupon c = couponService.selectById(orderPI.getToPayId());
                                            c.setIsUsed(false);
                                            c.setUsingTime(null);
                                            couponService.update(c);

                                            //后记录新的优惠券
                                            orderPaymentItemService.insert(orderPaymentItem);
                                        }
                                    }else{
                                        orderPaymentItemService.insert(orderPaymentItem);
                                    }
                                }
                            }
                        }else{
                            orderPaymentItemService.insert(orderPaymentItem);
                        }
                    }
                }
                //  根据 pos 传输的数据为准
                if (json.has("isPosPay")) {
                    order.setIsPosPay(json.getInt("isPosPay"));
                }
                order.setOrderState(OrderState.PAYMENT);
                order.setPaymentAmount(BigDecimal.valueOf(0));
                order.setAllowCancel(false);
                order.setAllowContinueOrder(false);
                order.setPayMode(json.getInt("payMode"));
                order.setConfirmTime(null);
                order.setSyncState(json.getInt("syncState"));
                order.setLastSyncTime(json.get("lastSyncTime").toString());
                orderService.update(order);
                if (!StringUtils.isEmpty(order.getParentOrderId())) {
                    updateParent(order);
                }
                updateChild(order);
                redisService.set(order.getShopDetailId() + order.getTableNumber() + "status", true);
                orderService.confirmBossOrder(order);
                //发放消费返利优惠券
                couponService.addConsumptionRebateCoupon(order);
                //修改菜品的实际金额
                orderItemService.updateOrderItemActualAmount(order, false);
                log.info(">>>>>>>>>>>>>>>>>>>>>>开始进入syncPosOrderPay方法--->同步机制,开始进入支付同步机制方法>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
            returnParam.put("success", true);
            returnParam.put("isPolling", true);
            returnParam.put("synSuccess", true);
            returnParam.put("order", JSON.toJSONString(order));
        } catch (JSONException e) {
            e.printStackTrace();
            returnParam.put("success", false);
            returnParam.put("isPolling", false);
            returnParam.put("synSuccess", false);
        }
        returnParam.put("orderId", order.getId());
        return returnParam.toString();
    }


    private void updateParent(Order order) {
        Order parent = orderService.selectById(order.getParentOrderId());
        int articleCountWithChildren = 0;
        Double amountWithChildren = 0.0;
        articleCountWithChildren = orderMapper.selectArticleCountByIdBossOrder(parent.getId());
        amountWithChildren = orderMapper.selectParentAmountByBossOrder(parent.getId());
        parent.setCountWithChild(articleCountWithChildren);
        parent.setAmountWithChildren(new BigDecimal(amountWithChildren));
        orderService.update(parent);
    }


    private void updateChild(Order order) {
        List<Order> orders = orderService.selectByParentId(order.getId(), order.getPayType());
        if (!CollectionUtils.isEmpty(orders)) {
            for (Order child : orders) {
                if (child.getOrderState() < OrderState.PAYMENT) {
                    child.setOrderState(OrderState.PAYMENT);
                    child.setPaymentAmount(BigDecimal.valueOf(0));
                    child.setAllowCancel(false);
                    child.setAllowContinueOrder(false);
                    orderService.update(child);
                }
            }
        }

    }

    @Override
    public String syncPosRefundOrder(String data) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        JSONObject json = new JSONObject(data);
        Order order = JSON.parseObject(json.get("refund").toString(), Order.class);
        result.put("orderId", order.getId());
        try {
            //标识为pos2.0退菜
            order.setPosRefundArticleType(Common.YES);
            Order refundOrder = orderService.selectOrderInfo(order.getId());
            result.put("shopId",refundOrder.getShopDetailId());
            if(StringUtils.isEmpty(refundOrder.getShopDetailId())){
                result.put("shopId",json.get("shopId").toString());
            }else{
                result.put("shopId",refundOrder.getShopDetailId());
            }
            result.put("orderItems", order.getOrderItems());
            if (refundOrder.getOrderState() == OrderState.SUBMIT) {
                //编辑
                result.put("type", Common.NO);
                for (OrderItem orderItem : order.getOrderItems()) {
                    OrderItem item = orderItemService.selectById(orderItem.getId());
                    if (orderItem.getType() != ArticleType.ARTICLE) {
                        Order o = orderMapper.selectByPrimaryKey(orderItem.getOrderId());
                        //此处退的是服务费
                        item = new OrderItem();
                        item.setOrderId(orderItem.getOrderId());
                        item.setCount(o.getCustomerCount());
                    }
                    orderService.updateOrderItem(item.getOrderId(), item.getCount() - orderItem.getCount(), orderItem.getId(), orderItem.getType());
                }
            } else {
                orderService.refundItem(order);
                result.put("data", com.alibaba.fastjson.JSONObject.toJSONString(order.getRefundPaymentList()));
                //退款
                result.put("type", Common.YES);
                orderService.refundArticleMsg(order);
                //退菜后再重新更新一下主订单信息， 用来判断是否一退光
                refundOrder = orderService.selectOrderInfo(order.getId());
                //判断是否清空
                boolean flag = true;
                //原逻辑为articleCount为0则改成退菜取消，现改成如果orderMoney为0则改为退菜取消(注：其实orderMoney为0就是菜品及服务费退完了)
                if (refundOrder.getOrderMoney().compareTo(BigDecimal.ZERO) > 0) {
                    flag = false;
                }
                if (flag) {
                    //如果当前订单为主订单
                    if (refundOrder.getParentOrderId() == null) {
                        List<Order> orders = orderService.selectByParentId(refundOrder.getId(), 0); //得到子订单
                        for (Order child : orders) { //遍历子订单
                            child = orderService.selectOrderInfo(child.getId());
                            if (child.getOrderMoney().compareTo(BigDecimal.ZERO) > 0) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            refundOrderArticleNull(refundOrder);
                            //表示父子订单的金额都已退完，将子订单的状态也改成退菜取消
                            orders.forEach(parentOrder -> {
                                refundOrderArticleNull(parentOrder);
                                orderService.update(parentOrder);
                            });
                        }
                    } else {
                        refundOrderArticleNull(refundOrder);
                    }
                    //存在退菜之后订单菜品是为负的情况  所以这边添加此校验  若为负则为零
                    if (refundOrder.getArticleCount() != null && refundOrder.getArticleCount() < 0) {
                        refundOrder.setArticleCount(0);
                    } else if (refundOrder.getCountWithChild() != null && refundOrder.getCountWithChild() < 0) {
                        refundOrder.setCountWithChild(0);
                    }
                    orderService.update(refundOrder);
                }
            }
            // 插入退款备注
            orderRefundRemarkService.posSyncInsertList(order.getOrderRefundRemarks());
            // 还原库存
//            Boolean addStockSuccess = false;
//            try {
//                addStockSuccess = orderService.addStock(order);
//            } catch (AppException e) {
//                e.printStackTrace();
//            }
//            if (!addStockSuccess) {
//                log.info("库存还原失败:" + order.getId());
//            }
            result.put("synSuccess", true);
            result.put("orderIds", getRefundOrderIds(order.getOrderItems()));
        } catch (Exception e) {
            e.printStackTrace();
            result.put("synSuccess", false);
            result.put("success", false);
            result.put("message", "退菜失败");
            result.put("orderIds", getRefundOrderIds(order.getOrderItems()));
        }
        return result.toString();
    }

    @Override
    public String syncPosGrantOrder(String data) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        JSONObject json = new JSONObject(data);
        String orderId = json.optString("orderId");
        result.put("orderId", orderId);

        List<Order> order = JSON.parseArray(json.get("order").toString(), Order.class);
        for(Order o : order){
            orderService.update(o);
        }

        List<OrderItem> orderItems = JSON.parseArray(json.get("orderItems").toString(), OrderItem.class);
        for(OrderItem oi : orderItems){
            orderItemService.update(oi);
        }

        List<OrderRefundRemark> orderRefundRemarks = JSON.parseArray(json.get("remarks").toString(), OrderRefundRemark.class);
        orderRefundRemarkService.posSyncInsertList(orderRefundRemarks);

        result.put("synSuccess", true);
        result.put("success", true);
        return result.toString();
    }

    private String getRefundOrderIds(List<OrderItem> orderItemList){
        StringBuffer buffer = new StringBuffer();
        orderItemList.forEach(orderItem -> {
            buffer.append(orderItem.getOrderId()).append(",");
        });
        return buffer.toString().substring(0, buffer.toString().length() - 1);
    }

    private void refundOrderArticleNull(Order refundOrder) {
        refundOrder.setAllowAppraise(false);
        refundOrder.setAllowContinueOrder(false);
        refundOrder.setIsRefundOrder(true);
        refundOrder.setProductionStatus(ProductionStatus.REFUND_ARTICLE);
    }

    @Override
    public void syncPosConfirmOrder(String orderId) {
        Order order = orderService.selectById(orderId);
        if (order != null && order.getOrderState() == OrderState.SUBMIT) {
            orderService.confirmOrderPos(orderId);
        }
    }


    @Override
    public void test() {
//        Order order = new Order();
//        order.setId("00b8a27437cf460c93910bdc2489d061");
//        order.setBrandId("31946c940e194311b117e3fff5327215");
//        order.setShopDetailId("31164cebcc4b422685e8d9a32db12ab8");
        mqMessageProducer.sendPlatformOrderMessage("1210056817231407326", 1, "2f83afee7a0e4822a6729145dd53af33", "8565844c69b94b0dbde38b0861df62c8");
    }

    @Override
    public List<ArticleSupport> syncArticleSupport(String shopId) {
        return posMapper.selectArticleSupport(shopId);
    }

    @Override
    public String syncChangeTable(String orderId, String tableNumber) {
        //定义返回值
        JSONObject returnObject = new JSONObject();
        Order order = orderService.selectById(orderId);
        returnObject.put("success", true);
        returnObject.put("synSuccess", true);
        returnObject.put("orderId", orderId);
        try {
            if (order == null) {
                return returnObject.toString();
            }
            //  释放老桌位
            redisService.set(order.getShopDetailId() + order.getTableNumber() + "status", true);
            order.setTableNumber(tableNumber);
            orderService.update(order);
            //  绑定新桌位
            redisService.set(order.getShopDetailId() + tableNumber + "status", false);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.put("success", false);
            returnObject.put("synSuccess", false);
        }
        return returnObject.toString();
    }

    @Override
    public void syncOpenTable(String shopId, String tableNumber) {
//        RedisUtil.set(shopId + tableNumber + "status", false);
        redisService.set(shopId + tableNumber + "status", false);
    }

    @Override
    public String syncTableState(String shopId, String tableNumber, boolean state, String orderId) {
        JSONObject resultObject = new JSONObject();
        try {
            if (state) {
                redisService.remove(shopId + tableNumber + "status");
            } else {
                redisService.set(shopId + tableNumber + "status", state);
            }
            resultObject.put("success", true);
            resultObject.put("synSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultObject.put("success", false);
            resultObject.put("synSuccess", false);
        }
        resultObject.put("orderId", orderId);
        return resultObject.toString();
    }

    @Override
    public String syncTableState(String shopId, String tableNumber, boolean state) {
        JSONObject resultObject = new JSONObject();
        try {
            if (state) {
                redisService.remove(shopId + tableNumber + "status");
            } else {
                redisService.set(shopId + tableNumber + "status", state);
            }
            resultObject.put("success", true);
            resultObject.put("synSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultObject.put("success", false);
            resultObject.put("synSuccess", false);
        }
        return resultObject.toString();
    }

    @Override
    public boolean syncPosLocalOrder(String data) {
        JSONObject json = new JSONObject(data);
        OrderDto orderDto = JSON.parseObject(json.get("order").toString(), OrderDto.class);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(orderDto.getShopDetailId());
        syncPosLocalOrder(orderDto, shopDetail);
        for (OrderDto childrenOrderDto : orderDto.getChildrenOrders()) {
            syncPosLocalOrder(childrenOrderDto, shopDetail);
        }
        log.info("\n\n 【" + shopDetail.getName() + "】 本地 POS 同步订单信息 ：" + orderDto.getId() + "\n");
        log.info(data);
        log.info("\n\n本地 POS 同步订单信息 ");
        return true;
    }

    @Override
    public void posCheckOut(String brandId, String shopId, OffLineOrder offLineOrder,String operator,String dailyLogId) {
//        offLineOrder = new OffLineOrder(ApplicationUtils.randomUUID(), shopId, brandId , 1, BigDecimal.ZERO, 0, 0, BigDecimal.ZERO, 0, new Date(), new Date(), 1);
        Brand brand = brandService.selectByPrimaryKey(brandId);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        closeShopService.cleanShopOrder(shopDetail, offLineOrder, brand,operator, dailyLogId);
    }

    @Override
    public void posCancelOrder(String shopId, String orderId) {
        try {
            Order order = orderService.selectById(orderId);
            if (order != null) {
                //查询是否存在子订单
                List<Order> childrenOrders = orderService.selectByParentId(orderId, order.getPayType());
                for (Order childrenOrder : childrenOrders) {
                    if (!childrenOrder.getClosed()) {
                        orderService.cancelOrderPos(childrenOrder.getId());//取消子订单
                    }
                }
                orderService.cancelOrderPos(orderId);//取消父订单

                //  释放桌位
                if (StringUtils.isEmpty(order.getParentOrderId())) {
                    //  如果绑定的有桌位，则释放桌位
                    if (StringUtils.isNotEmpty(order.getTableNumber())) {
//                        RedisUtil.set(shopId + order.getTableNumber() + "status", true);
                        redisService.set(shopId + order.getTableNumber() + "status", true);
                    }
                } else {
                    //  如果父订单的状态 不是 未支付，并且绑定了桌位，则释放主订单的桌位
                    Order parentOrder = orderService.selectById(order.getParentOrderId());
                    if (parentOrder.getOrderState() != OrderState.SUBMIT) {
                        if (StringUtils.isNotEmpty(parentOrder.getTableNumber())) {
//                            RedisUtil.set(shopId + order.getTableNumber() + "status", true);
                            redisService.set(shopId + order.getTableNumber() + "status", true);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("pos端拒绝订单失败！");
            e.printStackTrace();
        }
    }

    @Override
    public void serverError(String brandId, String shopId) {
//        RedisUtil.set(shopId + "loginStatus", false);
        redisService.set(shopId + "loginStatus", false);
        com.alibaba.fastjson.JSONObject param = new com.alibaba.fastjson.JSONObject();
        Brand brand = brandService.selectByPrimaryKey(brandId);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        param.put("service", "【" + brand.getBrandName() + "】-" + shopDetail.getName() + "  Pos2.0系统");
        SMSUtils.sendMessage("17671111590", param, SMSUtils.SIGN, SMSUtils.SMS_SERVER_ERROR);
    }

    @Override
    public void sendMockMQMessage(String shopId, String type, String orderId, Integer platformType) {
        Order order = new Order();
        if (StringUtils.isNotEmpty(orderId)) {
            order = orderService.selectById(orderId);
        }
        switch (type) {
            case "createOrder":
                mqMessageProducer.sendCreateOrderMessage(order, null);
                break;
            case "platform":
                platformType = platformType != null ? platformType : 1;
                ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
                if (shopDetail != null) {
                    mqMessageProducer.sendPlatformOrderMessage(orderId, platformType, shopDetail.getBrandId(), shopId);
                }
                break;
            case "orderPay":
                mqMessageProducer.sendOrderPay(order, 5 * 1000);
                break;
            case "cancelOrder":
                mqMessageProducer.sendCancelOrder(order);
                break;
           /* case "change":
                mqMessageProducer.sendShopChangeMessage(shopId);
                break;*/
            default:
                log.info("【sendMockMQMessage】未匹配~");
                break;
        }
        log.info("\n\n  shopId：" + shopId + "\n   type：" + type + "\n   orderId：" + orderId + "\n   platformType" + platformType);
    }

    @Override
    public void sendServerCommand(String shopId, String type, String sql) {
        com.alibaba.fastjson.JSONObject obj = new com.alibaba.fastjson.JSONObject();
        obj.put("shopId", shopId);
        obj.put("dataType", type);
        obj.put("data", sql);
        mqMessageProducer.sendServerCommandToNewPos(obj);
        log.info("\n\n  shopId：" + shopId + "\n   type：" + type + "\n   sql：" + sql);
    }

    @Override
    public List<String> getServerOrderIds(String shopId) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginDate = format.format(DateUtil.getDateBegin(today));
        String endDate = format.format(DateUtil.getDateEnd(today));
        return orderService.posSelectNotCancelledOrdersIdByDate(shopId, beginDate, endDate);
    }

    @Override
    public void posCallNumber(String orderId) {
        orderService.callNumber(orderId);
    }

    @Override
    public void posPrintOrder(String orderId) {
        Order order = orderService.selectById(orderId);
        if (order == null || order.getPayType() == null) {
            return;
        }
        if (order.getPayType() == 0) {    //  先付
            //  如果已付款，并且已下单了
            if (order.getOrderState() == OrderState.PAYMENT && (
                    order.getProductionStatus() == ProductionStatus.HAS_ORDER || order.getProductionStatus() == ProductionStatus.NOT_ORDER)) {
                printSuccess(orderId);
            }
            //  如果用户出现 待下单状态，但是POS接收到订单
            if (order.getOrderState() == OrderState.CONFIRM && (
                    order.getProductionStatus() == ProductionStatus.HAS_ORDER || order.getProductionStatus() == ProductionStatus.NOT_ORDER)) {
                printSuccess(orderId);
            }
        } else if (order.getPayType() == 1) {  //  后付
            //  如果已付款，并且已下单了
            if (order.getOrderState() == OrderState.SUBMIT && (
                    order.getProductionStatus() == ProductionStatus.HAS_ORDER || order.getProductionStatus() == ProductionStatus.NOT_ORDER)) {
                printSuccess(orderId);
            }
        }
    }

    @Override
    public String serverExceptionOrderList(String shopId) {
        JSONArray orderList = new JSONArray();
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginDate = format.format(DateUtil.getDateBegin(today));
        String endDate = format.format(DateUtil.getDateEnd(today));
        Boolean isFirstPay = true;
        ShopDetail shopDetail = shopDetailService.selectById(shopId);
        //  如果是 Boss 模式的后付
        if (shopDetail.getShopMode() == ShopMode.BOSS_ORDER && shopDetail.getAllowAfterPay() == 0) {
            isFirstPay = false;
        }
        log.info("\n\n" + shopDetail.getName() + "  isFirstPay：" + isFirstPay + "\n\n");
        List<String> orderIds = orderMapper.serverExceptionOrderList(shopId, isFirstPay, beginDate, endDate);
        for (String orderId : orderIds) {
            orderList.put(selectOrderRelationInfo(orderId));
        }
        return orderList.toString();
    }

    public String selectOrderRelationInfo(String orderId) {
        Order order = orderService.selectById(orderId);
        if (order == null) {
            return "";
        }
        OrderDto orderDto = new OrderDto(order);
        JSONObject jsonObject = new JSONObject(orderDto);

        //  tb_order_item
        List<OrderItem> orderItems = orderItemService.posSyncListByOrderId(orderId);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDto orderItemDto = new OrderItemDto(orderItem);
            orderItemDtos.add(orderItemDto);
        }
        jsonObject.put("orderItem", orderItemDtos);

        //  tb_order_payment_item
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        List<OrderPaymentDto> orderPaymentDtos = new ArrayList<>();
        for (OrderPaymentItem orderPaymentItem : payItemsList) {
            OrderPaymentDto orderPaymentDto = new OrderPaymentDto(orderPaymentItem);
            if (orderPaymentDto.getPaymentModeId() == PayMode.COUPON_PAY) {
                orderPaymentDto.setResultData("手机端完成的优惠券支付");
            }
            if (orderPaymentDto.getPaymentModeId() == PayMode.ACCOUNT_PAY) {
                orderPaymentDto.setResultData("手机端完成的余额支付");
            }
            if (orderPaymentDto.getPaymentModeId() == PayMode.REWARD_PAY) {
                orderPaymentDto.setResultData("手机端完成的充值赠送金额支付");
            }
            orderPaymentDtos.add(orderPaymentDto);
        }
        jsonObject.put("orderPayment", orderPaymentDtos);

        //  tb_customer & tb_customer_address todo: 如果是外带则查询用户收货地址
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            jsonObject.put("customer", new JSONObject(new CustomerDto(customer)));
        }
        CustomerAddress customerAddress = customerAddressService.selectByPrimaryKey(order.getCustomerAddressId());
        if (customerAddress != null) {
            CustomerAddressDto customerAddressDto = new CustomerAddressDto(customerAddress);
            jsonObject.put("customerAddress", new JSONObject(customerAddressDto));
        }
        return jsonObject.toString();
    }

    public void syncPosLocalOrder(OrderDto orderDto, ShopDetail shopDetail) {
        String orderId = orderDto.getId();
        StringBuffer backUps = new StringBuffer();
        // 备份老数据
        Order orderBackUps = orderService.posSyncSelectById(orderId);
        if (orderBackUps != null) {
            List<OrderItem> orderItemListBackUps = orderItemService.posSyncListByOrderId(orderId);
            List<OrderPaymentItem> orderPaymentItemListBackUps = orderPaymentItemService.posSyncListByOrderId(orderId);
            List<OrderRefundRemark> orderRefundRemarkListBackUps = orderRefundRemarkService.posSyncListByOrderId(orderId);
            orderBackUps.setOrderItems(orderItemListBackUps);
            orderBackUps.setOrderPaymentItems(orderPaymentItemListBackUps);
            orderBackUps.setOrderRefundRemarks(orderRefundRemarkListBackUps);
            backUps.append(DateUtil.getTime()).append("___").append(JSON.toJSONString(orderBackUps));
        }

        // 清除老数据
        orderService.delete(orderId);
        orderItemService.posSyncDeleteByOrderId(orderId);
        orderPaymentItemService.posSyncDeleteByOrderId(orderId);
        orderRefundRemarkService.posSyncDeleteByOrderId(orderId);
        //  插入新数据
        Order order = new Order(orderDto);
        order.setOrderMode(shopDetail.getShopMode());
        order.setReductionAmount(BigDecimal.valueOf(0));
        order.setBrandId(shopDetail.getBrandId());
        order.setPosBackUps(StringUtils.isEmpty(backUps.toString()) ? null : backUps.toString());
        // 如果 服务器端数据状态 为 已确认 或者 已评论，则以服务器为基准
        if (orderBackUps != null && (orderBackUps.getOrderState() == OrderState.CONFIRM || orderBackUps.getOrderState() == OrderState.HASAPPRAISE)) {
            order.setOrderState(orderBackUps.getOrderState());
        }
        //  订单
        orderService.insert(order);
        //  订单项
        List<OrderItemDto> orderItemDtos = orderDto.getOrderItem();
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            if (StringUtils.isNotEmpty(orderItemDto.getId())) {
                OrderItem orderItem = new OrderItem(orderItemDto);
                orderItems.add(orderItem);
            }
        }
        if (orderItems.size() > 0) {
            orderItemService.insertItems(orderItems);
        }
        //  订单支付项
        List<OrderPaymentDto> orderPaymentDtos = orderDto.getOrderPayment();
        for (OrderPaymentDto orderPaymentDto : orderPaymentDtos) {
            if (StringUtils.isNotEmpty(orderPaymentDto.getId())) {
                orderPaymentItemService.insert(new OrderPaymentItem(orderPaymentDto));
            }
        }
        //  订单退菜备注
        List<OrderRefundRemark> orderRefundRemarks = orderDto.getOrderRefundRemarks();
        for (OrderRefundRemark orderRefundRemark : orderRefundRemarks) {
            if (orderRefundRemark.getId() != null) {
                orderRefundRemarkService.insert(orderRefundRemark);
            }
        }
    }

    @Override
    public String scanCodePayment(String data) {
        log.info("开始构建支付请求，请求信息：" + data);
        JSONObject object = new JSONObject(data);
        //此次扫码的支付类型
        int payType = object.getInt("payType");
        ShopDetail shopDetail = shopDetailService.selectById(object.getString("shopId"));
        Brand brand = brandService.selectById(object.getString("brandId"));
        WechatConfig wechatConfig = wechatConfigService.selectByBrandId(brand.getId());
        //用作商户系统内部订单号，只用用来查询订单在第三方平台的支付状态
        String outTradeNo = ApplicationUtils.randomUUID();
        //此次扫码的扫描结果
        String authCode = object.getString("authCode");
        //构建支付请求的订单Id
        String orderId = object.getString("orderId");
        //此次请求支付金额
        BigDecimal paymentAmount = object.getBigDecimal("paymentAmount");
        //返回的信息
        JSONObject returnParam = new JSONObject();
        returnParam.put("success", true);
        returnParam.put("isPolling", true);
        JSONArray orderPaymentItems = new JSONArray();
        Map<String, String> map = new HashMap<>();
        try {
            if (payType == 1) { //微信支付
                String terminalIp = InetAddress.getLocalHost().getHostAddress();  //终端IP String(16)
                //微信支付的金额已分为单位
                int total = paymentAmount.multiply(new BigDecimal(100)).intValue();
                if (shopDetail.getWxServerId() == null) {
                    //普通商户
                    map = crashPay(wechatConfig.getAppid(), wechatConfig.getMchid(), "", outTradeNo, total, authCode,
                            shopDetail.getName().concat("消费"), terminalIp, wechatConfig.getMchkey());
                } else {
                    //服务商模式下的特约商户
                    WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
                    map = crashPay(wxServerConfig.getAppid(), wxServerConfig.getMchid(), shopDetail.getMchid(), outTradeNo, total, authCode,
                            shopDetail.getName().concat("消费"), terminalIp, wxServerConfig.getMchkey());
                }
                if (Boolean.valueOf(map.get("success"))) {
                    //构建微信支付请求成功
                    returnParam.put("outTradeNo", outTradeNo);
                    returnParam.put("isPolling", false);
                    addOrderPayment(PayMode.WEIXIN_PAY, orderId, paymentAmount, null, "微信支付："+ paymentAmount
                            ,map.get("data"), orderPaymentItems, brand, shopDetail, map.get("transactionId"), OrderPayMode.WX_PAY);
                } else {
                    //如果构建微信请求失败时的错误原因是系统级别导致的，调用查询API查询订单状态
                    if (!"SYSTEMERROR".equalsIgnoreCase(map.get("errCode")) &&
                            !"BANKERROR".equalsIgnoreCase(map.get("errCode")) &&
                            !"USERPAYING".equalsIgnoreCase(map.get("errCode"))) {
                        returnParam.put("isPolling", false);
                        returnParam.put("message", map.getOrDefault("msg", "构建微信支付请求失败，请检查商家相关配置"));
                    } else {
                        returnParam.put("outTradeNo", outTradeNo);
                    }
                    returnParam.put("success", false);
                }
            } else if (payType == 2) { //支付宝支付
                BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
                //支付宝支付
                AliPayUtils.connection(StringUtils.isNotBlank(shopDetail.getAliAppId()) ? shopDetail.getAliAppId() : brandSetting.getAliAppId(),
                        StringUtils.isNotBlank(shopDetail.getAliPrivateKey()) ? shopDetail.getAliPrivateKey() : brandSetting.getAliPrivateKey(),
                        StringUtils.isNotBlank(shopDetail.getAliPublicKey()) ? shopDetail.getAliPublicKey() : brandSetting.getAliPublicKey(),
                        shopDetail.getAliEncrypt());
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("out_trade_no", outTradeNo);
                json.put("subject", shopDetail.getName() + "---消费");
                json.put("total_amount", paymentAmount + "");
                json.put("auth_code", authCode);
                json.put("scene", "bar_code");
                Map<String, Object> resultMap = AliPayUtils.tradePay(json);
                if (Boolean.valueOf(resultMap.get("success").toString())) {
                    //构建支付宝支付请求成功
                    returnParam.put("outTradeNo", outTradeNo);
                } else {
                    if ((resultMap.get("sub_code") != null
                            && !resultMap.get("sub_code").toString().equalsIgnoreCase("ACQ.SYSTEM_ERROR"))
                            || resultMap.get("sub_code") == null) {
                        returnParam.put("isPolling", false);
                        returnParam.put("message", resultMap.getOrDefault("msg", "构建支付宝请求失败，请检查商家相关配置"));
                    }else{
                        returnParam.put("outTradeNo", outTradeNo);
                    }
                    returnParam.put("success", false);
                }
            } else {
                returnParam.put("isPolling", false);
                //R+扫码支付
                Customer customer = customerService.selectByTelePhone(authCode);
                //查询出该笔订单的账户信息
                Account account = accountService.selectById(customer.getAccountId());
                //查询出该笔订单的优惠券信息
                Map<String, Object> selectMap = new HashMap<>();
                selectMap.put("customerId", customer.getId());
                selectMap.put("shopId", shopDetail.getId());
                selectMap.put("orderMoney", paymentAmount);
                if (account.getRemain().compareTo(BigDecimal.ZERO) > 0) {
                    selectMap.put("useWithAccount", 1);
                }
                Coupon coupon = couponService.selectPosPayOrderCanUseCoupon(selectMap);
                //有优惠券则待支付金额减去优惠券的金额
                BigDecimal couponValue = Optional.ofNullable(coupon).map(resultCoupon -> resultCoupon.getValue()).orElse(BigDecimal.ZERO);
                BigDecimal payValue = paymentAmount.subtract(couponValue);
                if (payValue.compareTo(account.getRemain()) > 0) {
                    returnParam.put("success", false);
                    returnParam.put("message", "账户余额不足，请更换支付方式");
                } else {
                    if (couponValue.compareTo(BigDecimal.ZERO) > 0) {
                        //使用优惠券
                        addOrderPayment(PayMode.COUPON_PAY, orderId, couponValue, coupon.getId(),"优惠券支付:" + couponValue,
                                 null, orderPaymentItems, brand, shopDetail, null, null);
                    }
                    if (payValue.compareTo(BigDecimal.ZERO) > 0) {
                        //使用余额
                        addOrderPayment(PayMode.ACCOUNT_PAY, orderId, payValue, account.getId(),
                                "余额支付:" + payValue, null, orderPaymentItems, brand, shopDetail, authCode, OrderPayMode.YUE_PAY);
                    }
                    returnParam.put("customerId", customer.getId());
                }
            }
            returnParam.put("payMentInfo", orderPaymentItems);
        } catch (Exception e) {
            e.printStackTrace();
            //如果在构建支付请求时报错，将不进行下一步查询订单的操作
            returnParam.put("success", false);
            returnParam.put("isPolling", false);
            returnParam.put("message", "构建支付请求失败，请更换支付方式");
        }
        return returnParam.toString();
    }

    @Override
    public String confirmPayment(String data) {
        log.info("开始构查询订单支付信息，请求信息：" + data);
        JSONObject object = new JSONObject(data);
        ShopDetail shopDetail = shopDetailService.selectById(object.getString("shopId"));
        Brand brand = brandService.selectById(object.getString("brandId"));
        WechatConfig wechatConfig = wechatConfigService.selectByBrandId(brand.getId());
        //商户系统内部订单号
        String outTradeNo = object.getString("outTradeNo");
        //要修改的订单信息
        Order order = JSON.parseObject(object.get("order").toString(), Order.class);
        //返回的信息
        JSONObject returnParam = new JSONObject();
        returnParam.put("shopId", shopDetail.getId());
        returnParam.put("brandId", shopDetail.getBrandId());
        returnParam.put("success", true);
        returnParam.put("isPolling", true);
        returnParam.put("orderId", order.getId());
        //存储返回的支付信息
        JSONArray orderPaymentItems = new JSONArray();
        try {
            //业务返回值
            Boolean resultCode = true;
            //回调信息
            JSONObject resultInfo = new JSONObject();
            if (order.getPayMode() == OrderPayMode.WX_PAY) {
                //微信支付
                Map<String, String> map = new HashMap<>();
                if (shopDetail.getWxServerId() == null) {
                    //普通商户
                    map = queryPay(wechatConfig.getAppid(), wechatConfig.getMchid(), "", outTradeNo, wechatConfig.getMchkey());
                } else {
                    //服务商模式下的特约商户
                    WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
                    map = queryPay(wxServerConfig.getAppid(), wxServerConfig.getMchid(), shopDetail.getMchid(), outTradeNo, wxServerConfig.getMchkey());
                }
                if (!Boolean.valueOf(map.get("success"))) {
                    //如果正在支付中，则轮询继续去查。 反之则支付失败退出轮询
                    if ((map.containsKey("trade_state") && !"USERPAYING".equalsIgnoreCase(map.get("trade_state")))
                            || (map.containsKey("errCode") && !"SYSTEMERROR".equalsIgnoreCase(map.get("errCode"))
                            && !"ORDERNOTEXIST".equalsIgnoreCase(map.get("errCode")))) {
                        returnParam.put("isPolling", false);
                        returnParam.put("message", map.getOrDefault("msg", "构建微信请求失败，请检查商家相关配置"));
                    }
                    returnParam.put("success", false);
                    resultCode = false;
                } else {
                    resultInfo = new JSONObject(map.get("data"));
                    //此时该变量用来保存微信后台的交易号
                    outTradeNo = resultInfo.getString("transaction_id");
                }
            } else {
                //支付宝支付
                BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
                AliPayUtils.connection(StringUtils.isNotBlank(shopDetail.getAliAppId()) ? shopDetail.getAliAppId() : brandSetting.getAliAppId(),
                        StringUtils.isNotBlank(shopDetail.getAliPrivateKey()) ? shopDetail.getAliPrivateKey() : brandSetting.getAliPrivateKey(),
                        StringUtils.isNotBlank(shopDetail.getAliPublicKey()) ? shopDetail.getAliPublicKey() : brandSetting.getAliPublicKey(),
                        shopDetail.getAliEncrypt());
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                jsonObject.put("out_trade_no", outTradeNo);
                //查询支付宝订单的支付进度
                Map<String, Object> returnMap = AliPayUtils.tradeQuery(jsonObject);
                //查询订单成功
                if (Boolean.valueOf(returnMap.get("success").toString())) {
                    String trade_status = returnMap.get("trade_status").toString();
                    if (!"TRADE_SUCCESS".equalsIgnoreCase(trade_status) && !"TRADE_FINISHED".equalsIgnoreCase(trade_status)) {
                        if ("TRADE_CLOSED".equalsIgnoreCase(trade_status)) {
                            //未付款交易超时关闭，或支付完成后全额退款
                            returnParam.put("isPolling", false);
                            returnParam.put("message", "未付款交易超时关闭");
                        }
                        returnParam.put("success", false);
                        resultCode = false;
                    } else {
                        resultInfo = new JSONObject(returnMap.get("msg").toString());
                        resultInfo = new JSONObject(resultInfo.get("alipay_trade_query_response").toString());
                        //此时该变量用来保存支付宝后台的交易号
                        outTradeNo = resultInfo.getString("trade_no");
                    }
                } else {
                    if ((returnMap.get("sub_code") != null
                            && !returnMap.get("sub_code").toString().equalsIgnoreCase("ACQ.SYSTEM_ERROR")
                            && !returnMap.get("sub_code").toString().equalsIgnoreCase("ACQ.TRADE_NOT_EXIST"))
                            || returnMap.get("sub_code") == null) {
                        returnParam.put("isPolling", false);
                        returnParam.put("message", returnMap.getOrDefault("msg", "构建支付宝请求失败，请检查商家相关配置"));
                    }
                    returnParam.put("success", false);
                    resultCode = false;
                }
            }
            //微信或支付宝支付成功的时候
            if (resultCode) {
                //支付成功，退出轮询插入支付信息修改订单信息
                returnParam.put("isPolling", false);
                addOrderPayment(order.getPayMode() == OrderPayMode.WX_PAY ? PayMode.WEIXIN_PAY : PayMode.ALI_PAY, order.getId(),
                        order.getPayMode() == OrderPayMode.WX_PAY ? resultInfo.getBigDecimal("total_fee").divide(new BigDecimal(100))
                                : new BigDecimal(resultInfo.getString("total_amount")), null, order.getPayMode() == OrderPayMode.WX_PAY ? "微信支付：" + resultInfo.getBigDecimal("total_fee").divide(new BigDecimal(100))
                                : "支付宝支付：" + resultInfo.get("total_amount"), resultInfo.toString(), orderPaymentItems, brand, shopDetail, outTradeNo, order.getPayMode());
            }
            returnParam.put("payMentInfo", orderPaymentItems);
        } catch (Exception e) {
            e.printStackTrace();
            //如果在构建查询支付进度请求时报错，将不进行下一步查询订单的操作
            returnParam.put("success", false);
        }
        return returnParam.toString();
    }


    /**
     * 插入对应支付项
     *
     * @param payModeId
     * @param orderId
     * @param payValue
     * @param toPayId
     * @param remark
     * @param resultData
     * @param orderPaymentItems
     * @param brand
     * @param shopDetail
     */
    private void addOrderPayment(Integer payModeId, String orderId, BigDecimal payValue, String toPayId, String remark,
                                 String resultData, JSONArray orderPaymentItems, Brand brand, ShopDetail shopDetail, String outTradeNo,
                                 Integer payMode) {

        log.info(">>>>>>>>>>>>>>>>>>开始进入addOrderPayment方法,同步机制>>>>>>>>>>>>>>>>>>>>");

        OrderPaymentItem paymentItem = new OrderPaymentItem();
        paymentItem.setId(StringUtils.isNotBlank(outTradeNo) && payModeId != PayMode.ACCOUNT_PAY ? outTradeNo : ApplicationUtils.randomUUID());
        paymentItem.setPayTime(new Date());
        paymentItem.setPayValue(payValue);
        paymentItem.setPaymentModeId(payModeId);
        paymentItem.setOrderId(orderId);
        paymentItem.setToPayId(toPayId);
        paymentItem.setRemark(remark);
        paymentItem.setResultData(resultData);
        if (payModeId == PayMode.ACCOUNT_PAY) {
            //使用余额明细
            Order newOrderInfo = orderService.selectById(orderId);
            Customer customer = customerService.selectById(newOrderInfo.getCustomerId());
            if (customer == null) {
                //如果此时customer为null，则代表是newPos本地下的单。需要根据outTradeNo(电话号码)查询customer
                customer = customerService.selectByTelePhone(outTradeNo);
            }
            accountService.payOrder(newOrderInfo, payValue, customer, brand, shopDetail);
        } else {
            if (payModeId == PayMode.COUPON_PAY) {
                //优惠券支付
                Coupon coupon = couponService.selectById(toPayId);
                coupon.setUsingTime(new Date());
                coupon.setIsUsed(true);
                couponService.update(coupon);
            }
            orderPaymentItemService.insert(paymentItem);
        }

        log.info(">>>>>>>>>>>>>>>>>>开始进入addOrderPayment方法,同步机制,判断payMode != null>>>>>>>>>>>>>>>>>>>>");

        if (payMode != null) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>开始进入addOrderPayment方法--->同步机制,开始进入支付同步机制方法>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

        //返回的支付信息
        JSONObject returnPayment = new JSONObject(paymentItem);
        returnPayment.put("payTime", paymentItem.getPayTime().getTime());
        returnPayment.put("resultData", paymentItem.getRemark());
        orderPaymentItems.put(returnPayment);
    }

    @Override
    public String revocationOfOrder(String data) {
        log.info("开始撤销订单，请求信息：" + data);
        //转换所需参数
        JSONObject paramObject = new JSONObject(data);
        //商户订单号
        String outTradeNo = paramObject.getString("outTradeNo");
        Brand brand = brandService.selectById(paramObject.getString("brandId"));
        ShopDetail shopDetail = shopDetailService.selectById(paramObject.getString("shopId"));
        WechatConfig wechatConfig = wechatConfigService.selectByBrandId(brand.getId());
        int payTyoe = paramObject.getInt("payType");
        //定义返回参数
        JSONObject returnObject = new JSONObject();
        returnObject.put("success", true);
        try {
            if (payTyoe == 1) {
                Map<String, String> map = new HashMap<>();
                //撤销微信订单
                if (shopDetail.getWxServerId() == null) {
                    if (StringUtils.isNotBlank(wechatConfig.getPayCertPath())) {
                        //普通商户
                        map = reverseOrder(wechatConfig.getAppid(), wechatConfig.getMchid(), "", wechatConfig.getMchkey(), outTradeNo, wechatConfig.getPayCertPath());
                    } else {
                        returnObject.put("success", false);
                        returnObject.put("message", "撤销订单失败，无退款证书请线下处理");
                        return returnObject.toString();
                    }
                } else {
                    WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
                    map = reverseOrder(wxServerConfig.getAppid(), wxServerConfig.getMchid(), shopDetail.getMchid(), wxServerConfig.getMchkey(), outTradeNo, wxServerConfig.getPayCertPath());
                }
                if (!Boolean.valueOf(map.get("success"))) {
                    //撤销失败
                    String message = map.get("msg");
                    returnObject.put("message",Optional.ofNullable(message).orElse("撤销支付订单失败，请线下处理"));
                    returnObject.put("success", false);
                }
            } else if (payTyoe == 2) {
                //撤销支付宝订单
                com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
                object.put("out_trade_no", outTradeNo);
                Map<String, Object> returnMap = AliPayUtils.tradeCancel(object);
                //撤销失败
                if (!Boolean.valueOf(returnMap.get("success").toString())) {
                    //撤销失败
                    String message = returnMap.get("msg").toString();
                    returnObject.put("message",Optional.ofNullable(message).orElse("撤销支付订单失败，请线下处理"));
                    returnObject.put("success", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.put("success", false);
            returnObject.put("message", "撤销支付订单出错，请线下处理");
        }
        return returnObject.toString();
    }

    @Override
    public String updateData(String data) {
        log.info("接收到要修改的数据" + data + ", 开始执行修改操作");
        //定义返回信息
        JSONObject returnObject = new JSONObject();
        returnObject.put("success", true);
        JSONObject paramInfo = new JSONObject(data);
        List<String> orderIds = new ArrayList<>();
        //修改的订单项的Id
        List<String> orderItemIds = new ArrayList<>();
//        //解密需要更新的数据
//        log.info("解密之前的数据：" + paramInfo.getString("dataList"));
//        String info = RSAEncryptionUtil.decryptBase64(paramInfo.getString("dataList"));
//        log.info("解密之后的数据：" + info);
        //得到要修改的数据信息
        List<Map<String, Object>> dataList = JSON.parseObject(paramInfo.getString("dataList"), new TypeReference<List<Map<String, Object>>>(){});
        try {
            //遍历dataList
            dataList.forEach(dataInfo -> {
                dataInfo.forEach((key, value) -> {
                    execution(key, value, orderIds, orderItemIds);
                });
            });
            returnObject.put("orderIdList", orderIds);
            returnObject.put("orderItemIdList", orderItemIds);
            returnObject.put("synSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            //拿到此次修改的所有订单
            returnObject.put("success", false);
            returnObject.put("message", "修改数据出错，事务回滚");
            returnObject.put("synSuccess", false);
            //获取此次修改的订单信息Id
        }
        returnObject.put("orderIds", errorOrderIdList(dataList));
        return returnObject.toString();
    }


    private String errorOrderIdList(List<Map<String, Object>> dataList) {
        StringBuffer orderIds = new StringBuffer();
        dataList.forEach(dataInfo -> {
            dataInfo.forEach((key, value) -> {
                if (key.equalsIgnoreCase("order")){
                    List<Order> orderList = JSON.parseObject(value.toString(), new TypeReference<List<Order>>() {});
                    orderList.forEach(order -> {
                        orderIds.append(order.getId()).append(",");
                    });
                }
            });
        });
        return orderIds.toString().substring(0, orderIds.toString().length() - 1);
    }

    /**
     * 通过实体名去修改对应的表的数据
     *
     * @param key
     * @param value
     */
    private void execution(String key, Object value, List<String> orderIds, List<String> orderItemIds) {
        log.info("key：" + key + "---value：" + value);
        switch (key) {
            case "order":
                List<Order> orderList = JSON.parseObject(value.toString(), new TypeReference<List<Order>>() {});
                orderList.forEach(order -> {
                    log.info("反序列化的Order：" + order.getId());
                    orderService.update(order);
                    orderIds.add(order.getId());
                });
                break;
            case "orderItem":
                List<OrderItem> orderItemList = JSON.parseObject(value.toString(), new TypeReference<List<OrderItem>>() {});
                orderItemList.forEach(orderItem -> {
                    log.info("反序列化的OrderItem：" + orderItem.getId());
                    OrderItem newOrderItem = orderItemService.selectById(orderItem.getId());
                    if (newOrderItem != null) {
                        orderItemService.update(orderItem);
                    } else {
                        orderItemService.insert(orderItem);
                    }
                    orderItemIds.add(orderItem.getId());
                });
                break;
            default:
                log.error("参数错误");
                break;
        }
    }


    /**
     * 得到能用的余额和优惠券
     *
     * @param orderId
     * @return
     */
    @Override
    public String useAccountAndCoupon(String orderId) {
        JSONObject returnInfo = new JSONObject();
        //查询到对应订单
        Order order = orderService.selectById(orderId);
        //查找到当前主订单对应的支付项
        List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectByOrderId(orderId);
        //判断当前订单是否占用了优惠券
        BigDecimal usedCouponValue = BigDecimal.ZERO;
        //订单待支付金额
        BigDecimal payValue = BigDecimal.ZERO;
        //支付的余额
        BigDecimal accountPayValue = BigDecimal.ZERO;
        //支付的优惠券
        BigDecimal couponValue = BigDecimal.ZERO;
        //存在对应用户再调取用户账户余额、优惠券信息
        if (!order.getCustomerId().equalsIgnoreCase("0")
                && StringUtils.isNotBlank(order.getCustomerId())) {
            //判断主订单是否支付
            if (order.getOrderState() == OrderState.SUBMIT) {
                //如果是主订单未支付，则父子订单一起支付
                payValue = order.getAmountWithChildren().compareTo(BigDecimal.ZERO) > 0 ? order.getAmountWithChildren() : order.getOrderMoney();
                for (OrderPaymentItem paymentItem : orderPaymentItems) {
                    if (paymentItem.getPaymentModeId() == PayMode.COUPON_PAY) {
                        //newPos结算时会存在修改订单金额的情况，此时重新判断订单金额是否满足优惠券最低使用金额
                        Coupon coupon = couponService.selectById(paymentItem.getToPayId());
                        if (coupon.getMinAmount().compareTo(payValue) <= 0) {
                            //当前订单满足占用的优惠券的使用条件
                            usedCouponValue = paymentItem.getPayValue();
                            //父子订单一起支付，此时的payValue是父子订单的订单金额之和应当减去已占用的优惠券之和
                            payValue = payValue.subtract(usedCouponValue);
                            returnInfo.put("useCouponValue", usedCouponValue);
                            returnInfo.put("paymentItem", JSON.toJSONString(paymentItem));
                        } else {
                            //未满足使用条件，返回优惠券
                            orderPaymentItemService.delete(coupon.getId());
                            couponService.refundCoupon(coupon.getId());
                            order.setPaymentAmount(order.getPaymentAmount().add(coupon.getValue()));
                            orderService.update(order);
                        }
                    }
                }
            } else {
                //存储已占用优惠券的相关信息
                JSONObject usedCouponInfo = new JSONObject();
                for (OrderPaymentItem paymentItem : orderPaymentItems) {
                    if (paymentItem.getPaymentModeId() == PayMode.COUPON_PAY) {
                        //支付金额减去已占用的优惠券金额
                        usedCouponValue = paymentItem.getPayValue();
                        //标识主订单使用过优惠券
                        usedCouponInfo.put("mainCoupon", true);
                    }
                }
                //查询出该笔主订单下的子订单
                List<Order> orderList = orderService.selectListByParentId(orderId);
                for (Order subOrder : orderList) {
                    if (usedCouponValue.compareTo(BigDecimal.ZERO) == 0) {
                        orderPaymentItems = orderPaymentItemService.selectByOrderId(subOrder.getId());
                        for (OrderPaymentItem paymentItem : orderPaymentItems) {
                            if (paymentItem.getPaymentModeId() == PayMode.COUPON_PAY) {
                                //预防已取消订单返还优惠券会产生多笔记录的情况， 此处usedCouponValue累加
                                usedCouponValue = usedCouponValue.add(paymentItem.getPayValue());
                                //优惠券Id
                                usedCouponInfo.put("couponId", paymentItem.getToPayId());
                                //当前优惠券的支付项
                                usedCouponInfo.put("paymentItem", JSON.toJSONString(paymentItem));
                                //当前支付记录的Id
                                usedCouponInfo.put("paymentItemId", paymentItem.getId());
                                //标识子订单使用过优惠券
                                usedCouponInfo.put("mainCoupon", false);
                            }
                        }
                    }
                    if (subOrder.getOrderState() == OrderState.SUBMIT) {
                        payValue = subOrder.getOrderMoney();
                        //判断当前订单是否占用优惠券
                        if (usedCouponValue.compareTo(BigDecimal.ZERO) > 0
                                && !usedCouponInfo.getBoolean("mainCoupon")) {
                            //newPos结算时会存在修改订单金额的情况，此时重新判断订单金额是否满足优惠券最低使用金额
                            Coupon coupon = couponService.selectById(usedCouponInfo.getString("couponId"));
                            if (coupon.getMinAmount().compareTo(subOrder.getOrderMoney()) <= 0) {
                                //当前订单满足占用的优惠券的使用条件
                                returnInfo.put("paymentItem", usedCouponInfo.getString("paymentItem"));
                                returnInfo.put("useCouponValue", usedCouponValue);
                                payValue = payValue.subtract(usedCouponValue);
                            } else {
                                //未满足使用条件，返回优惠券
                                orderPaymentItemService.delete(usedCouponInfo.getString("paymentItemId"));
                                couponService.refundCoupon(coupon.getId());
                                //归零已占用优惠券金额
                                usedCouponValue = BigDecimal.ZERO;
                                subOrder.setPaymentAmount(subOrder.getPaymentAmount().add(usedCouponValue));
                                orderService.update(subOrder);
                            }
                        }
                    }
                }
            }
            //查询到订单对应用户
            Customer customer = customerService.selectById(order.getCustomerId());
            //查询到用户的账户信息
            Account account = accountService.selectById(customer.getAccountId());
            //如果没有使用过优惠券则查询出可用的优惠券
            if (usedCouponValue.compareTo(BigDecimal.ZERO) == 0) {
                Map<String, Object> selectMap = new HashMap<>();
                selectMap.put("customerId", customer.getId());
                selectMap.put("shopId", order.getShopDetailId());
                selectMap.put("orderMoney", payValue);
                if (account.getRemain().compareTo(BigDecimal.ZERO) > 0) {
                    selectMap.put("useWithAccount", 1);
                }
                Coupon coupon = couponService.selectPosPayOrderCanUseCoupon(selectMap);
                //存在优惠券的则记入couponValue
                couponValue = Optional.ofNullable(coupon).map(resultCoupon -> resultCoupon.getValue()).orElse(BigDecimal.ZERO);
                //待支付金额减去优惠券金额
                payValue = payValue.subtract(couponValue);
                //存在优惠券则返回对应Id
                Optional.ofNullable(coupon).ifPresent(resultCoupon -> returnInfo.put("couponId", resultCoupon.getId()));
            }
            //判断剩余需要支付金额
            if (payValue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal oldRemain = account.getRemain();
                BigDecimal remain = oldRemain;
                if (remain.compareTo(BigDecimal.ZERO) > 0) {
                    returnInfo.put("accountId", account.getId());
                    remain = remain.subtract(payValue);
                    if (remain.compareTo(BigDecimal.ZERO) >= 0) {
                        accountPayValue = payValue;
                    } else {
                        accountPayValue = oldRemain;
                    }
                }
            }
        }
        returnInfo.put("accountPayValue", accountPayValue);
        returnInfo.put("couponValue", couponValue);
        return returnInfo.toString();
    }

    @Override
    public void synchronousData(Order order) {
        //拿到线上订单上一次修改时间
        Order onlineOrder = orderService.selectById(order.getId());
        String lastSyncTime = Optional.ofNullable(onlineOrder).map(lineOrder -> onlineOrder.getLastSyncTime()).orElse("0");
        if (order.getLastSyncTime() != null && (Long.valueOf(order.getLastSyncTime()) > Long.valueOf(lastSyncTime))) {
            SyncOrderLog syncOrderLog = new SyncOrderLog();
            //插入或修改订单项
            orderService.insert(order);
            syncOrderLog.setContentOrder(order.toString());
            Optional.ofNullable(order.getOrderItems()).ifPresent(orderItems -> {
                //插入或修改订单菜品项
                orderItemService.insertOrUpdateOrderItems(orderItems);
                syncOrderLog.setContentOrderItem(orderItems.toString());
            });
            Optional.ofNullable(order.getOrderPaymentItems()).ifPresent(orderPaymentItems -> {
                //插入或修改订单支付项
                orderPaymentItemService.insertItems(orderPaymentItems);
                syncOrderLog.setContentOrderPaymentItem(orderPaymentItems.toString());
            });
            Optional.ofNullable(order.getOrderRefundRemarks()).ifPresent(orderRefundRemarks -> {
                //插入退菜备注
                orderRefundRemarkService.posSyncInsertList(orderRefundRemarks);
            });
            syncOrderLog.setBrandId(order.getBrandId());
            syncOrderLog.setShopId(order.getShopDetailId());
            syncOrderLogMapper.insertSelective(syncOrderLog);//插入日志
            //当前订单同步成功
            order.setSynSuccess(true);
        } else {
            //拒接当前订单
            order.setSynSuccess(false);
        }
    }

    @Override
    public void removeIsUseCoupon(String couponId) {
        OrderPaymentItem orderPaymentItem = orderPaymentItemService.selectByToPayIdpaymentModeId(3, couponId);
        if(orderPaymentItem != null){
            List<Order> orders = orderService.selectListByParentId(orderPaymentItem.getOrderId());
            //如果移除的优惠券订单没有加菜订单  则恢复订单的实际还需要支付的金额
            if(orders.size() == 0){
                Order order = orderService.selectById(orderPaymentItem.getOrderId());
                order.setPaymentAmount(order.getPaymentAmount().add(orderPaymentItem.getPayValue()));
                orderService.update(order);
            }
        }
        log.info("收到NewPos发送的移除优惠券请求，优惠券Id为：" + couponId);
        //移除已占用的优惠券支付记录
        orderPaymentItemService.deletePaymentByToPayId(couponId);
        //归还优惠券
        couponService.refundCoupon(couponId);
    }

    @Override
    public String getCustomerAmount(String data) {
        JSONObject result = new JSONObject();
        //访问参数
        JSONObject params = new JSONObject(data);
        String customerId = params.getString("customerId");
        BigDecimal money = params.getBigDecimal("money");
        //获取用户
        Customer customer = customerService.selectById(customerId);
        //获取用户账户信息
        Account account = accountService.selectById(customer.getAccountId());
        //如果用户账户余额满足要求的金额则返回要求的金额，反之则返回用户的最大余额
        if (account.getRemain().compareTo(money) >= 0) {
            result.put("accountPayValue", money);
        } else {
            result.put("accountPayValue", account.getRemain());
        }
        result.put("accountId", account.getId());
        return result.toString();
    }
}
