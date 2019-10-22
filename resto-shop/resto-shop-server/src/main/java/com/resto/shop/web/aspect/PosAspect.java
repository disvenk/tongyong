package com.resto.shop.web.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.RedisService;
import com.resto.brand.web.service.WeChatService;
import com.resto.brand.web.service.WechatConfigService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.OrderPayMode;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.OrderItem;
import com.resto.shop.web.model.OrderPaymentItem;
import com.resto.shop.web.service.CustomerService;
import com.resto.shop.web.service.OrderItemService;
import com.resto.shop.web.service.OrderService;
import com.resto.shop.web.service.ToCollectPostDataService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by KONATA on 2017/8/10.
 */
@Component
@Aspect
public class PosAspect {

    @Autowired
    WeChatService weChatService;

    Logger log = LoggerFactory.getLogger(getClass());

   /* @Pointcut("execution(* com.resto.shop.web.service.PosService.shopMsgChange(..))")
    public void shopMsgChange() {
    }*/

    @Pointcut("execution(* com.resto.shop.web.service.PosService.updateData(..))")
    public void updateData() {
    }

//    @Pointcut("execution(* com.resto.shop.web.service.PosService.syncPosRefundOrder(..))")
//    public void syncPosRefundOrder() {
//    }


   /* @AfterReturning(value = "shopMsgChange()", returning = "shopMsgChangeDto")
    public void shopMsgChange(ShopMsgChangeDto shopMsgChangeDto) throws Throwable {
        sendShopChangeMessage(shopMsgChangeDto);
    }*/

    @Resource
    OrderService orderService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    CustomerService customerService;

    @Resource
    WechatConfigService wechatConfigService;

    @Autowired
    RedisService redisService;
    @Autowired
    ToCollectPostDataService toCollectPostDataService;


    @AfterReturning(value = "updateData()", returning = "resultInfo")
    public void updateData(JoinPoint point, String resultInfo) {
        log.info("修改数据完成，进入切面返回信息：" + resultInfo);
        //得到调用此方法时传递的参数
        String paramData = (String) point.getArgs()[0];
        log.info("调用方法时传递的参数：" + paramData);
        JSONObject param = new JSONObject(paramData);
        //得到此次修改数据的业务类型
        String serverType = Optional.ofNullable(param.get("serverType")).orElse("").toString();
        JSONObject returnObject = new JSONObject(resultInfo);
        List<String> orderIds = JSON.parseObject(returnObject.getJSONArray("orderIdList").toString(), new TypeReference<List<String>>() {
        });
        Order order = new Order();
        //判断业务类型进行联动操作
        switch (serverType) {
            case "weightPackage":
                log.info("开始修改重量包后的后续操作");
                //修改重量包
                List<String> orderItemIds = JSON.parseObject(returnObject.getJSONArray("orderItemIdList").toString(), new TypeReference<List<String>>() {
                });
                //得到当前订单
                order = orderService.selectById(orderIds.get(0));
                //查询到当前用户
                Customer customer = customerService.selectById(order.getCustomerId());
                if (customer != null) {
                    String sendMessage = "报告老板，商家已确认您的订单，香喷喷的美食马上就来~\n" +
                            "菜品明细：\n";
                    for (String orderItemId : orderItemIds) {
                        OrderItem orderItem = orderItemService.selectById(orderItemId);
                        sendMessage = sendMessage + orderItem.getArticleName() + "  ×1";
                    }
                    WechatConfig wechatConfig = wechatConfigService.selectByBrandId(order.getBrandId());
                    log.info("发送微信消息：" + sendMessage);
                    weChatService.sendCustomerMsg(sendMessage, customer.getWechatId(), wechatConfig.getAppid(), wechatConfig.getAppsecret());
                }
                break;
            case "orderPay":
                log.info("开始支付订单后的后续操作");
                for (String orderId : orderIds) {
                    order = orderService.selectById(orderId);
                    if (StringUtils.isBlank(order.getParentOrderId())) {
                        break;
                    }
                }
                //释放桌位
                redisService.set(order.getShopDetailId() + order.getTableNumber() + "status", true);
                //确认订单
                orderService.confirmBossOrder(order);
                break;
            default:
                log.info("本次调用无后续联动操作");
                break;
        }
    }


//    @AfterReturning(value = "syncPosRefundOrder()", returning = "result")
//    public void syncPosRefundOrderAfter(String result) throws Throwable {
//        JSONObject jsonObject = new JSONObject(result);
//        String orderId = jsonObject.getString("orderId");
//        String shopId = jsonObject.getString("shopId");
//        List<OrderItem> orderItems = JSON.parseArray(jsonObject.get("orderItems").toString(), OrderItem.class);
//        //此次退款方式
//        Integer refundPayMode = 0;
//        //此次退菜方式 0：编辑 1：退菜
//        Integer refundItemType = jsonObject.getInt("type");
//        if (refundItemType == Common.YES) {
//            List<OrderPaymentItem> paymentItems = JSON.parseArray(jsonObject.getString("data"), OrderPaymentItem.class);
//            if (paymentItems.size() == 1) {
//                refundPayMode = OrderPayMode.getPayModeByPaymentMode(paymentItems.get(0).getPaymentModeId());
//            }
//        }
//    }

}
