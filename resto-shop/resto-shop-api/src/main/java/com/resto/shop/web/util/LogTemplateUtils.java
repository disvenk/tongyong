package com.resto.shop.web.util;

import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.model.Brand;
import com.resto.shop.web.constant.OrderItemType;
import com.resto.api.appraise.entity.Appraise;
import com.resto.shop.web.model.*;
import com.resto.api.customer.entity.Customer;
import com.resto.api.article.entity.Article;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 日志模板
 */
public class LogTemplateUtils {

    //日志类型
    public static final String POSTYPE = "posAction";
    public static final String USERTYPE="UserAction";
    public static final String ORDERTYPE="orderAction";

    public static final String DAYSMSTYPE="daySmsAction";

    public  static  final String THIRDTYPE = "thirdAction";

    public static final String SHOPTYPE="shopAction";      //记录shop端操作记录

    //模板map
    public static  Map getOrderBaseMap(String brandName,String id,String logType){
        //注:如果是id是customerId 则logType传userAction 如果是orderId则传orderActoin 如果是shopDetailId则传posAction
        //id是telephone传daySmsAction
        Map map = new HashMap(4);
        map.put("brandName",brandName);
        map.put("fileName",id);
        map.put("type",logType);
        return  map;
    }

    //--------------------第一种 记录orderAction begin
    //createOrder创建订单时记录 记录订单项的数据 在orderserviceImpl中调用
    public static void getOrderItemLogByOrderType(String brandName, String orderId, List<OrderItem> orderItems){
        //yz 2017-03-27 orderAction 增加订单菜品项
        Map map = getOrderBaseMap(brandName,orderId,ORDERTYPE);
        StringBuilder sb = new StringBuilder();
        if(!orderItems.isEmpty()){
            for(OrderItem item:orderItems){
                sb.append("菜品id:"+item.getArticleId());
                sb.append("菜品名称:"+item.getArticleName());
                sb.append("原价:"+item.getOriginalPrice());
                sb.append("单价:"+item.getPrice());
                sb.append("最终计算的价格:"+item.getFinalPrice());
                sb.append("餐品数量"+item.getCount());
                sb.append("折扣"+item.getDiscount());
                sb.append("餐盒数量:"+item.getMealFeeNumber());
                sb.append("餐品类型:"+OrderItemType.getPayModeName(item.getType()));
            }
        }
        map.put("content", "订单:" + orderId + "订单菜品项为:"+sb.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //记录订单支付项 2017-03-27
    //支付项---等位红包支付
    public  static  void getWaitMoneyLogByOrderType(String brandName, String orderId, BigDecimal payValue){
        Map waitPayMap=getOrderBaseMap(brandName,orderId,ORDERTYPE);
        waitPayMap.put("content", "订单:"+orderId+"使用等位红包支付了：" + payValue +",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(waitPayMap);
    }

    //支付项---优惠券
    public static void getCouponByOrderType(String brandName, String id, BigDecimal payValue) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        map.put("content", "订单:"+id+"订单使用优惠券支付了：" + payValue +",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc( map);
    }

    //支付项---红包(余额)支付 在AccountServeImpl中payOrder
    public static void getAccountByOrderType(String brandName, String id, BigDecimal payValue) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        map.put("content", "订单:" + id + "订单使用余额(红包)支付了:"+payValue+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //支付项---银联
    public static void getBankByOrderType(String brandName, String id, BigDecimal payValue) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        map.put("content", "订单:" + id + "订单使用银行卡支付了:"+payValue+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //现金支付
    public static void getMoneyByOrderType(String brandName, String id, BigDecimal payValue) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        map.put("content", "订单:" + id + "订单使用现金卡支付了:"+payValue+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //充值余额支付
    public static void getChargeByOrderType(String brandName, BigDecimal payValue, String id) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        map.put("content", "订单:" + id + "订单使用了充值余额支付了:"+payValue+",请求服务器地址为:" + MQSetting.getLocalIP());
    }

    //充值赠送余额支付
    public static void getChargeRewardByOrderType(String brandName, BigDecimal payValue, String id) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        map.put("content", "订单:" + id + "订单使用了充值赠送余额支付了:"+payValue+",请求服务器地址为:" + MQSetting.getLocalIP());
    }



    public static void getConfirmByOrderType(String brandName, Order order, Integer orginState,String content) {
        Map map=getOrderBaseMap(brandName,order.getId(),ORDERTYPE);
        map.put("content", "订单:" + order.getId() + "订单被自动确认,开始状态:"+orginState+"修改后状态:"+order.getOrderState()+"确认走的方法为:"+content+",请求服务器地址为:" + MQSetting.getLocalIP());
    }


    //父订单打印
    public static void getParentOrderPrintSuccessByOrderType(String brandName, String id,Integer status) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        if(1==status||0==status){
            map.put("content", "订单:" + id + "打印成功，订单为主订单，允许加菜---是消费清单"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }else {
            map.put("content", "订单:" + id + "打印成功，订单为主订单，允许加菜---是结账单"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }
        doPostAnsc( map);
    }

    //子订单打印
    public static void getChildOrderPrintSuccessByOrderType(String brandName, String id,Integer status) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        if(1==status||0==status){
            map.put("content", "订单:" + id + "打印成功，订单为子订单---是消费清单"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }else {
            map.put("content", "订单:" + id + "打印成功，订单为子订单---是结账清单"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }

        doPostAnsc( map);
    }

    /**
     * 记录自动取消订单
     * @param brandName
     * @param id
     * @param auto
     */
    public static void getAutoCancleOrderByOrderType(String brandName, String id, Boolean auto) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        if(null!=auto&&auto){
            map.put("content", "订单:" + id + "取消订单成功--是2小时自动取消订单"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }else {
            map.put("content", "订单:" + id + "取消订单成功--是用户自己取消未支付"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }
        doPostAnsc(map);
    }

    /**
     * 记录退菜
     * @param brandName
     * @param id
     * @param orderItems
     */
    public  static void getBackArticleByOrderType(String brandName,String id,List<OrderItem> orderItems){
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        StringBuilder sb = new StringBuilder();
        BigDecimal temp = BigDecimal.ZERO;
        if(!orderItems.isEmpty()){
            for(OrderItem oi:orderItems){
                if(oi.getRefundCount()>0){
                    sb.append("退菜id:"+oi.getArticleId()+"退菜名字:"+oi.getArticleName()+"单价:"+oi.getUnitPrice()+"退菜数量:"+oi.getRefundCount()+"退菜金额:"+oi.getUnitPrice().multiply(new BigDecimal(oi.getRefundCount())));
                    temp=temp.add(oi.getUnitPrice().multiply(new BigDecimal(oi.getRefundCount())));
                }
            }
            sb.append("退菜总额为:"+temp);
        }
        map.put("content", "订单:" + id + "在pos端执行退菜:"+sb.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    /**
     * 记录拒绝订单
     * @param brandName
     * @param orderId
     */
    public static void cancelOrderByOrderType(String brandName, String orderId) {
        Map map=getOrderBaseMap(brandName,orderId,ORDERTYPE);
        map.put("content", "订单:" +orderId + "在pos端被拒绝,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    /**
     * 记录订单被叫号
     * @param brandName
     * @param id
     */
    public static void getCallNumber(String brandName, String id) {
        Map map=getOrderBaseMap(brandName,id,ORDERTYPE);
        map.put("content", "订单:" +id + "订单被叫号,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    /**
     * 记录评论
     * @param brandName
     * @param appraise
     * @param content
     */
    public  static  void  getAppraiseByOrderType(String brandName,Appraise appraise,String content){
        Map map=getOrderBaseMap(brandName,appraise.getOrderId(),ORDERTYPE);
        StringBuilder sb = new StringBuilder();
        sb.append("评论的菜品为:"+appraise.getFeedback()+"评论等级:"+appraise.getLevel()+"★"+"评论返还红包"+appraise.getRedMoney()+"评论内容:"+content);
        map.put("content", "订单:" +appraise.getOrderId() + sb.toString() +"服务器地址"+ MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    /**
     * 记录 pos手动确认
     * @param brandName
     * @param
     * @param originState
     */
    public static void getConfirmOrderPosByOrderType(String brandName, Order o, Integer originState) {
        Map map=getOrderBaseMap(brandName,o.getId(),ORDERTYPE);
        map.put("content","订单:"+o.getId()+"确认开始前订单状态:"+originState+"确认之后订单状态:"+o.getOrderState()+"服务器地址:"+MQSetting.getLocalIP());
    }

    //记录orderAction end-------------------------------------------------------------


    //记录userAction begin----------------------------------------------------------
    //支付项---等位红包支付
    public  static  void getWaitMoneyLogByUserType(String brandName, String orderId, BigDecimal payValue,String nickName){
        Map map=getOrderBaseMap(brandName,orderId,USERTYPE);
        map.put("content", "用户:"+nickName+"使用等位红包支付了：" +payValue +"订单Id为:"+orderId+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //优惠券支付
    public static void getCouponByUserType(String brandName, String id, String nickname, BigDecimal payValue) {
        // CustomerCouponPaymap.put("content", "用户:"+customer.getNickname()+"使用优惠券支付了：" + item.getPayValue() +"订单Id为:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
        Map map = getOrderBaseMap(brandName,id,USERTYPE);
        map.put("content", "用户:"+nickname+"使用优惠券支付了：" +payValue +"订单Id为:"+id+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //余额(红包)支付
    public static void getAccountByUserType(String brandName, String id, String nickname, BigDecimal payValue) {
        Map map=getOrderBaseMap(brandName,id,USERTYPE);
        map.put("content", "用户:"+nickname+"使用余额(红包)支付了:"+payValue+"订单Id为:"+id+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //银行卡支付
    public static void getBankByUserType(String brandName, String id, String nickname, BigDecimal payValue) {
        Map map=getOrderBaseMap(brandName,id,USERTYPE);
        map.put("content", "用户:"+nickname+"使用银行卡支付了:"+payValue+"订单Id为:"+id+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //现金支付
    public static void getMoneyByUserType(String brandName, String id, String nickname, BigDecimal payValue) {
        Map map=getOrderBaseMap(brandName,id,USERTYPE);
        map.put("content", "用户:"+nickname+"使用现金支付了:"+payValue+"订单Id为:"+id+",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //充值余额支付
    public static void getChargeByUserType(String brandName, Customer customer,BigDecimal payValue) {
        Map map=getOrderBaseMap(brandName,customer.getId(),ORDERTYPE);
        map.put("content", "用户:" + customer.getNickname() + "使用了充值余额支付了:"+payValue+",请求服务器地址为:" + MQSetting.getLocalIP());
    }

    //充值赠送余额支付
    public static void getChargeRewardByUserType(String brandName,Customer customer, BigDecimal payValue) {
        Map map=getOrderBaseMap(brandName,customer.getId(),ORDERTYPE);
        map.put("content", "用户:" + customer.getNickname() + "使用了充值赠送余额支付了:"+payValue+",请求服务器地址为:" + MQSetting.getLocalIP());
    }

    /**
     * 添加购物车
     */
    public  static  void getUpdateShopcart(String brandName,Customer customer,String shopName,Article article,ShopCart shopCart){
        Map map=getOrderBaseMap(brandName,customer.getId(),USERTYPE);
        StringBuilder sb = new StringBuilder();
        String articleTypeName=OrderItemType.getPayModeName(article.getArticleType());
        sb.append("用户:"+customer.getNickname())
                .append("将菜品id:"+article.getId())
                .append("菜品类型为:")
                .append(articleTypeName).append("菜品名字为:")
                .append(article.getName()).append("x"+shopCart.getNumber()).append("的菜品加入到购物车").append("加入的店铺为:"+shopName);
        map.put("content",sb.toString()+"请求服务器地址为:"+MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    public  static  void getUpdateShopcartTwo(String brandName,Customer customer,String shopName,Integer type,String articelId,String articleName){
        Map map=getOrderBaseMap(brandName,customer.getId(),USERTYPE);
        StringBuilder sb = new StringBuilder();
        sb.append("用户:"+customer.getNickname())
                .append("将菜品id:"+articelId)
                .append("将菜品类型为:").append(OrderItemType.getPayModeName(type))
                .append("菜品名字为:").append(articleName).append("的菜品加入到购物车").append("加入的店铺为:"+shopName);
        map.put("content",sb.toString()+"请求服务器地址为:"+MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    /**
     * 记录评论
     */
    public  static  void  getAppraiseByUserType(String brandName,Appraise appraise,String content,Customer customer){
        Map map=getOrderBaseMap(brandName,appraise.getCustomerId(),USERTYPE);
        StringBuilder sb = new StringBuilder();
        sb.append("评论的菜品为:"+appraise.getFeedback()+"评论等级:"+appraise.getLevel()+"★"+"评论返还红包"+appraise.getRedMoney()+"评论内容:"+content);
        map.put("content", "用户:" +customer.getNickname() + sb.toString() +"服务器地址"+ MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    public static void getRefundWechatByUserType(Order order, Brand brand, String name) {
        Map map=getOrderBaseMap(brand.getBrandName(),order.getCustomerId(),USERTYPE);
        map.put("content", "用户:" +order.getCustomer().getNickname() +"用户在微信支付上点了×" +"服务器地址"+ MQSetting.getLocalIP());

    }
    //记录userActin end---------------------------------------------------------


    //记录posAction begin--------------------------------------------------------
    public static void getParentOrderPrintSuccessByPOSType(String brandName, String id,Integer status) {
        Map map=getOrderBaseMap(brandName,id,POSTYPE);
        if(1==status||0==status){
            map.put("content", "订单:" + id + "打印成功，订单为主订单，允许加菜---是消费清单"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }else {
            map.put("content", "订单:" + id + "打印成功，订单为主订单，允许加菜---是结账单"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }
    }

    public static void getChildOrderPrintSuccessByPOSType(String brandName, String id,Integer status) {
        Map map=getOrderBaseMap(brandName,id,POSTYPE);
        if(1==status||0==status){
            map.put("content", "订单:" + id + "打印成功，订单为子订单 --是消费清单"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }else {
            map.put("content", "订单:" + id + "打印成功，订单为子订单---是结账清单"+",请求服务器地址为:" + MQSetting.getLocalIP());
        }
        doPostAnsc(map);
    }

    public static void cancleOrderByPosType(String brandName, String name,String orderId) {
        Map map=getOrderBaseMap(brandName,name,POSTYPE);
        map.put("content", "店铺:"+name+"在pos端执行拒绝订单:" + orderId + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }


    //记录 posAction end---------------------------------------------------------

    //记录每日短信
    public static void  dayMessageSms(String brandName,String shopName,String telephone,String result){
        Map map=getOrderBaseMap(brandName,telephone,DAYSMSTYPE);
        map.put("content","发送日结短信短信推送店铺为:"+shopName+"发送短信返回的内容为:"+result+"请求服务器地址:"+MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    //记录 shopAction  start ---------------------------------------
    public static void  shopDeatilEdit(String brandName,String shopName,String result){
        Map map=getOrderBaseMap(brandName,shopName,SHOPTYPE);
        map.put("content","当前登录账号为"+result+"，修改了" + shopName + "店铺设置信息。请求服务器地址:"+MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    public static void  brandSettingEdit(String brandName,String shopName,String result){
        Map map=getOrderBaseMap(brandName, shopName,SHOPTYPE);
        map.put("content","当前登录账号为"+result+"，修改了" + shopName + "品牌参数设置信息。请求服务器地址:"+MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    public static void  articleEdit(String brandName,String shopName,String result){
        Map map=getOrderBaseMap(brandName, shopName,SHOPTYPE);
        map.put("content","当前登录账号为"+result+"，修改了" + shopName + "店铺下菜品设置。请求服务器地址:"+MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    public static void  shopUserLogin(String brandName,String shopName,String result){
        Map map=getOrderBaseMap(brandName, shopName,SHOPTYPE);
        map.put("content","当前登录账号为"+result+"，登录了" + shopName + "店铺。请求服务器地址:"+MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    public static void  shopUserLogout(String brandName,String shopName,String result){
        Map map=getOrderBaseMap(brandName, shopName,SHOPTYPE);
        map.put("content","当前登录账号为"+result+"，退出了" + shopName + "店铺。请求服务器地址:"+MQSetting.getLocalIP());
        doPostAnsc(map);
    }
}
