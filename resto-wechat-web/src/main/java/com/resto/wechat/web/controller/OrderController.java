package com.resto.wechat.web.controller;

import com.resto.api.appraise.service.NewAppraiseNewService;
import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.api.article.dto.UnitArticleDto;
import com.resto.api.article.service.NewArticleRecommendService;
import com.resto.api.article.service.NewUnitService;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.core.util.WeChatPayUtils;
import com.resto.brand.web.model.*;
import com.resto.api.customer.entity.Customer;
import com.resto.api.article.entity.Recommend;
import com.resto.api.article.entity.Unit;
import com.resto.brand.web.model.TableQrcode;
import com.resto.brand.web.service.*;
import com.resto.brand.web.service.TableQrcodeService;
import com.resto.shop.web.constant.*;
import com.resto.shop.web.exception.AppException;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.entity.AppraiseNew;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.*;
import com.resto.wechat.web.rpcinterceptors.DataSourceTarget;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppElemeUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.MobilePackageBean;
import eleme.openapi.sdk.utils.Base64;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@RestController
@RequestMapping("order")
public class OrderController extends GenericController {

    // 设置APP KEY
    private static final String key = "o6ph8ACwrY";

    // 设置APP SECRET
    private static final String secret = "11b1d008b10ecb1510dbdf100d1c97e1";

    @Resource
    private OrderService orderService;
    @Resource
    private OrderItemService orderItemService;

    @Autowired
    private NewArticleRecommendService newArticleRecommendService;

    @Resource
    NewCustomerService newCustomerService;

    @Resource
    BrandService brandService;

    @Autowired
    private NewUnitService newUnitService;

    @Resource
    private NewAppraiseService newAppraiseService;

    @Autowired
    private ThirdService thirdService;

    @Resource
    private ShareSettingService shareSettingService;

    @Autowired
    private BrandSettingService brandSettingService;

    @Autowired
    ShopDetailService shopDetailService;

    @Autowired
    SysErrorService sysErrorService;

    @Autowired
    WxServerConfigService wxServerConfigService;

    @Resource
    TableQrcodeService tableQrcodeService;

    @Resource
    OrderPaymentItemService orderPaymentItemService;

    @Resource
    CouponService couponService;

    @Resource
    ParticipantService participantService;

    @Resource
    NewAppraiseNewService newAppraiseNewService;

    @Autowired
    RedisService redisService;

    @RequestMapping("listOrder")
    public Result listOrder(Integer start, Integer datalength, String orderId, String orderState) {
        List<Order> orderList = orderService.listOrder(start, datalength, getCurrentShopId(), getCurrentCustomerId(), orderState);
        for (Order order : orderList) {
            String shopName = getShopDetail(order.getShopDetailId()).getName();
            order.setShopName(shopName);
            log.info("oid:" + order.getId() + " shopName:" + shopName + " --- " + order.getShopDetailId());
        }
        return getSuccessResult(orderList);
    }

