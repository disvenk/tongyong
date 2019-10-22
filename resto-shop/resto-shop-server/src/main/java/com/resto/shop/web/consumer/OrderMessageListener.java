package com.resto.shop.web.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.core.util.SMSUtils;
import com.resto.api.appraise.entity.Appraise;
import com.resto.brand.core.util.*;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.OrderState;
import com.resto.shop.web.datasource.DataSourceContextHolder;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.LogTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@Component
public class OrderMessageListener implements MessageListener {
    Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    OrderService orderService;
    @Resource
    WechatConfigService wechatConfigService;
    @Resource
    BrandSettingService brandSettingService;
    @Resource
    BrandService brandService;
    @Resource
    CustomerService customerService;
    @Resource
    CouponService couponService;
    @Resource
    ShareSettingService shareSettingService;
    @Resource
    OrderItemService orderItemService;
    @Resource
    ShopDetailService shopDetailService;
    @Resource
    NewCustomCouponService newcustomcouponService;
    @Resource
	BrandAccountService brandAccountService;
    @Resource
	AccountSettingService accountSettingService;
    @Resource
	AccountNoticeService accountNoticeService;
    @Resource
    TemplateService templateService;
    @Autowired
    TableGroupService tableGroupService;
    @Resource
    AccountService accountService;
    @Resource
    LogBaseService logBaseService;
    @Resource
    RedPacketService redPacketService;
    @Resource
    ShopCartService shopCartService;
    @Autowired
    WeChatService weChatService;

    @Value("#{propertyConfigurer['orderMsg']}")
    public static String orderMsg;
    @Override
    public Action consume(Message message, ConsumeContext context) {
        Logger log = LoggerFactory.getLogger(getClass());

        log.info("接收到队列消息:" + message.getTag() + "@" + message);
        try {
            return executeMessage(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("字符编码转换错误:" + e.getMessage());
        }
        return Action.ReconsumeLater;
    }

    public Action executeMessage(Message message) throws UnsupportedEncodingException {
        String tag = message.getTag();
        if (tag.equals(MQSetting.TAG_CANCEL_ORDER)) { //取消订单消息
            return executeCancelOrder(message);
        } else if (tag.equals(MQSetting.TAG_AUTO_CONFIRM_ORDER)) {
            return executeAutoConfirmOrder(message);
        } else if (tag.equals(MQSetting.TAG_NOT_PRINT_ORDER)) {
            return executeChangeProductionState(message);
        } else if (tag.equals(MQSetting.TAG_NOT_ALLOW_CONTINUE)) {
            return executeNotAllowContinue(message);
        } else if (tag.equals(MQSetting.TAG_SHOW_ORDER)) {
            return executeShowComment(message);
        } else if (tag.equals(MQSetting.TAG_SHARE_GIVE_MONEY)){
            return executeShareGiveMoneyDelay(message);
        } else if (tag.equals(MQSetting.TAG_AUTO_REFUND_ORDER)) {
            return executeAutoRefundOrder(message);
        } else if (tag.equals(MQSetting.TAG_NOTICE_SHARE_CUSTOMER)) {
            return executeNoticeShareCustomer(message);
        } else if (tag.equals(MQSetting.SEND_CALL_MESSAGE)){
            return executeSendCallMessage(message);
        }else if (tag.equals(MQSetting.TAG_REMIND_MSG)){
        	return executeRemindMsg(message);
        }else if (tag.equals(MQSetting.TAG_AUTO_SEND_REMMEND)){
        	return executeRecommendMsg(message);
        }else if(tag.equals(MQSetting.TAG_BOSS_ORDER)){
            return executeBossOrderMsg(message);
        }else if(tag.equals(MQSetting.TAG_BRAND_ACCOUNT_SEND)){
        	return excuteBrandAccountMsg(message);
		}else if (tag.equals(MQSetting.TAG_REMOVE_TABLE_GROUP)){
            return excuteRemoveGroup(message);
        }
        return Action.ReconsumeLater;
    }

    private Action excuteRemoveGroup(Message message){
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            JSONObject obj = JSONObject.parseObject(msg);
            String brandId = obj.getString("brandId");
            DataSourceContextHolder.setDataSourceName(brandId);
            tableGroupService.removeTableGroup(obj.getString("groupId"));
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }
        return Action.CommitMessage;
    }

	private Action excuteBrandAccountMsg(Message message) {

    	log.info("消费者开始消费品牌账户欠费消息。。");
		try {
			String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
			JSONObject json = JSONObject.parseObject(msg);
			//品牌账户
			BrandAccount brandAccount = brandAccountService.selectByBrandId(json.getString("brandId"));
			//账户提醒设置
			Boolean flag = true;//默认商户已经充钱

			List<AccountNotice> noticeList = accountNoticeService.selectByAccountId(brandAccount.getId());
			if(!noticeList.isEmpty()){
				for(AccountNotice accountNotice:noticeList){
					if(brandAccount.getAccountBalance().compareTo(accountNotice.getNoticePrice())<0){// 账户只要小于设置就代表商户没充或者没有冲够钱 更改账户设置为 未发送短信
						flag = false;
						//更新设置
						log.info("账户没有充值或者充值--把账户设置已发送欠费短信改为未发送欠费短信");
						BrandSetting brandSetting = brandSettingService.selectByBrandId(json.getString("brandId"));
						AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
						Long id = accountSetting.getId();
						AccountSetting ast = new AccountSetting();
						ast.setType(0);
						ast.setId(id);
						accountSettingService.update(ast);
						break;
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return Action.ReconsumeLater;
		}
		return Action.CommitMessage;
	}


	private Action executeNoticeShareCustomer(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            Customer customer = JSONObject.parseObject(msg, Customer.class);
            DataSourceContextHolder.setDataSourceName(customer.getBrandId());
            noticeShareCustomer(customer);
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }

        return Action.CommitMessage;
    }
    
    private Action executeRemindMsg(Message message) throws UnsupportedEncodingException {
    	//就餐提醒的消息队列
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            Order order = JSONObject.parseObject(msg, Order.class);
            DataSourceContextHolder.setDataSourceName(order.getBrandId());
            Customer customer = customerService.selectById(order.getCustomerId());
            WechatConfig config = wechatConfigService.selectByBrandId(order.getBrandId());
            ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
            weChatService.sendCustomerMsgASync(shop.getPushContext(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }
        return Action.CommitMessage;
    }
    
    
    //优惠券过期提前推送消息队列

	/**
	 * 方法已过期 wtl
	 * @param message
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    private Action executeRecommendMsg(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            JSONObject obj = JSONObject.parseObject(msg);
            Customer customer = customerService.selectById(obj.getString("id"));
            WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            BrandSetting setting = brandSettingService.selectByBrandId(customer.getBrandId());
            String pr = obj.getString("pr");
            String shopName = obj.getString("shopName");
            String name = obj.getString("name");
            String pushDay = obj.getInteger("pushDay")+"";
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            StringBuffer str=new StringBuffer();
            String jumpurl = setting.getWechatWelcomeUrl()+"?subpage=tangshi";
            str.append("优惠券到期提醒"+"\n");
            str.append("<a href='"+jumpurl+"'>"+shopName+"温馨提醒您：您价值"+pr+"元的\""+name+"\""+pushDay+"天后即将到期，别浪费啊~</a>");
            if(setting.getTemplateEdition()==0){
                weChatService.sendCustomerMsg(str.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());//提交推送
            }else{
                List<TemplateFlow> templateFlowList=templateService.selectTemplateId(config.getAppid(),"TM00710");
                if(templateFlowList!=null&&templateFlowList.size()!=0){
                    String templateId = templateFlowList.get(0).getTemplateId();
                    String jumpUrl ="";
                    Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                    Map<String, Object> first = new HashMap<String, Object>();
                    first.put("value", str.toString());
                    first.put("color", "#00DB00");
                    Map<String, Object> time = new HashMap<String, Object>();
                    time.put("value", pushDay);
                    time.put("color", "#000000");
                    Map<String, Object> number = new HashMap<String, Object>();
                    number.put("value", "-");
                    number.put("color", "#000000");
                    Map<String, Object> remark = new HashMap<String, Object>();
                    remark.put("value", "快来尝尝我们的新菜吧~");
                    remark.put("color", "#173177");
                    content.put("first", first);
                    content.put("time", time);
                    content.put("number", number);
                    content.put("remark", remark);
                    String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                    Map map = new HashMap(4);
                    map.put("brandName", setting.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                    map.put("content","用户:"+customer.getNickname()+"优惠券过期发短信提醒"+"请求地址:"+MQSetting.getLocalIP());

                    //发送短信
                    if(setting.getMessageSwitch()==1){
                        com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                        smsParam.put("shop",shopName);
                        smsParam.put("price", pr);
                        smsParam.put("name",name);
                        smsParam.put("day",pushDay);
                        com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_43790004");
                    }

                }else{
                    Map map = new HashMap(4);
                    map.put("brandName", setting.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
            if(setting.getIsSendCouponMsg() == Common.YES){
                sendNote(shopName,pr,name,pushDay,customer.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }


        return Action.CommitMessage;
    }
    
  //发送短信
    private void sendNote(String shop,String price,String name,String pushDay,String customerId){
        Customer customer=customerService.selectById(customerId);
    	Map param = new HashMap();
        param.put("shop", shop);
		param.put("price", price);
		param.put("name", name);
		param.put("day", pushDay);
        JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), new JSONObject(param), "餐加", "SMS_43790004");


    }

    //
    private Action executeBossOrderMsg(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            Order order = JSON.parseObject(msg, Order.class);
            DataSourceContextHolder.setDataSourceName(order.getBrandId());
            log.info("执行自动确认逻辑" + order.getId());
            orderService.confirmBossOrder(order);
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }

        return Action.CommitMessage;
    }

    private void noticeShareCustomer(Customer customer) {
        Customer shareCustomer = customerService.selectById(customer.getShareCustomer());
        ShareSetting shareSetting = shareSettingService.selectValidSettingByBrandId(customer.getBrandId());
        if (shareCustomer != null && shareSetting != null) {
            BigDecimal sum = new BigDecimal(0);
            List<Coupon> couponList = new ArrayList<>();
            //品牌专属优惠券
            List<Coupon> couponList1 = couponService.listCouponByStatus("0", customer.getId(),customer.getBrandId(),null);
            couponList.addAll(couponList1);
            List<ShopDetail> listShop = shopDetailService.selectByBrandId(customer.getBrandId());
            for(ShopDetail s : listShop){
                //店铺专属优惠券
                List<Coupon> couponList2 = couponService.listCouponByStatus("0", customer.getId(),null,s.getId());
                couponList.addAll(couponList2);
            }
            for (Coupon coupon : couponList) {
                sum = sum.add(coupon.getValue());
            }
            StringBuffer msg = new StringBuffer("亲，感谢您的分享，您的好友");
            msg.append(customer.getNickname()).append("已领取").append(sum).append("元红包，")
                    .append(customer.getNickname()).append("如到店消费您将获得").append(shareSetting.getMinMoney())
                    .append("-").append(shareSetting.getMaxMoney()).append("元红包返利");
            WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            log.info("异步发送分享注册微信通知ID:" + customer.getShareCustomer() + " 内容:" + msg);
            weChatService.sendCustomerMsgASync(msg.toString(), shareCustomer.getWechatId(), config.getAppid(), config.getAppsecret());
        }

    }

    private Action executeAutoRefundOrder(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            JSONObject obj = JSONObject.parseObject(msg);
            String brandId = obj.getString("brandId");
            DataSourceContextHolder.setDataSourceName(brandId);
            Order order = orderService.selectById(obj.getString("orderId"));
            ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
            Brand brand = brandService.selectById(order.getBrandId());
            String customerId = obj.getString("customerId");
            if (orderService.checkRefundLimit(order)) {
                orderService.autoRefundOrder(obj.getString("orderId"));
                log.info("款项自动退还到相应账户:" + obj.getString("orderId"));
                Customer customer = customerService.selectById(customerId);
                WechatConfig config = wechatConfigService.selectByBrandId(brandId);
                StringBuilder sb = new StringBuilder("亲,昨日未消费订单已退款,欢迎下次再来本店消费\n");
                sb.append("订单编号:"+order.getSerialNumber()+"\n");
                if(order.getOrderMode()!=null){
                    switch (order.getOrderMode()) {
                        case ShopMode.TABLE_MODE:
                            sb.append("桌号:"+(order.getTableNumber()!=null?order.getTableNumber():"无")+"\n");
                            break;
                        default:
                            sb.append("消费码："+(order.getVerCode()!=null?order.getVerCode():"无")+"\n");
                            break;
                    }
                }
                if( order.getShopName()==null||"".equals(order.getShopName())){
                    order.setShopName(shopDetailService.selectById(order.getShopDetailId()).getName());
                }
                sb.append("店铺名："+order.getShopName()+"\n");
                sb.append("订单时间："+ DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm")+"\n");
                sb.append("订单明细：\n");
                Map<String, String> param = new HashMap<>();
                param.put("orderId", order.getId());
                List<OrderItem> orderItem  = orderItemService.listByOrderId(param);
                for(OrderItem item : orderItem){
                    sb.append("  "+item.getArticleName()+"x"+item.getCount()+"\n");
                }
                sb.append("订单金额："+order.getOrderMoney()+"\n");
                weChatService.sendCustomerMsgASync(sb.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            } else {
                log.info("款项自动退还到相应账户失败，订单状态不是已付款或商品状态不是已付款未下单");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }

        return Action.CommitMessage;
    }

    private Action executeSendCallMessage(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            JSONObject obj = JSONObject.parseObject(msg);
            String brandId = obj.getString("brandId");
            DataSourceContextHolder.setDataSourceName(brandId);
            String customerId = obj.getString("customerId");
            Customer customer = customerService.selectById(customerId);
            WechatConfig config = wechatConfigService.selectByBrandId(brandId);
            Order order=orderService.selectById(obj.getString("orderId"));
            ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
            BrandSetting setting = brandSettingService.selectByBrandId(brandId);
            if(setting.getTemplateEdition()==0){
                weChatService.sendCustomerMsgASync("您的餐品已经准备好了，请尽快到吧台取餐！", customer.getWechatId(), config.getAppid(), config.getAppsecret());
            }else{
                List<TemplateFlow> templateFlowList=templateService.selectTemplateId(config.getAppid(),"OPENTM411223846");
                if(templateFlowList!=null&&templateFlowList.size()!=0){
                    String templateId = templateFlowList.get(0).getTemplateId();
                    String jumpUrl ="";
                    Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                    Map<String, Object> first = new HashMap<String, Object>();
                    first.put("value", "您好，餐点已备齐，请取餐。");
                    first.put("color", "#00DB00");
                    Map<String, Object> keyword1 = new HashMap<String, Object>();
                    keyword1.put("value", shop.getName());
                    keyword1.put("color", "#000000");
                    Map<String, Object> keyword2 = new HashMap<String, Object>();
                    keyword2.put("value", order.getVerCode());
                    keyword2.put("color", "#000000");
                    Map<String, Object> keyword3 = new HashMap<String, Object>();
                    keyword3.put("value", order.getSerialNumber());
                    keyword3.put("color", "#000000");
                    Map<String, Object> remark = new HashMap<String, Object>();
                    remark.put("value", "为了保证出品新鲜美味，请您请尽快到吧台取餐！");
                    remark.put("color", "#173177");
                    content.put("first", first);
                    content.put("keyword1", keyword1);
                    content.put("keyword2", keyword2);
                    content.put("keyword3", keyword3);
                    content.put("remark", remark);
                    String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                    Brand brand = brandService.selectById(brandId);
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                    //发送短信
                    if(setting.getMessageSwitch()==1){
                        com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                        smsParam.put("key1",shop.getName());
                        smsParam.put("key2", order.getVerCode());
                        smsParam.put("key3",order.getSerialNumber());
                        com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105785023");
                    }
                }else{
                    Brand brand = brandService.selectById(order.getBrandId());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }

        return Action.CommitMessage;
    }


    private Action executeNotAllowContinue(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            Order order = JSON.parseObject(msg, Order.class);
//            Brand brand = brandService.selectById(order.getBrandId());
//            ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
            DataSourceContextHolder.setDataSourceName(order.getBrandId());
            orderService.updateAllowContinue(order.getId(), false);
            if(!StringUtils.isEmpty(order.getGroupId())){
                //如果订单是在组里的
                //禁止加菜后，组释放，并且删除所有 人与组的关系，并且删除该组的购物车
                TableGroup tableGroup = tableGroupService.selectByGroupId(order.getGroupId());
                tableGroup.setState(TableGroup.FINISH);
                tableGroupService.update(tableGroup);
//                customerGroupService.removeByGroupId(order.getGroupId());
                shopCartService.resetGroupId(order.getGroupId());
            }
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }

        return Action.CommitMessage;
    }

    private Action executeShowComment(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            Appraise appraise = JSON.parseObject(msg, Appraise.class);
            DataSourceContextHolder.setDataSourceName(appraise.getBrandId());
            log.info("开始发送分享通知:");
            sendShareMsg(appraise);
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }
        return Action.CommitMessage;
    }

    private void sendShareMsg(Appraise appraise) {
        StringBuffer msg = new StringBuffer("感谢您的评论，将");
        BrandSetting setting = brandSettingService.selectByBrandId(appraise.getBrandId());
        WechatConfig config = wechatConfigService.selectByBrandId(appraise.getBrandId());
        Customer customer = customerService.selectById(appraise.getCustomerId());
        ShareSetting shareSetting = shareSettingService.selectByBrandId(customer.getBrandId());
        log.info("分享人:" + customer.getNickname());
        List<NewCustomCoupon> coupons = newcustomcouponService.selectListByCouponType(customer.getBrandId(), 1, appraise.getShopDetailId());
        BigDecimal money = new BigDecimal("0.00");
        for(NewCustomCoupon coupon : coupons){
            money = money.add(coupon.getCouponValue().multiply(new BigDecimal(coupon.getCouponNumber())));
        }
        if(money.doubleValue() == 0.00 && shareSetting == null){
            msg.append("红包发送给朋友/分享朋友圈，朋友到店消费后，您将获得红包返利\n");
        }else if(money.doubleValue() == 0.00){
            msg.append(money+"元红包发送给朋友/分享朋友圈，朋友到店消费后，您将获得红包返利\n");
        }else if(shareSetting == null){
            msg.append("红包发送给朋友/分享朋友圈，朋友到店消费后，您将获得"+shareSetting.getMinMoney()+"元-"+shareSetting.getMaxMoney()+"元红包返利\n");
        }else{
            msg.append(money +"元红包发送给朋友/分享朋友圈，朋友到店消费后，您将获得"+shareSetting.getMinMoney()+"元-"+shareSetting.getMaxMoney()+"元红包返利\n");
        }
        if(setting.getTemplateEdition()==0) {
            msg.append("<a href='" + setting.getWechatWelcomeUrl() + "?shopId=" + customer.getLastOrderShop() + "&subpage=home&dialog=share&appraiseId=" + appraise.getId() + "'>立即分享红包</a>");
        }
        log.info("异步发送分享好评微信通知ID:" + appraise.getId() + " 内容:" + msg);
        log.info("ddddd-"+customer.getWechatId()+"dddd-"+config.getAppid()+"dddd-"+config.getAppsecret());
        ShopDetail shopDetail = shopDetailService.selectById(appraise.getShopDetailId());
        if(setting.getTemplateEdition()==0){
            weChatService.sendCustomerMsgASync(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
            //logBaseService.insertLogBaseInfoState(shopDetail, customer, appraise.getId(), LogBaseState.SHARE);
            log.info("分享完毕:" );
        }else{
            log.info("进入微信模板消息...");
            List<TemplateFlow> templateFlowList=templateService.selectTemplateId(config.getAppid(),"OPENTM207012446");
            if(templateFlowList!=null&&templateFlowList.size()!=0){
                String templateId = templateFlowList.get(0).getTemplateId();
                String jumpUrl =setting.getWechatWelcomeUrl() + "?shopId=" + customer.getLastOrderShop() + "&subpage=home&dialog=share&appraiseId=" + appraise.getId();
                log.info("orderid---------------->"+appraise.getOrderId());
                Order order=orderService.selectById(appraise.getOrderId());
                log.info("订单号："+order.getSerialNumber()+"日期："+DateUtil.formatDate(order.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
                Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                Map<String, Object> first = new HashMap<String, Object>();
                first.put("value", msg.toString());
                first.put("color", "#00DB00");
                Map<String, Object> keyword1 = new HashMap<String, Object>();
                keyword1.put("value", order.getSerialNumber());
                keyword1.put("color", "#000000");
                Map<String, Object> keyword2 = new HashMap<String, Object>();
                keyword2.put("value", DateUtil.formatDate(order.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
                keyword2.put("color", "#000000");
                Map<String, Object> remark = new HashMap<String, Object>();
                remark.put("value", "立即分享红包");
                remark.put("color", "#173177");
                content.put("first", first);
                content.put("keyword1", keyword1);
                content.put("keyword2", keyword2);
                content.put("remark", remark);
                String result1 = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                log.info("result1-------------->"+result1);
                Brand brand = brandService.selectById(order.getBrandId());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                //发送短信
                if(setting.getMessageSwitch()==1){
                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                    smsParam.put("money1", money);
                    smsParam.put("money2",shareSetting.getMinMoney());
                    smsParam.put("money3",shareSetting.getMaxMoney());
                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_105890057");
                }
            }else{
                Order order=orderService.selectById(appraise.getOrderId());
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

    private Action executeShareGiveMoneyDelay(Message message) {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            RedPacket redPacket = JSON.parseObject(msg, RedPacket.class);
            DataSourceContextHolder.setDataSourceName(redPacket.getBrandId());
            redPacket = redPacketService.selectById(redPacket.getId());
            if(redPacket.getState() == 0){
                log.info("开始发送分享红包到账通知:");
                Customer customer = customerService.selectById(redPacket.getCustomerId());
                accountService.addAccount(redPacket.getRedMoney(),customer.getAccountId(), " 评论奖励红包:"+redPacket.getRedMoney(),AccountLog.APPRAISE_RED_PACKAGE,redPacket.getShopDetailId());
                RedPacket newRedPacket = new RedPacket();
                newRedPacket.setId(redPacket.getId());
                newRedPacket.setState(1);
                redPacketService.update(newRedPacket);
                sendShareGiveMoneyDelayMsg(redPacket, customer);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }
        return Action.CommitMessage;
    }

    private void sendShareGiveMoneyDelayMsg(RedPacket redPacket , Customer customer) {
        StringBuffer msg = new StringBuffer();
        Brand brand = brandService.selectById(redPacket.getBrandId());
        WechatConfig config = wechatConfigService.selectByBrandId(redPacket.getBrandId());
        msg.append("太好了！评论红包已经送达您的账户！");
        String jumpurl = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?dialog=myYue&subpage=my";
        msg.append("<a href='" + jumpurl + "'>前往查看</a>");
        weChatService.sendCustomerMsgASync(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
    }

    private Action executeChangeProductionState(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            Order order = JSON.parseObject(msg, Order.class);
            DataSourceContextHolder.setDataSourceName(order.getBrandId());
            orderService.changePushOrder(order);
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }

        return Action.CommitMessage;
    }

    private Action executeAutoConfirmOrder(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            Order order = JSON.parseObject(msg, Order.class);
            DataSourceContextHolder.setDataSourceName(order.getBrandId());
            log.info("执行自动确认逻辑" + order.getId());
            if(order.getProductionStatus()==4){
                orderService.confirmWaiMaiOrder(order);
            }else{
                orderService.confirmOrder(order);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }

        return Action.CommitMessage;
    }

    private Action executeCancelOrder(Message message) throws UnsupportedEncodingException {
        try {
            String msg = new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET);
            JSONObject obj = JSONObject.parseObject(msg);
            String brandId = obj.getString("brandId");
            Boolean auto = obj.getBoolean("auto");
            DataSourceContextHolder.setDataSourceName(brandId);
            Order order = orderService.selectById(obj.getString("orderId"));
            ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
            Brand brand = brandService.selectById(order.getBrandId());
            if (order.getOrderState() == OrderState.SUBMIT) {
                log.info("自动取消订单:" + obj.getString("orderId"));
                orderService.cancelOrder(obj.getString("orderId"));
                LogTemplateUtils.getAutoCancleOrderByOrderType(brand.getBrandName(),order.getId(),auto);
                log.info("款项自动退还到相应账户:" + obj.getString("orderId"));
                Customer customer = customerService.selectById(order.getCustomerId());
                WechatConfig config = wechatConfigService.selectByBrandId(brandId);
                StringBuilder sb = new StringBuilder("亲,今日未完成支付的订单已被系统自动取消,欢迎下次再来本店消费\n");
                sb.append("订单编号:"+order.getSerialNumber()+"\n");
                if(order.getOrderMode()!=null){
                    switch (order.getOrderMode()) {
                        case ShopMode.TABLE_MODE:
                            sb.append("桌号:"+(order.getTableNumber()!=null?order.getTableNumber():"无")+"\n");
                            break;
                        default:
                            sb.append("消费码："+(order.getVerCode()!=null?order.getVerCode():"无")+"\n");
                            break;
                    }
                }
                if( order.getShopName()==null||"".equals(order.getShopName())){
                    order.setShopName(shopDetailService.selectById(order.getShopDetailId()).getName());
                }
                sb.append("店铺名："+order.getShopName()+"\n");
                sb.append("订单时间："+ DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm")+"\n");
                sb.append("订单明细：\n");
                Map<String, String> param = new HashMap<>();
                param.put("orderId", order.getId());
                List<OrderItem> orderItem  = orderItemService.listByOrderId(param);
                for(OrderItem item : orderItem){
                    sb.append("  "+item.getArticleName()+"x"+item.getCount()+"\n");
                }
                sb.append("订单金额："+order.getOrderMoney()+"\n");
                weChatService.sendCustomerMsgASync(sb.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            } else {
                log.info("自动取消订单失败，订单状态不是已提交");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Action.ReconsumeLater;
        }

        return Action.CommitMessage;
    }


}