    @RequestMapping("/new/listOrder")
    public void listOrderNew(Integer start, Integer datalength, String orderState, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        ShareSetting shareSetting = shareSettingService.selectByBrandId(getCurrentBrandId());
        List<Order> orderList = orderService.listOrder(start, datalength, getCurrentShopId(), getCurrentCustomerId(), orderState);
        log.info("这里还没有问题");
        for (Order order : orderList) {
            String shopName = getShopDetail(order.getShopDetailId()).getName();
            order.setShopName(shopName);
            //判断本条订单是否评价
            if (order.getOrderState() == OrderState.HASAPPRAISE && (order.getGroupId() == null || "".equals(order.getGroupId()))) {
                Appraise appraise = newAppraiseService.selectDeatilByOrderId(order.getBrandId(),order.getId(), getCurrentCustomerId());
                if (appraise != null) {
                    if (appraise.getLevel() >= shareSetting.getMinLevel() && appraise.getContent().length() >= shareSetting.getMinLength() && shareSetting.getIsActivity()) {
                        Long remindTime = (new Date().getTime() - appraise.getCreateTime().getTime()) / 1000;
                        if (remindTime >= shareSetting.getDelayTime()) {
                            order.setIsShare(1);
                        }
                    }
                }else{
                    AppraiseNew aa = newAppraiseNewService.selectByOrderIdCustomerId(order.getBrandId(),order.getId(),getCurrentCustomerId());
                    if (aa.getLevel()/20 >= shareSetting.getMinLevel() && aa.getContent().length() >= shareSetting.getMinLength() && shareSetting.getIsActivity()) {
                        Long remindTime = (new Date().getTime() - aa.getCreateTime().getTime()) / 1000;
                        if (remindTime >= shareSetting.getDelayTime()) {
                            order.setIsShare(1);
                        }
                    }
                }
            }else if(order.getGroupId() != null && !"".equals(order.getGroupId()) && order.getAllowAppraise()){
                Participant participant = participantService.selectByOrderIdCustomerId(order.getId(), getCurrentCustomerId());
                if(participant != null && participant.getAppraise() == 1){
                    Appraise appraise = newAppraiseService.selectByOrderIdCustomerId(order.getBrandId(),order.getId(), getCurrentCustomerId());
                    if (appraise != null) {
                        if (appraise.getLevel() >= shareSetting.getMinLevel() && appraise.getContent().length() >= shareSetting.getMinLength() && shareSetting.getIsActivity()) {
                            Long remindTime = (new Date().getTime() - appraise.getCreateTime().getTime()) / 1000;
                            if (remindTime >= shareSetting.getDelayTime()) {
                                order.setIsShare(1);
                            }
                        }
                    }else{
                        AppraiseNew aa = newAppraiseNewService.selectByOrderIdCustomerId(order.getBrandId(),order.getId(),getCurrentCustomerId());
                        if (aa.getLevel()/20 >= shareSetting.getMinLevel() && aa.getContent().length() >= shareSetting.getMinLength() && shareSetting.getIsActivity()) {
                            Long remindTime = (new Date().getTime() - aa.getCreateTime().getTime()) / 1000;
                            if (remindTime >= shareSetting.getDelayTime()) {
                                order.setIsShare(1);
                            }
                        }
                    }
                    order.setAllowAppraise(false);
                }
            }
        }
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", orderList);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("findOrderById")
    public Result findOrderById(String id) {
        Order or = orderService.selectById(id);
        return getSuccessResult(or);
    }

    @RequestMapping("/new/findOrderById")
    public void findOrderByIdNew(String id, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Order or = orderService.selectById(id);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();

        net.sf.json.JSONObject orJson = new net.sf.json.JSONObject().fromObject(or);
        orJson.remove("articleNames");
        orJson.remove("orderPaymentItems");
        orJson.remove("orderItems");
        orJson.remove("shopName");
        orJson.put("tableNumber", or.getTableNumber() == null ? null : or.getTableNumber());
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", orJson);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("readylistOrder")
    public Result readylistOrder() {
        List<Order> orderList = orderService.selectReadyOrder(getCurrentShopId());
        return getSuccessResult(orderList);
    }

    @RequestMapping("/new/readylistOrder")
    public void readylistOrderNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        List<Order> orderList = orderService.selectReadyOrder(getCurrentShopId());

        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("message", "请求成功");
        json.put("success", true);
        json.put("data", orderList);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("pushOrder")
    public Result pushOrder(String orderId) throws AppException {

        orderService.pushOrder(orderId);
        return getSuccessResult();
    }

    @RequestMapping("/new/pushOrder")
    public void pushOrderNew(String orderId, HttpServletRequest request, HttpServletResponse response) throws AppException {
        AppUtils.unpack(request, response);

        orderService.pushOrder(orderId);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "0");
        json.put("message", "下单成功");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("getOrderStates")
    public Result getOrderStates(String orderId) {
        Order order = orderService.selectOrderStatesById(orderId);
        if(order.getPaymentAmount().compareTo(BigDecimal.ZERO)==0){
            order.setOrderState(1);
            order.setAllowContinueOrder(true);
        };
        return getSuccessResult(order);
    }

    @RequestMapping("/new/getOrderStates")
    public void getOrderStatesNew(String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        Order order = orderService.selectOrderStatesById(orderId);

        if(order == null){
            json.put("statusCode", "100");
            json.put("message", "未找到订单");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
            return;
        }

        net.sf.json.JSONObject orderJson = new net.sf.json.JSONObject();

        orderJson.put("id", order.getId());
        orderJson.put("orderState", order.getOrderState());
        orderJson.put("productionStatus", order.getProductionStatus());
        orderJson.put("allowContinueOrder", order.getAllowContinueOrder());
        orderJson.put("orderBefore", order.getOrderBefore());
        orderJson.put("allowCancel", order.getAllowCancel());
        orderJson.put("allowAppraise", order.getAllowAppraise());
        orderJson.put("isPay", order.getIsPay());
        orderJson.put("payMode", order.getPayMode());
        orderJson.put("orderMode", order.getOrderMode());
        orderJson.put("tableNumber", order.getTableNumber() == null ? null : order.getTableNumber());

        if(StringUtils.isEmpty(order.getGroupId())){
            if (order.getOrderState() == OrderState.HASAPPRAISE) {
                ShareSetting s = shareSettingService.selectByBrandId(getCurrentBrandId());
                Appraise a = newAppraiseService.selectDeatilByOrderId(order.getBrandId(),order.getId(), getCurrentCustomerId());
                if (a != null) {
                    if (a.getLevel() >= s.getMinLevel() && a.getContent().length() >= s.getMinLength() && s.getIsActivity()) {
                        orderJson.put("isShare", 1);
                        orderJson.put("time", s.getDelayTime());
                        orderJson.put("appraiseTime", a.getCreateTime());
                    }
                }
            }
        }else if(order.getAllowAppraise()){
            ShareSetting s = shareSettingService.selectByBrandId(getCurrentBrandId());
            Participant participant = participantService.selectByOrderIdCustomerId(order.getId(), getCurrentCustomerId());
            if(participant != null && participant.getAppraise() == 1){
                Appraise appraise = newAppraiseService.selectByOrderIdCustomerId(order.getBrandId(),order.getId(), getCurrentCustomerId());
                if (appraise != null) {
                    if (appraise.getLevel() >= s.getMinLevel() && appraise.getContent().length() >= s.getMinLength() && s.getIsActivity()) {
                        Long remindTime = (new Date().getTime() - appraise.getCreateTime().getTime()) / 1000;
                        if (remindTime >= s.getDelayTime()) {
                            order.setIsShare(1);
                            orderJson.put("isShare", 1);
                            orderJson.put("time", s.getDelayTime());
                            orderJson.put("appraiseTime", appraise.getCreateTime());
                        }
                    }
                }

                order.setAllowAppraise(false);
                orderJson.put("allowAppraise", order.getAllowAppraise());
            }
        }



        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", orderJson);
        ApiUtils.setBackInfo(response, json); // 返回信息设置

    }

    @RequestMapping("/new/getOrderInfo")
    public void getOrderInfo(String id, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Order order = orderService.getOrderInfo(id); //得到主订单

        List<Order> orders = orderService.selectByParentId(id,order.getPayType()); //得到子订单
        List<OrderItem> itemList = new ArrayList<>();
        BigDecimal sum = order.getPaymentAmount();
        for (Order child : orders) { //遍历子订单
            if(child.getOrderMoney().doubleValue() <= 0){
                continue;
            }
            child = orderService.getOrderInfo(child.getId());
            if(child.getOrderItems() != null){
                for (OrderItem item : child.getOrderItems()) {
                    itemList.add(item);
                }
            }
            //加上子订单的应付金额
            sum = sum.add(child.getPaymentAmount());
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            itemList.add(orderItem);
        }
        if (order.getAmountWithChildren() != null && order.getAmountWithChildren().doubleValue() != Double.valueOf(0.00)) {
            order.setPaymentAmount(order.getAmountWithChildren());
        }
        order.setOrderItems(itemList);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", new net.sf.json.JSONObject().fromObject(order));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/getOrderDetail")
    public void getOrderDetail(String id, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Order order = orderService.getOrderInfo(id); //得到主订单
        if(order==null){
            ApiUtils.setBackInfo(response, new JSONObject());
        }
        BigDecimal allOrderOriginalAmount = order.getOriginalAmount();
        if(order.getOrderState() == OrderState.SUBMIT && order.getProductionStatus() == ProductionStatus.NOT_ORDER && order.getPayMode() == 1 && order.getIsPay() != OrderPayState.ALIPAYING){
            order.setIsPay(OrderPayState.NOT_PAY);
            orderService.update(order);
        }

        List<Order> orders = orderService.selectByParentId(id,order.getPayType()); //得到子订单
        List<OrderItem> itemList = new ArrayList<>();
        BigDecimal sum = order.getPaymentAmount();
        BigDecimal mealFeeNumber = order.getMealFeePrice();
        for (Order child : orders) { //遍历子订单
            //0元菜品可以展示
//            if(child.getOrderMoney().doubleValue() <= 0){
//                continue;
//            }
            child = orderService.getOrderInfo(child.getId());
            if(child.getOrderItems() != null){
                for (OrderItem item : child.getOrderItems()) {
                    itemList.add(item);
                }
            }
            //加上子订单的应付金额
            sum = sum.add(child.getPaymentAmount());
            mealFeeNumber = mealFeeNumber.add(child.getMealFeePrice());
            allOrderOriginalAmount = allOrderOriginalAmount.add(child.getOriginalAmount());
        }
        for (OrderItem orderItem : order.getOrderItems()) {
            itemList.add(orderItem);
        }
        if (order.getAmountWithChildren() != null && order.getAmountWithChildren().doubleValue() != Double.valueOf(0.00)) {
            order.setPaymentAmount(order.getAmountWithChildren());
            BigDecimal payBefore = orderService.selectPayBefore(order.getId());
            if(order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY
                    && payBefore != null){
                order.setAmountWithChildren(order.getAmountWithChildren().subtract(payBefore));
            }
        }

        List<OrderPaymentItem> orderPayMentItems = orderPaymentItemService.selectByOrderId(order.getId());
        for (OrderPaymentItem item : orderPayMentItems) {
            if (item.getPaymentModeId() == 3) {
                order.setIfUseCoupon(true);
                order.setCouponInfo(couponService.selectById(item.getToPayId()));
            }
        }
        order.setOrderItems(itemList);
        order.setAllOrderOriginalAmount(allOrderOriginalAmount);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("mealFeeNumber", mealFeeNumber);
        json.put("data", new net.sf.json.JSONObject().fromObject(order));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("saveOrder")
    public Result order(@RequestBody Order order) throws AppException {
        order.setCustomerId(getCurrentCustomerId());
        order.setShopDetailId(getCurrentShopId());
        order.setBrandId(getCurrentBrandId());
        JSONResult jsonResult = orderService.createOrder(order);
        order = (Order) jsonResult.getData();

        return jsonResult;
    }

    //目前微信使用该接口保存订单
    @RequestMapping("/new/saveOrder")
    public void orderNew(String orderArray, HttpServletRequest request, HttpServletResponse response) throws AppException {
        AppUtils.unpack(request, response);
        JSONObject myJsonObject = new JSONObject(orderArray);
        JSONObject json = new JSONObject();

        Order order = new Order();
        order.setCreateOrderByAddress("wechat");
        order.setDistributionModeId(Integer.parseInt(myJsonObject.optString("distributionModeId")));
        order.setUseCoupon(myJsonObject.optString("useCoupon").equals("") ? null : myJsonObject.optString("useCoupon"));
        if (myJsonObject.optString("useAccount").equals("true")) {
            order.setUseAccount(true);
        } else if (myJsonObject.optString("useAccount").equals("false")) {
            order.setUseAccount(false);
        }
        if (!StringUtils.isEmpty(myJsonObject.optString("tableNumber"))) {
            order.setTableNumber(myJsonObject.optString("tableNumber"));
        }
        if (!StringUtils.isEmpty(myJsonObject.optString("beforeId"))) {
            order.setBeforeId(myJsonObject.optString("beforeId"));
        }

        if (!StringUtils.isEmpty(myJsonObject.optString("verCode"))) {
            order.setVerCode(myJsonObject.optString("verCode"));
        }
        if (!StringUtils.isEmpty(myJsonObject.optString("groupId"))) {
            order.setGroupId(myJsonObject.optString("groupId"));
        }
        if (!StringUtils.isEmpty(myJsonObject.optString("waitId"))) {
            order.setWaitId(myJsonObject.optString("waitId"));
        }
        if (!StringUtils.isEmpty(myJsonObject.optString("useProductCoupon"))){
            order.setUseProductCoupon(Integer.parseInt(myJsonObject.optString("useProductCoupon")));
        }
        if (!StringUtils.isEmpty(myJsonObject.optString("productCouponArticleId"))){
            order.setProductCouponArticleId(myJsonObject.optString("productCouponArticleId"));
        }
        if (!StringUtils.isEmpty(myJsonObject.optString("productCouponMoney"))) {
            order.setProductCouponMoney(new BigDecimal(myJsonObject.optString("productCouponMoney")));
        }

        Integer payMode = myJsonObject.optInt("payMode");
        order.setPayMode(payMode);

        //标识标识该笔订单是不是加菜订单
        boolean flg = false;
        if (!StringUtils.isEmpty(myJsonObject.optString("parentOrderId"))) {
            order.setParentOrderId(myJsonObject.optString("parentOrderId"));
            Order faOrder = orderService.selectById(order.getParentOrderId());
            if(faOrder.getGroupId() != null && !"".equals(faOrder.getGroupId())
                    && order.getGroupId() != null && !"".equals(order.getGroupId())
                    && !order.getGroupId().equals(faOrder.getGroupId())){
                order.setParentOrderId(null);
            } else {
                order.setCustomerCount(faOrder.getCustomerCount());
                order.setServicePrice(new BigDecimal(0));
                //当前订单为加菜订单，添加标识用于NewPos校验微信端与NewPos端不可同步加菜
                redisService.set(order.getParentOrderId().concat("ADDADISH"), true);
                flg = true;
            }

            //如果加菜订单支付的时候 主订单未支付  则支付失败
            if("0".equals(myJsonObject.optString("payType"))){
                Order fatherOrder = orderService.selectById(myJsonObject.optString("parentOrderId"));
                if(!fatherOrder.getCustomerId().equals(getCurrentCustomerId())){
                    redisService.set(order.getParentOrderId().concat("ADDADISH"), false);
                    json.put("message", "该订单已被其他小伙伴执行操作，请重新扫码");
                    json.put("success", false);
                    json.put("code", 50);
                    ApiUtils.setBackInfo(response, json);
                    return;
                }
                if(fatherOrder.getOrderState() == OrderState.SUBMIT){
                    //支付失败，取消当前主订单正在加菜的标识
                    redisService.set(order.getParentOrderId().concat("ADDADISH"), false);
                    json.put("message", "买单失败，您有未支付的订单，请共同支付！");
                    json.put("success", false);
                    json.put("code", 50);
                    ApiUtils.setBackInfo(response, json);
                    return;
                }
            }
        }else{
            BigDecimal servicePrice = new BigDecimal(myJsonObject.optString("servicePrice"));
            Order lastOrder = orderService.lastOrderByCustomer(getCurrentCustomerId(), getCurrentShopId(), order.getTableNumber());
            if(lastOrder == null){
                order.setServicePrice(servicePrice);
            }else{
                order.setServicePrice(new BigDecimal(0));
            }

            if (!StringUtils.isEmpty(myJsonObject.optString("customerCount"))) {
                try {
                    order.setCustomerCount(Integer.parseInt(myJsonObject.optString("customerCount")));
                    if (order.getCustomerCount() < 0) {
                        json.put("statusCode", "-1");
                        json.put("success", false);
                        json.put("message", "就餐人数格式错误！");
                        ApiUtils.setBackInfo(response, json); // 返回信息设置
                        return;
                    }
                } catch (Exception e) {
                    json.put("statusCode", "-1");
                    json.put("success", false);
                    json.put("message", "就餐人数格式错误！");
                    ApiUtils.setBackInfo(response, json); // 返回信息设置
                    log.error(e.getMessage());
                    return;
                }
            }
        }

        BigDecimal pa = new BigDecimal(myJsonObject.optString("paymentAmount"));
        order.setPaymentAmount(pa);

        BigDecimal mdm = new BigDecimal(myJsonObject.optString("memberDiscountMoney"));
        order.setMemberDiscountMoney(mdm);
        if (!StringUtils.isEmpty(myJsonObject.optString("memberDiscount"))) {
            order.setMemberDiscount(new BigDecimal(myJsonObject.optString("memberDiscount")));
        }else{
            order.setMemberDiscount(new BigDecimal(1));
        }

        BigDecimal waitMoney = new BigDecimal(myJsonObject.optString("waitMoney"));
        order.setWaitMoney(waitMoney);

        BigDecimal mealFeePrice = new BigDecimal(myJsonObject.optString("mealFeePrice"));
        order.setMealFeePrice(mealFeePrice);

        Integer mealAllNumber = Integer.parseInt(myJsonObject.optString("mealAllNumber"));
        order.setMealAllNumber(mealAllNumber);

        Integer payType = Integer.parseInt(myJsonObject.optString("payType"));
        order.setPayType(payType);

        String remark = myJsonObject.optString("remark");
        order.setRemark(remark);

        JSONArray items = myJsonObject.getJSONArray("orderItems");//获取JSONArray
        List<OrderItem> itemsList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            OrderItem item = new OrderItem();
            item.setCustomerId(items.getJSONObject(i).optString("customerId"));
            item.setArticleId(items.getJSONObject(i).optString("articleId"));
            item.setCount(Integer.parseInt(items.getJSONObject(i).optString("count")));
            item.setType(Integer.parseInt(items.getJSONObject(i).optString("type")));
            item.setRecommendId(items.getJSONObject(i).optString("recommendId"));
            item.setParentId(items.getJSONObject(i).optString("parentId"));
            String discount = items.getJSONObject(i).optString("discount");
            item.setDiscount(StringUtils.isEmpty(discount) ? 100 : Integer.valueOf(discount));
            if (!StringUtils.isEmpty(items.getJSONObject(i).optString("price"))) {
                item.setPrice(new BigDecimal(Double.parseDouble(items.getJSONObject(i).optString("price"))));
            }
            if (!StringUtils.isEmpty(items.getJSONObject(i).optString("weight"))) {
                item.setWeight(new BigDecimal(Double.parseDouble(items.getJSONObject(i).optString("weight"))));
            }
            if (items.getJSONObject(i).optString("name") != null) {
                item.setName(items.getJSONObject(i).optString("name"));
            }
            if (items.getJSONObject(i).optString("articleId") != null) {
                item.setArticleId(items.getJSONObject(i).optString("articleId"));
            }
            if (Integer.parseInt(items.getJSONObject(i).optString("type")) == 3) {
                String mealItemsString = items.getJSONObject(i).optString("mealItems");
                String str[] = mealItemsString.substring(1, mealItemsString.length() - 1).split(",");
                Integer array[] = new Integer[str.length];
                for (int j = 0; j < str.length; j++) {
                    if (!StringUtils.isEmpty(str[j].trim())) {
                        array[j] = Integer.parseInt(str[j].trim());
                    }
                }
                item.setMealItems(array);
            }
            itemsList.add(item);
        }
        order.setOrderItems(itemsList);
        order.setCustomerId(getCurrentCustomerId());
        order.setShopDetailId(getCurrentShopId());
        order.setBrandId(getCurrentBrandId());
        order.setCustomerAddressId(myJsonObject.optString("customerAddressId"));

        if(order.getParentOrderId() != null && !"".equals(order.getParentOrderId())){
            // 加菜品 如果订单折扣过  则恢复未折扣的样子
            Order faOrder = orderService.selectById(order.getParentOrderId());
            if((faOrder.getPosDiscount().compareTo(new BigDecimal(1)) != 0
                    || faOrder.getEraseMoney().compareTo(new BigDecimal(0)) != 0
                    || faOrder.getNoDiscountMoney().compareTo(new BigDecimal(0)) != 0) && faOrder.getOrderState() == OrderState.SUBMIT){
                orderService.posDiscount(order.getParentOrderId(), new BigDecimal(1), new ArrayList<OrderItem>(), new BigDecimal(0), new BigDecimal(0), 0, new BigDecimal(0));
            }
        }
        log.info("此处开始创建订单咯：！");
        //创建订单
        JSONResult jsonResult = orderService.createOrder(order);
        //加菜完成，取消主订单正在加菜的标识
        if (flg) {
            redisService.set(order.getParentOrderId().concat("ADDADISH"), false);
        }
        order = (Order) jsonResult.getData();
        log.info("jsonResult.isSuccess():" + jsonResult.isSuccess());
        log.info("jsonResult.getMessage():" + jsonResult.getMessage());
        json.put("statusCode", "0");
        if (jsonResult.isSuccess() == true) {
            if (!(order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY)
                    && order.getOrderState() != OrderState.SUBMIT){
                //先付、电视叫号模式订单已支付时将订单发送给NewPos端
                orderService.sendPosNewOrder(getCurrentShopId(), order);
            }else if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY){
                //后付订单直接向NewPos推送订单
                orderService.sendPosNewOrder(getCurrentShopId(), order);
            }
            log.info("发送至NewPOS：" + jsonResult.toString());
            json.put("success", true);
            json.put("message", "请求成功");
            json.put("data", new JSONObject(order));
        } else {
            json.put("success", false);
            json.put("message", jsonResult.getMessage());
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/repayOrder")
    public void repayOrder(String orderArray, HttpServletRequest request, HttpServletResponse response) throws AppException {
        AppUtils.unpack(request, response);

        JSONObject myJsonObject = new JSONObject(orderArray);
        JSONObject json = new JSONObject();

        Order order = new Order();
        order.setUseCoupon(myJsonObject.optString("useCoupon").equals("") ? null : myJsonObject.optString("useCoupon"));
        if (myJsonObject.optString("useAccount").equals("true")) {
            order.setUseAccount(true);
        } else if (myJsonObject.optString("useAccount").equals("false")) {
            order.setUseAccount(false);
        }


        if (!StringUtils.isEmpty(myJsonObject.optString("waitId"))) {
            order.setWaitId(myJsonObject.optString("waitId"));
        }

        if (!StringUtils.isEmpty(myJsonObject.optString("id"))) {
            order.setId(myJsonObject.optString("id"));
        }


        Integer payMode = myJsonObject.optInt("payMode");
        order.setPayMode(payMode);


        if (!StringUtils.isEmpty(myJsonObject.optString("customerCount"))) {
            try {
                order.setCustomerCount(Integer.parseInt(myJsonObject.optString("customerCount")));
                if (order.getCustomerCount() < 0) {
                    json.put("statusCode", "-1");
                    json.put("success", false);
                    json.put("message", "就餐人数格式错误！");
                    ApiUtils.setBackInfo(response, json); // 返回信息设置
                    return;
                }
            } catch (Exception e) {
                json.put("statusCode", "-1");
                json.put("success", false);
                json.put("message", "就餐人数格式错误！");
                ApiUtils.setBackInfo(response, json); // 返回信息设置
                log.error(e.getMessage());
                return;
            }

        }

        BigDecimal pa = new BigDecimal(myJsonObject.optString("paymentAmount"));
        order.setPaymentAmount(pa);

        BigDecimal waitMoney = new BigDecimal(myJsonObject.optString("waitMoney"));
        order.setWaitMoney(waitMoney);

        BigDecimal servicePrice = new BigDecimal(myJsonObject.optString("servicePrice"));
        order.setServicePrice(servicePrice);


        JSONResult jsonResult = orderService.repayOrder(order);
        order = (Order) jsonResult.getData();


        json.put("statusCode", "0");
        if (jsonResult.isSuccess() == true) {
            json.put("success", true);
            json.put("message", "请求成功");
            json.put("data", new JSONObject(order));
        } else {
            json.put("success", false);
            json.put("message", jsonResult.getMessage());
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("cancelOrder")
    public Result cancelOrder(String orderId) {
        orderService.cancelOrder(orderId);
        return getSuccessResult();
    }

    @RequestMapping("/new/cancelOrder")
    public void cancelOrderNew(String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        String message = null;
        boolean success = true;
        try{
            orderService.cancelOrder(orderId);
        }catch (Exception e){
            message = "取消订单失败！订单异常。";
            success = false;
            e.printStackTrace();
        }
        json.put("statusCode", "0");
        json.put("message", message);
        json.put("success", success);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("customerNewOrder")
    public Result customerNewOrder(String orderId) {
        if (getCurrentShop() == null) {
            return getSuccessResult();
        }
        Order order = orderService.findCustomerNewOrder(getCurrentCustomerId(), getCurrentShopId(), orderId);
        try {
            processOrder(order);
        } catch (ParseException e) {
            Result result = new Result(e.getMessage(), false);
            log.error(e.getMessage());
            return result;
        }
        return getSuccessResult(order);
    }

    @RequestMapping("/new/customerNewOrder")
    public void customerNewOrderNew(String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        if (getCurrentShop() == null) {
            json.put("statusCode", "0");
            json.put("message", "请求失败");
            json.put("success", false);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }

        Order order = orderService.findCustomerNewOrder(getCurrentCustomerId(), getCurrentShopId(), orderId);
//        if(order.getPaymentAmount()==null || order.getPaymentAmount().compareTo(BigDecimal.ZERO)==0){
//            order.setAllowContinueOrder(true);
//            order.setOrderState(1);
//        }
        ShareSetting s = shareSettingService.selectByBrandId(getCurrentBrandId());
        if (order != null) {
            if(StringUtils.isEmpty(order.getGroupId())){
                if (order.getOrderState() == OrderState.HASAPPRAISE) {
                    Appraise a = newAppraiseService.selectDeatilByOrderId(order.getBrandId(),order.getId(), getCurrentCustomerId());
                    if (a != null) {
                        if (a.getLevel() >= s.getMinLevel() && a.getContent().length() >= s.getMinLength() && s.getIsActivity()) {
                            Long remindTime = (new Date().getTime() - a.getCreateTime().getTime()) / 1000;
                            if (remindTime >= s.getDelayTime()) {
                                order.setIsShare(1);
                            }
                        }
                    }else{
                        AppraiseNew aa = newAppraiseNewService.selectByOrderIdCustomerId(order.getBrandId(),order.getId(),getCurrentCustomerId());
                        if (aa.getLevel()/20 >= s.getMinLevel() && aa.getContent().length() >= s.getMinLength() && s.getIsActivity()) {
                            Long remindTime = (new Date().getTime() - aa.getCreateTime().getTime()) / 1000;
                            if (remindTime >= s.getDelayTime()) {
                                order.setIsShare(1);
                            }
                        }
                    }
                }
            }else if(order.getAllowAppraise()){
                Participant participant = participantService.selectByOrderIdCustomerId(order.getId(), getCurrentCustomerId());
                if(participant != null && participant.getAppraise() == 1){
                    Appraise appraise = newAppraiseService.selectByOrderIdCustomerId(order.getBrandId(),order.getId(), getCurrentCustomerId());
                    if (appraise != null) {
                        if (appraise.getLevel() >= s.getMinLevel() && appraise.getContent().length() >= s.getMinLength() && s.getIsActivity()) {
                            Long remindTime = (new Date().getTime() - appraise.getCreateTime().getTime()) / 1000;
                            if (remindTime >= s.getDelayTime()) {
                                order.setIsShare(1);
                            }
                        }
                    }
                    order.setAllowAppraise(false);
                }
            }

        }
        try {
            processOrder(order);
        } catch (ParseException e) {
            log.error(e.getMessage());
            json.put("message", e.getMessage());
            json.put("success", false);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
        json.put("statusCode", "0");
        json.put("message", "");
        json.put("success", true);
        if (order != null) {
            json.put("data", new net.sf.json.JSONObject().fromObject(order));
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置

    }

    @RequestMapping("customerNewPackage")
    public Result customerNewPackage() {
        Order order = orderService.findCustomerNewPackage(getCurrentCustomerId(), getCurrentShopId());
        try {
            processOrder(order);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return new Result(e.getMessage(), false);
        }
        return getSuccessResult(order);
    }

    @RequestMapping("/new/customerNewPackage")
    public void customerNewPackageNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        Order order = orderService.findCustomerNewPackage(getCurrentCustomerId(), getCurrentShopId());
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        try {
            processOrder(order);
        } catch (ParseException e) {
            log.error(e.getMessage());
            json.put("message", e.getMessage());
            json.put("success", false);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
        json.put("statusCode", "0");
        json.put("success", true);
        json.put("message", "请求成功");

        if (order != null && StringUtils.isEmpty(order.getGroupId())) {
            json.put("data", new net.sf.json.JSONObject().fromObject(order));
        }else if (order != null && !StringUtils.isEmpty(order.getGroupId())){
            Participant participant = participantService.selectByOrderIdCustomerId(order.getId(), getCurrentCustomerId());
            if(participant != null && participant.getAppraise() != 1) {
                json.put("data", new net.sf.json.JSONObject().fromObject(order));
            }
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    private void processOrder(Order order) throws ParseException {
        if (order != null) {
            List<OrderItem> oitem = new ArrayList<>();
            for (OrderItem item : order.getOrderItems()) {
                OrderItem newItem = new OrderItem();
                newItem.setId(item.getId());
                newItem.setType(item.getType());
                newItem.setArticleName(item.getArticleName());
                newItem.setCount(item.getCount());
                newItem.setArticleId(item.getArticleId());
                newItem.setCreateTime(item.getCreateTime());
                oitem.add(newItem);
            }
            order.setOrderItems(oitem);
            ShopDetail shop = getShopMap().get(order.getShopDetailId());
            order.setShopName(shop.getName());
            order.setOriginalAmount(order.getOriginalAmount());
        }
    }

    @RequestMapping("checkShopId")
    public Result checkShopId(String shopId, String orderId) {
        if (StringUtils.isEmpty(shopId)) {
            return new Result(true);
        }
        Boolean result = orderService.checkShop(orderId, shopId);
        String message = "";
        if (!result) {
            message = "该桌号与门店信息不符，请核对所在门店信息";
        }
        return new Result(message, result);
    }

    @RequestMapping("/new/checkShopId")
    public void checkShopIdNew(String shopId, String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        if (StringUtils.isEmpty(shopId)) {
            json.put("success", true);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
        Boolean result = orderService.checkShop(orderId, shopId);
        String message = "";
        if (!result) {
            message = "该桌号与门店信息不符，请核对所在门店信息";
        }
        json.put("statusCode", "0");
        json.put("message", message);
        json.put("success", result);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("checkArticleCount")
    public Result checkArticleCount(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            return new Result("订单号不存在", false);
        }
        return orderService.checkArticleCount(orderId);
    }

    @RequestMapping("settable")
    public Result setTableNumber(String orderId, String tableNumber) {
        orderService.setTableNumber(orderId, tableNumber);
        return getSuccessResult();
    }

    @RequestMapping("/new/settable")
    @ResponseBody
    public void setTableNumberNew(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        JSONObject json = new JSONObject();

        String tableNumber = queryParams.get("tableNumber").toString();
        String orderId = queryParams.get("orderId").toString();
        Order order = orderService.selectById(orderId);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        if (order.getDistributionModeId() == DistributionType.TAKE_IT_SELF && shopDetail.getContinueOrderScan() == Common.NO) { //如果是外带的情况
            tableNumber = order.getVerCode();
        }

        if (StringUtils.isEmpty(tableNumber) && (order.getDistributionModeId() != DistributionType.TAKE_IT_SELF || shopDetail.getContinueOrderScan() == Common.YES)
                ) {
            json.put("statusCode", "100");
            json.put("success", false);
            json.put("message", "扫码失败，请重新扫码");
        } else {
            orderService.setTableNumber(orderId, tableNumber);
            json.put("success", true);
            json.put("statusCode", "200");
            json.put("message", "扫码成功");
        }
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("payOrderWx")
    public Result payOrderWx(String orderId, HttpServletRequest request) throws UnknownHostException, DocumentException {
        Order order = orderService.selectById(orderId);
        String body = "restoPay";
        String uuid = ApplicationUtils.randomUUID();
        String out_trade_no = uuid;
        String attach = order.getId();
        int total_fee = order.getPaymentAmount().multiply(new BigDecimal(100)).intValue();
        String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();  //终端IP String(16)
        String openid = getCurrentCustomer().getWechatId();
        String notifyUrl = getBaseUrl() + "pay/orderpay_notify";
        WechatConfig config = getCurrentBrand().getWechatConfig();
        Map<String, String> apiReqeust = WeChatPayUtils.createJSAPIPayRequest(body, out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, config.getAppid(), config.getMchid(), config.getMchkey(), attach);
        log.info("获取pay api返回结果成功:" + apiReqeust);
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

    @RequestMapping("/new/payOrderWxNew")
    @ResponseBody
    public Result payOrderWxNew(String orderId, String factMoney, HttpServletRequest request) throws UnknownHostException, DocumentException {
        Order order = orderService.selectById(orderId);
        String body = "restoPay";
        String uuid = ApplicationUtils.randomUUID();
        String out_trade_no = uuid;
        String attach = order.getId();
        int total_fee = Integer.parseInt(factMoney);
        String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();  //终端IP String(16)
        String openid = getCurrentCustomer().getWechatId();
        String notifyUrl = getBaseUrl() + "pay/houfu/orderpay_notify";
        WechatConfig config = getCurrentBrand().getWechatConfig();
        Map<String, String> apiReqeust = WeChatPayUtils.createJSAPIPayRequest(body, out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, config.getAppid(), config.getMchid(), config.getMchkey(), attach);
        log.info("获取pay api返回结果成功:" + apiReqeust);
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

    @RequestMapping("/new/payOrderWx")
    public void payOrderWx(HttpServletRequest request, HttpServletResponse response) throws UnknownHostException, DocumentException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String orderId = queryParams.get("orderId").toString();

        DataSourceTarget.setDataSourceName(getCurrentBrandId());

//        if(RedisUtil.get(orderId + "WxPayBrandId") != null){
//            RedisUtil.set(orderId + "WxPayBrandId", getCurrentBrandId());
//        }

        JSONObject json = new JSONObject();
        log.error("当前orderId为:"+orderId+"1111111111111111111111");
        Order order = orderService.selectOrderInfo(orderId);
        log.error("当前orderId为:"+orderId+"2222222222222222222222");
        Brand brand = brandService.selectByPrimaryKey(order.getBrandId());
        log.error("当前orderId为:"+orderId+"3333333333333333333333");
        log.error("当前orderId为:"+orderId+"4444444444444444444444");
        if (redisService.get(orderId + "wxPaySuccess") != null && Integer.parseInt(redisService.get(orderId + "wxPaySuccess").toString()) == 2) {
            log.error("当前orderId为:"+orderId+"5555555555555555555");
            json.put("message", "支付状态异常：订单支付中或已支付");
            json.put("success", false);
            ApiUtils.setBackInfo(response, json);
            return;
        }
        if (order.getIsPay() != OrderPayState.NOT_PAY && order.getIsPay() != OrderPayState.ALIPAYING) { //如果订单的支付状态不是未支付
            log.error("当前orderId为:"+orderId+"6666666666666666666666");
            json.put("message", "支付状态异常：订单支付中或已支付");
            json.put("success", false);
            ApiUtils.setBackInfo(response, json);
            return;
        }
        //用户发起微信支付记录UserAction日志
        log.error("当前orderId为:"+orderId+"77777777777777777777");
        //标识当前订单正在发起微信支付
        redisService.set(order.getId() + "WxPay", true, 60L);
        Map customerMap = new HashMap(4);
        customerMap.put("brandName", brand.getBrandName());
        customerMap.put("fileName", order.getCustomerId());
        customerMap.put("type", "UserAction");
        customerMap.put("content", "用户:"+order.getCustomerId()+"使用微信发起支付，订单Id:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(customerMap);

        String body = "restoPay";
        String uuid = ApplicationUtils.randomUUID();
        String out_trade_no = uuid;
        String attach = order.getId();
        int total_fee = order.getPaymentAmount().multiply(new BigDecimal(100)).intValue();
        String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();  //终端IP String(16)
        Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),order.getCustomerId());  //获取用户会员
        String openid = customer.getWechatId();
        String notifyUrl = getBaseUrl() + "pay/orderpay_notify";
        WechatConfig config = getCurrentBrand().getWechatConfig();
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        Map<String, String> apiReqeust;
        if (shopDetail.getWxServerId() == null) {
            apiReqeust = WeChatPayUtils.createJSAPIPayRequest(body,
                    out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, StringUtils.isEmpty(shopDetail.getAppid()) ? config.getAppid() : shopDetail.getAppid(),
                    StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), StringUtils.isEmpty(shopDetail.getMchkey()) ? config.getMchkey() : shopDetail.getMchkey(), attach);
        } else {
            WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());

            apiReqeust = WeChatPayUtils.createJSAPIPayRequestNew(shopDetail.getAppid(), body,
                    out_trade_no, total_fee, spbill_create_ip, openid, notifyUrl, wxServerConfig.getAppid(),
                    wxServerConfig.getMchid(), wxServerConfig.getMchkey(), attach, StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), shopDetail.getIsSub());
        }

        log.info("获取pay api返回结果成功:" + apiReqeust);
        if (apiReqeust.containsKey("ERROR")) {
            json.put("message", apiReqeust.get("ERROR"));
            json.put("success", false);
        } else {
            json.put("statusCode", "0");
            json.put("message", "");
            json.put("success", true);
            json.put("data", new JSONObject(apiReqeust));
            if(StringUtils.isEmpty(order.getOperatorId())){
                order.setOperatorId("1");
            }else{
                order.setOperatorId("sb");
            }
            if(order.getIsPay() != OrderPayState.ALIPAYING){
                order.setIsPay(OrderPayState.PAYING);
            }
            log.info("订单现在的状态是"+order.getOrderState());
            orderService.update(order);
        }
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("/getRecommend")
    @ResponseBody
    public Recommend getRecommend(String articleId) {
        return newArticleRecommendService.getRecommendByArticleId(getCurrentBrandId(),articleId, getCurrentShopId());
    }

    @RequestMapping("/new/getRecommend")
    @ResponseBody
    public void getRecommendNew(String articleId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Recommend recomment = newArticleRecommendService.getRecommendByArticleId(getCurrentBrandId(),articleId, getCurrentShopId());
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "");
        json.put("success", true);
        if (recomment != null) {
            json.put("data", new net.sf.json.JSONObject().fromObject(recomment));
        }
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("/getUnit")
    @ResponseBody
    public List<Unit> getUnit(String articleId) {
        return newUnitService.getUnitByArticleid(getCurrentBrandId(),articleId);
    }

    @RequestMapping("/new/getUnit")
    @ResponseBody
    public void getUnitNew(String articleId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        List<Unit> units = newUnitService.getUnitByArticleidWechat(getCurrentBrandId(),articleId);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "");
        json.put("success", true);
        if (units != null) {
            json.put("data", units);
        }
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("/new/payPrice")
    @ResponseBody
    public void payPrice(BigDecimal factMoney, String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        orderService.payPrice(factMoney, orderId);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("/new/useRedPrice")
    public void useRedPrice(BigDecimal factMoney, String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        orderService.useRedPrice(factMoney, orderId);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("/new/third")
    public void getOrderByHunger(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        queryParams.put("pushType", PlatformType.E_LE_ME);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if (brandSetting.getIsOpenOutFood() == 0) {
            json.put("code", "100");
            json.put("message", "false");
            ApiUtils.setBackInfo(response, json);
        }
        queryParams.put("brandId", getCurrentBrandId());
        if (thirdService.orderAccept(queryParams, brandSetting)) {
            json.put("code", "200");
            json.put("message", "ok");
        } else {
            json.put("code", "100");
            json.put("message", "false");
        }
        ApiUtils.setBackInfo(response, json);
    }


    @RequestMapping("/new/third/test_eleme")
    public void testEleme(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        queryParams.put("pushType", PlatformType.E_LE_ME);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if (brandSetting.getIsOpenOutFood() == 0) {
            json.put("code", "100");
            json.put("message", "false");
            ApiUtils.setBackInfo(response, json);
        }
        queryParams.put("brandId", getCurrentBrandId());
        if (thirdService.orderAccept(queryParams, brandSetting)) {
            json.put("code", "200");
            json.put("message", "ok");
        } else {
            json.put("code", "100");
            json.put("message", "false");
        }
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("/new/eleme/version2.0/test")
    public void testElemeVersion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        StringBuilder basicContent = new StringBuilder();
        basicContent.append(key).append(":").append(secret);
        String encodeToString = Base64.encodeToString(basicContent.toString().getBytes(), false);
        sb.append("Basic").append(" ").append(encodeToString);

        MobilePackageBean mobile = AppElemeUtils.unpack(request, response, null);
        Map queryParams = (Map) mobile.getContent();
        queryParams.put("pushType", PlatformType.ELE_ME_VERSION_2);

        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8"));
        StringBuffer s = new StringBuffer("");
        String temp;
        while ((temp = br.readLine()) != null) {
            s.append(temp);
        }
        br.close();
        String params = s.toString();
        log.info(params);
        queryParams.put("oMessage", params);
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();

        //定位数据库
        com.alibaba.fastjson.JSONObject messageJson = com.alibaba.fastjson.JSONObject.parseObject(params);
        if(messageJson == null){
            json.put("code", "200");
            json.put("message", "ok");
            ApiUtils.setBackInfo(response, json);
        }else{
            Integer elemeShopId = messageJson.getInteger("shopId");
            ShopDetail shopDetail = shopDetailService.selectByRestaurantId(elemeShopId);
            DataSourceTarget.setDataSourceName(shopDetail.getBrandId());

            BrandSetting brandSetting = brandSettingService.selectByBrandId(shopDetail.getBrandId());
            if (brandSetting.getIsOpenOutFood() == 0) {
                json.put("code", "100");
                json.put("message", "false");
                ApiUtils.setBackInfo(response, json);
            }
            queryParams.put("brandId", shopDetail.getBrandId());
            log.info("推送进入:" + queryParams.get("brandId").toString() + "订单对应的店铺：" + shopDetail.getId());
            if (thirdService.orderAccept(queryParams, brandSetting)) {
                log.info("记录推送订单成功");
                json.put("code", "200");
                json.put("message", "ok");
                ApiUtils.setBackInfo(response, json);
            } else {
                log.info("记录推送订单失败");
                json.put("success", "false");
                json.put("code", "200");
                json.put("message", "ok");
                ApiUtils.setBackInfo(response, json);
            }
        }
    }

    @RequestMapping("/new/getUnitAll")
    @ResponseBody
    public void getUnitAll(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        //listc存放shop下all
        List<UnitArticleDto> units  = newUnitService.selectUnitDetail(getCurrentBrandId(),getCurrentShopId());

//        List<UnitArticle> units = unitService.selectUnitDetail(getCurrentShopId());
        //map存放article下all
        String articleId = null;
        Map<String, List<UnitArticleDto>> map = new HashMap<>();
        for (int i = 0; i < units.size(); i++) {
            UnitArticleDto unit = units.get(i);
            if (!unit.getArticleId().equals(articleId)) {
                articleId = unit.getArticleId();
                List<UnitArticleDto> unitArticles = new ArrayList<>();
                for (int j = 0; j < units.size(); j++) {
                    if (articleId.equals(units.get(j).getArticleId())) {
                        unitArticles.add(units.get(j));
                    }
                }
                map.put(articleId, unitArticles);
            }
        }
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "");
        json.put("success", true);
        json.put("data", units);
        json.put("map", map);
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("/getTable")
    public void getTable(String id, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        TableQrcode tableQrcode = tableQrcodeService.selectById(Long.parseLong(com.resto.wechat.web.util.Encrypter.decrypt(id)));

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        if (tableQrcode != null) {
            json.put("data", new JSONObject(tableQrcode));
        } else {
            json.put("data", "");
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/refundPaymentByUnfinishedOrder")
    public Result refundPaymentByUnfinishedOrder(String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        return orderService.refundPaymentByUnfinishedOrder(orderId);
    }


    @RequestMapping("/new/afterPay")
    public void afterPay(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        JSONObject json = new JSONObject();

        String orderId = String.valueOf(queryParams.get("orderId"));
        String couponId = String.valueOf(queryParams.get("couponId"));
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(queryParams.get("price").toString()));
        BigDecimal pay =  BigDecimal.valueOf(Double.parseDouble(queryParams.get("pay").toString()));
        BigDecimal waitMoney =  BigDecimal.valueOf(Double.parseDouble(queryParams.get("waitMoney").toString()));
        Integer payMode = Integer.valueOf(queryParams.get("payMode").toString());

        Order order = orderService.selectById(orderId);
        if(order == null){
            json.put("message", "订单异常，重新支付！");
            json.put("success", false);
            json.put("code", 50);
            ApiUtils.setBackInfo(response, json);
            return;
        }
        ShopDetail shop =shopDetailService.selectByPrimaryKey(order.getShopDetailId());

        Boolean loginFlag = (Boolean) redisService.get(getCurrentShopId() + "loginStatus");
        if(shop.getPosVersion() == PosVersion.VERSION_2_0){
            if (loginFlag == null || loginFlag == false) {
                json.put("message", "当前店铺暂未开启在线点餐，请联系服务员详询，谢谢");
                json.put("success", false);
                json.put("code", 50);
                ApiUtils.setBackInfo(response, json);
                return;
            }
        }

        //如果加菜订单支付的时候 主订单未支付  则支付失败
        if(order.getParentOrderId() != null && !"".equals(order.getParentOrderId())){
            Order fatherOrder = orderService.selectById(order.getParentOrderId());
            if(!fatherOrder.getCustomerId().equals(order.getCustomerId())){
                json.put("message", "该订单已被其他小伙伴执行操作，请重新扫码");
                json.put("success", false);
                json.put("code", 50);
                ApiUtils.setBackInfo(response, json);
                return;
            }
            if(fatherOrder.getOrderState() == OrderState.SUBMIT){
                json.put("message", "买单失败，您有未支付的订单，请共同支付！");
                json.put("success", false);
                json.put("code", 50);
                ApiUtils.setBackInfo(response, json);
                return;
            }
        }

        if(order.getNeedConfirmOrderItem() == 1){
            json.put("message", "请等待厨房给称重餐品称重确认后付款！");
            json.put("success", false);
            json.put("code", 50);
            ApiUtils.setBackInfo(response, json);
            return;
        }

        if(payMode == PayMode.WEIXIN_PAY){
//            if(!MemcachedUtils.add(orderId + "WxPayAgain", 1, 10)){
            if(!redisService.setnxTime(orderId + "WxPayAgain", 1, 10)){
                json.put("message", "微信支付过于频繁，请10秒再试！");
                json.put("success", false);
                json.put("code", 50);
                ApiUtils.setBackInfo(response, json);
                return;
            }
        }

        if(order.getOrderState() == OrderState.PAYMENT || order.getOrderState() == OrderState.CONFIRM || order.getOrderState() == OrderState.HASAPPRAISE){
            json.put("message", "订单已经被人买单！");
            json.put("success", false);
            json.put("code", 50);
            ApiUtils.setBackInfo(response, json);
            return;
        }
        BigDecimal finalMoney = pay.setScale(2,BigDecimal.ROUND_DOWN).add(price.setScale(2,BigDecimal.ROUND_DOWN));
//        if(!StringUtils.isEmpty(couponId)){
//            Coupon coupon = couponService.selectById(couponId);
//            finalMoney.subtract(coupon.getValue().setScale(2,BigDecimal.ROUND_DOWN)).subtract(waitMoney.setScale(2,BigDecimal.ROUND_DOWN));
//        }
        if(order.getAmountWithChildren().doubleValue() > 0){
            //后付的时候可能存在切换支付   当切换支付前段传送的price值为0.00  并且出现在后付的时候  此时应该重新获取他的price值；
            if(price.doubleValue() == 0 && order.getPayType() == PayType.NOPAY){
                BigDecimal value = orderService.selectPayBefore(orderId);
                if(value != null && value.doubleValue() > 0){
                    finalMoney = finalMoney.add(value);
                }
            }else{
                List<Coupon> coupon = couponService.usedCouponBeforeByOrderId(orderId);
                if(coupon.size()==1){
                    OrderPaymentItem item = orderPaymentItemService.selectById(coupon.get(0).getId());
                    finalMoney = finalMoney.add(item.getPayValue());
                }
            }
            if(finalMoney.doubleValue() != order.getAmountWithChildren().doubleValue()){
                json.put("success", false);
                json.put("code", "100");
                json.put("message", "订单金额发生改变，请重新买单！");
                ApiUtils.setBackInfo(response, json); // 返回信息设置
                return;
            }
        }else{
            if(finalMoney.doubleValue() != order.getPaymentAmount().doubleValue()){
                json.put("success", false);
                json.put("code", "100");
                json.put("message", "订单金额发生改变，请重新买单！");
                ApiUtils.setBackInfo(response, json); // 返回信息设置
                return;
            }
        }
        JSONResult jsonResult = orderService.afterPay(orderId, couponId, price, pay, waitMoney, payMode, getCurrentCustomerId());
        if(jsonResult.isSuccess()){
            Order o = (Order) jsonResult.getData();
            orderService.confirmOrder(o);
            redisService.remove(o.getShopDetailId()+o.getTableNumber()+"status");
            json.put("code", "200");
            json.put("success", true);
            json.put("message", "请求成功");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }else{
            json.put("success", false);
            json.put("message", jsonResult.getMessage());
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
    }

    @RequestMapping("/new/updateIsPay")
    public void updateIsPay(String orderId, HttpServletRequest request, HttpServletResponse response){
        Order order = orderService.selectById(orderId);
        if(order.getIsPay() != OrderPayState.ALIPAYING){
            order.setIsPay(OrderPayState.NOT_PAY);
            orderService.update(order);
        }
        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/updateOrderPayMode")
    public void updateOrderPayMode(String orderId, Integer payMode, HttpServletRequest request, HttpServletResponse response){
        Order order = orderService.selectById(orderId);
        order.setPayMode(payMode);
        orderService.update(order);

        OrderPaymentItem item = new OrderPaymentItem();
        item.setId(ApplicationUtils.randomUUID());
        item.setOrderId(orderId);
        if(payMode == OrderPayMode.XJ_PAY){
            item.setPaymentModeId(PayMode.CRASH_PAY);
        }else if(payMode == OrderPayMode.YL_PAY){
            item.setPaymentModeId(PayMode.BANK_CART_PAY);
        }
        item.setPayTime(order.getCreateTime());
        item.setPayValue(order.getPaymentAmount());
        if(payMode == OrderPayMode.XJ_PAY){
            item.setRemark("现金支付:" + item.getPayValue());
        }else if(payMode == OrderPayMode.YL_PAY){
            item.setRemark("银联支付:" + item.getPayValue());
        }
        orderPaymentItemService.insertByBeforePay(item);
        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/customerByOrderForMyPage")
    public void customerByOrderForMyPage(String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        Order order = orderService.customerByOrderForMyPage(getCurrentCustomerId(), getCurrentShopId());
        json.put("statusCode", "0");
        json.put("message", "");
        json.put("success", true);
        if (order != null) {
            Map<String, String> param = new HashMap<>();
            param.put("orderId", order.getId());
            order.setOrderItems(orderItemService.listByOrderId(param));
            json.put("data", new net.sf.json.JSONObject().fromObject(order));
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置

    }

    @RequestMapping("/aliPayCheck")
    public void aliPayCheck(String orderId, BigDecimal money, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        Order order = orderService.selectById(orderId);
        if(order.getOrderState() != OrderState.SUBMIT){
            json.put("success", false);
            json.put("message", "订单已经支付或者取消");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
        if(order.getAmountWithChildren().doubleValue() > 0){
            if(order.getAmountWithChildren().compareTo(money) == 0){
                json.put("success", true);
                ApiUtils.setBackInfo(response, json); // 返回信息设置
            }else{
                json.put("success", false);
                json.put("message", "订单金额发送变动，请重新扫码");
                ApiUtils.setBackInfo(response, json); // 返回信息设置
            }
        }else{
            if(order.getOrderMoney().compareTo(money) == 0){
                json.put("success", true);
                ApiUtils.setBackInfo(response, json); // 返回信息设置
            }else{
                json.put("success", false);
                json.put("message", "订单金额发送变动，请重新扫码");
                ApiUtils.setBackInfo(response, json); // 返回信息设置
            }
        }
    }
}
