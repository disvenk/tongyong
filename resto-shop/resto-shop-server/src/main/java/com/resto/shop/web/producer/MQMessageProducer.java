package com.resto.shop.web.producer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.*;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.entity.AppraiseNew;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.service.RedisService;
import com.resto.shop.web.constant.OrderPosStatus;
import com.resto.shop.web.model.*;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;


@Component
public class MQMessageProducer {


	@Autowired
	RedisService redisService;

	final static Logger log = LoggerFactory.getLogger(MQMessageProducer.class);


	private final static  Producer producer;
	static{
		Properties pro = MQSetting.getPropertiesWithAccessSecret();
		pro.setProperty(PropertyKeyConst.ProducerId,MQSetting.PID_SHOP);
		producer = ONSFactory.createProducer(pro);
		producer.start();
	}

	public static void sendAutoCloseMsg(final String orderId, final String brandId,final long delay) {
		JSONObject obj = new JSONObject();
		obj.put("orderId", orderId);
		obj.put("brandId", brandId);
		//是否是自动取消
		obj.put("auto",true);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_CANCEL_ORDER, obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delay);
		sendMessageASync(message);
	}

	public static void sendAutoRefundMsg(final String brandId,final String orderId,final String customerId){
		JSONObject obj = new JSONObject();
		obj.put("brandId", brandId);
		obj.put("orderId", orderId);
		obj.put("customerId",customerId);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_AUTO_REFUND_ORDER, obj.toJSONString().getBytes());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		message.setStartDeliverTime(calendar.getTime().getTime());
		sendMessageASync(message);
	}

	public static void autoSendRemmend(final String brandId,Calendar calendar,final  String customerId,final String pr,final String name,final Integer pushDay,final  String shopName){
		JSONObject obj = new JSONObject();
		obj.put("brandId", brandId);
		obj.put("shopName", shopName);
		obj.put("id",customerId);
		obj.put("pr", pr);
		obj.put("name", name);
		obj.put("pushDay", pushDay);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_AUTO_SEND_REMMEND, obj.toJSONString().getBytes());
		message.setStartDeliverTime(calendar.getTime().getTime());
		sendMessageASync(message);
	}


	public static void sendCallMessage(final String brandId,final String orderId,final String customerId){
		JSONObject obj = new JSONObject();
		obj.put("brandId", brandId);
		obj.put("orderId", orderId);
		obj.put("customerId",customerId);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.SEND_CALL_MESSAGE, obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis());
		sendMessageASync(message);
	}


	public void sendQueueOrder(GetNumber getNumber){
		JSONObject obj = new JSONObject();
		obj.put("id", getNumber.getId());
		obj.put("shopId", getNumber.getShopDetailId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_QUEUE_ORDER, obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis());
		sendMessageASync(message);
	}

	public void sendAutoConfirmOrder(final Order order,final long delayTime) {
		JSONObject obj = new JSONObject();
		obj.put("brandId", order.getBrandId());
		obj.put("id", order.getId());
		obj.put("orderMode",order.getOrderMode());
		obj.put("productionStatus",order.getProductionStatus());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_AUTO_CONFIRM_ORDER,obj.toJSONString().getBytes());
		long delay = System.currentTimeMillis()+delayTime;
		message.setStartDeliverTime(delay);
		sendMessageASync(message);
	}

	public void sendBossOrder(final Order order,final long delayTime) {
		JSONObject obj = new JSONObject();
		obj.put("brandId", order.getBrandId());
		obj.put("id", order.getId());
		obj.put("orderMode",order.getOrderMode());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_BOSS_ORDER,obj.toJSONString().getBytes());
		long delay = System.currentTimeMillis()+delayTime;
		message.setStartDeliverTime(delay);
		sendMessageASync(message);
	}

	public static void sendNotPrintedMessage(final Order order, final long delayTime) {
		JSONObject obj = new JSONObject();
		obj.put("brandId", order.getBrandId());
		obj.put("id", order.getId());
		obj.put("shopDetailId", order.getShopDetailId());
		obj.put("articleCount", order.getArticleCount());
		obj.put("productionStatus", order.getProductionStatus());
		obj.put("verCode", order.getVerCode());
		obj.put("parentOrderId", order.getParentOrderId());

		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_NOT_PRINT_ORDER,obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}

	public void sendShareMsg(final Appraise appraise, final long delayTime){
		JSONObject obj = new JSONObject();
		obj.put("brandId", appraise.getBrandId());
		obj.put("id", appraise.getId());
		obj.put("customerId", appraise.getCustomerId());
		obj.put("shopDetailId", appraise.getShopDetailId());
		obj.put("orderId", appraise.getOrderId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_SHOW_ORDER,obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}

	public static void sendShareMsgNew(final AppraiseNew appraiseNew, final long delayTime){
		JSONObject obj = new JSONObject();
		obj.put("brandId", appraiseNew.getBrandId());
		obj.put("id", appraiseNew.getId());
		obj.put("customerId", appraiseNew.getCustomerId());
		obj.put("shopDetailId", appraiseNew.getShopDetailId());
		obj.put("orderId", appraiseNew.getOrderId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_SHOW_ORDER,obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}

	public static void sendShareGiveMoneyMsg(RedPacket redPacket,long delayTime){
		JSONObject obj = new JSONObject();
		obj.put("id", redPacket.getId());
		obj.put("brandId", redPacket.getBrandId());
		obj.put("customerId", redPacket.getCustomerId());
		obj.put("shopDetailId", redPacket.getShopDetailId());
		obj.put("redMoney", redPacket.getRedMoney());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_SHARE_GIVE_MONEY,obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}

	public static void sendMessageASync(final Message message) {
		new Thread(new Runnable() {
			@Override
			public void run() {
                try {
                    SendResult result = producer.send(message);
                    log.info("["+message.getTag()+"] "+"发送消息成功："+result + "----消息内容：" + new String(message.getBody(), MQSetting.DEFAULT_CHAT_SET));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
		}).start();
	}


	public void sendPlatformOrderMessage(String id,Integer type,String brandId,String shopId) {
		JSONObject obj  = new JSONObject();
		obj.put("id", id);
		obj.put("type", type);
		obj.put("brandId", brandId);
		obj.put("shopId",shopId);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_PLACE_PLATFORM_ORDER,obj.toJSONString().getBytes());
		sendMessageASync(message);
	}

	public void sendPlaceOrderNoPayMessage(Order order){
		JSONObject obj  = new JSONObject();
		obj.put("brandId", order.getBrandId());
		obj.put("id", order.getId());
		obj.put("tableNumber", order.getTableNumber());
		obj.put("shopDetailId", order.getShopDetailId());
		obj.put("articleCount", order.getArticleCount());
		obj.put("orderMode",order.getOrderMode());
		obj.put("orderState",order.getOrderState());
		obj.put("productionStatus", order.getProductionStatus());
		obj.put("verCode", order.getVerCode());
		obj.put("parentOrderId", order.getParentOrderId());
		obj.put("originalAmount", order.getOriginalAmount());
		obj.put("orderMoney", order.getOrderMoney());
		obj.put("totalCount",order.getTotalCount());
		obj.put("serialNumber",order.getSerialNumber());
		obj.put("printTimes",order.getPrintTimes());
		obj.put("amountWithChildren",order.getAmountWithChildren());
		obj.put("printOrderTime",order.getPrintOrderTime());
		obj.put("payMode",order.getPayMode());
		obj.put("payType",order.getPayType());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_PLACE_NOPAY_ORDER,obj.toJSONString().getBytes());
		sendMessageASync(message);
	}

	public void sendPlaceOrderMessage(Order order) {
		JSONObject obj  = new JSONObject();
		obj.put("brandId", order.getBrandId());
		obj.put("id", order.getId());
		obj.put("tableNumber", order.getTableNumber());
		obj.put("shopDetailId", order.getShopDetailId());
		obj.put("articleCount", order.getArticleCount());
		obj.put("orderMode",order.getOrderMode());
		obj.put("productionStatus", order.getProductionStatus());
		obj.put("verCode", order.getVerCode());
		obj.put("parentOrderId", order.getParentOrderId());
		obj.put("originalAmount", order.getOriginalAmount());
		obj.put("orderMoney", order.getOrderMoney());
		obj.put("serialNumber",order.getSerialNumber());
		obj.put("printTimes",order.getPrintTimes());
		obj.put("amountWithChildren",order.getAmountWithChildren());
		obj.put("printOrderTime",order.getPrintOrderTime());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_PLACE_ORDER,obj.toJSONString().getBytes());
//		RedisUtil.set(order.getId()+"status", OrderPosStatus.SEND_MSG_SUCCESS);
//		List<String> orderList = (List<String>) RedisUtil.get(order.getShopDetailId()+"sendMsgList");
		redisService.set(order.getId()+"status", OrderPosStatus.SEND_MSG_SUCCESS);
		List<String> orderList = (List<String>) redisService.get(order.getShopDetailId()+"sendMsgList");
		if(CollectionUtils.isEmpty(orderList)){
			orderList = new ArrayList<>();
		}
		orderList.add(order.getId());
//		RedisUtil.set(order.getShopDetailId()+"sendMsgList",orderList);
		redisService.set(order.getShopDetailId()+"sendMsgList",orderList);
		log.info("test发送下单通知"+order.getId());
		log.debug("------发送指令方法--------订单状态："+order.getOrderState());
		sendMessageASync(message);
	}

	public void sendPlaceOrderMessageAgain(Order order, long delayTime) {
		JSONObject obj  = new JSONObject();
		obj.put("brandId", order.getBrandId());
		obj.put("id", order.getId());
		obj.put("tableNumber", order.getTableNumber());
		obj.put("shopDetailId", order.getShopDetailId());
		obj.put("articleCount", order.getArticleCount());
		obj.put("orderMode",order.getOrderMode());
		obj.put("productionStatus", order.getProductionStatus());
		obj.put("verCode", order.getVerCode());
		obj.put("parentOrderId", order.getParentOrderId());
		obj.put("originalAmount", order.getOriginalAmount());
		obj.put("orderMoney", order.getOrderMoney());
		obj.put("serialNumber",order.getSerialNumber());
		obj.put("printTimes",order.getPrintTimes());
		obj.put("amountWithChildren",order.getAmountWithChildren());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_PLACE_ORDER,obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}

	public void sendRemindMsg(Order order, long delayTime) {
		JSONObject obj  = new JSONObject();
		obj.put("id", order.getId());
		obj.put("customerId", order.getCustomerId());
		obj.put("shopDetailId", order.getShopDetailId());
		obj.put("brandId", order.getBrandId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_REMIND_MSG,obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}



	public void removeTableGroup(TableGroup tableGroup, long delayTime){
		JSONObject obj = new JSONObject();
		obj.put("groupId",tableGroup.getGroupId());
		obj.put("brandId",tableGroup.getBrandId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_REMOVE_TABLE_GROUP,obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}


	public static void checkPlaceOrderMessage(Order order,Long delayTime,Long limitTime) {
		JSONObject obj = new JSONObject();
		obj.put("brandId", order.getBrandId());
		obj.put("id", order.getId());
		obj.put("timeOut",delayTime.equals(limitTime));
		obj.put("orderMode",order.getOrderMode());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_CHECK_ORDER, obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}


	public void sendNoticeOrderMessage(Order order) {
		JSONObject obj  = new JSONObject();
		obj.put("brandId", order.getBrandId());
		obj.put("tableNumber", order.getTableNumber());
		obj.put("id", order.getId());
		obj.put("orderState", order.getOrderState());
		obj.put("shopDetailId", order.getShopDetailId());
		obj.put("articleCount", order.getArticleCount());
		obj.put("productionStatus", order.getProductionStatus());
		obj.put("verCode", order.getVerCode());
		obj.put("parentOrderId", order.getParentOrderId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_NOTICE_ORDER,obj.toJSONString().getBytes());
		sendMessageASync(message);

	}

	public void sendCreateOrderMessage(Order order, Long delayTime){
		JSONObject obj  = new JSONObject();
		obj.put("id", order.getId());
		obj.put("brandId", order.getBrandId());
		obj.put("shopDetailId", order.getShopDetailId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_ORDER_CREATED,obj.toJSONString().getBytes());
		Optional.ofNullable(delayTime).ifPresent(aLong -> {
			//拿到支付回调为保证查询订单的时候订单状态已更新成功，当前队列延迟5秒
			message.setStartDeliverTime(System.currentTimeMillis()+aLong);
		});
		sendMessageASync(message);
	}



	public static void sendNoticeShareMessage(Customer customer){
		JSONObject obj  = new JSONObject();
		obj.put("id", customer.getId());
		obj.put("shareCustomer", customer.getShareCustomer());
		obj.put("brandId", customer.getBrandId());
		obj.put("nickname",customer.getNickname());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_NOTICE_SHARE_CUSTOMER,obj.toJSONString().getBytes());
		sendMessageASync(message);

	}

	public static void main(String[] args) {
//		Order order = new Order();
//		order.setId("00b8a27437cf460c93910bdc2489d061");
//		order.setBrandId("31946c940e194311b117e3fff5327215");
//		order.setShopDetailId("31164cebcc4b422685e8d9a32db12ab8");
//		sendPlatformOrderMessage("3010611616147302622",1,"974b0b1e31dc4b3fb0c3d9a0970d22e4","013e35a74b0e43e1bb79c84d5e4d4d95");
	}

	public void sendNotAllowContinueMessage(Order order, long delay) {
		JSONObject object=  new JSONObject();
		object.put("brandId", order.getBrandId());
		object.put("id", order.getId());
		object.put("groupId", order.getGroupId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_NOT_ALLOW_CONTINUE,object.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delay);
		sendMessageASync(message);
	}


	public void sendShopChangeMessage(ShopMsgChangeDto shopMsgChangeDto) {
		JSONObject object=  new JSONObject();
		object.put("shopMsgChangeDto", shopMsgChangeDto);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_SHOP_CHANGE,object.toJSONString().getBytes());
		sendMessageASync(message);
	}

	public void sendModelFivePaySuccess(Order order) {
		JSONObject obj=  new JSONObject();
		obj.put("brandId", order.getBrandId());
		obj.put("id", order.getId());
		obj.put("tableNumber", order.getTableNumber());
		obj.put("shopDetailId", order.getShopDetailId());
		obj.put("articleCount", order.getArticleCount());
		obj.put("orderMode",order.getOrderMode());
		obj.put("productionStatus", order.getProductionStatus());
		obj.put("verCode", order.getVerCode());
		obj.put("parentOrderId", order.getParentOrderId());
		obj.put("originalAmount", order.getOriginalAmount());
		obj.put("orderMoney", order.getOrderMoney());
		obj.put("serialNumber",order.getSerialNumber());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_DELETE_ORDER,obj.toJSONString().getBytes());
		sendMessageASync(message);
	}

	public void sendPrintSuccess(String shopId) {
		JSONObject obj  = new JSONObject();
		obj.put("shopId",shopId);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_PRINT_SUCCESS,obj.toJSONString().getBytes());
		sendMessageASync(message);
	}

	/**
	 * 发送发票管理消息队列
	 * @param shopId orderNumber
	 */
	public void sendReceiptPrintSuccess(String shopId,String orderNumber) {
		JSONObject obj  = new JSONObject();
        obj.put("shopId",shopId);
        obj.put("orderNumber",orderNumber);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_RECEIPT_PRINT_SUCCESS,obj.toJSONString().getBytes());
//		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,"",obj.toJSONString().getBytes());
		sendMessageASync(message);
	}

	/**
	 * 发送打印差评订单的消息队列
	 * @param orderId
	 */
	public void sendBadAppraisePrintOrderMessage(String orderId, String shopId){
		JSONObject obj  = new JSONObject();
		obj.put("orderId",orderId);
		obj.put("shopId",shopId);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_BAD_APPRAISE_PRINT_ORDER,obj.toJSONString().getBytes());
		sendMessageASync(message);
	}

	/**
	 * 品牌账户发送消息延时任务(24小时)
	 */
	public void sendBrandAccountSms(String brandId,long delayTime){
		JSONObject obj = new JSONObject();
		obj.put("brandId",brandId);
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_BRAND_ACCOUNT_SEND,obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}




	public static void sendOrderPay(Order order, long delayTime) {
		JSONObject obj  = new JSONObject();
		obj.put("id", order.getId());
		obj.put("brandId", order.getBrandId());
		obj.put("payMode", order.getPayMode());
		obj.put("shopDetailId", order.getShopDetailId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_ORDER_PAY,obj.toJSONString().getBytes());
		message.setStartDeliverTime(System.currentTimeMillis()+delayTime);
		sendMessageASync(message);
	}

	public void sendCancelOrder(Order order) {
		JSONObject obj  = new JSONObject();
		obj.put("id", order.getId());
		obj.put("brandId", order.getBrandId());
		obj.put("shopDetailId", order.getShopDetailId());
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_ORDER_CANCEL,obj.toJSONString().getBytes());
		sendMessageASync(message);
	}

	/**
	 * 给 Pos2.0 客户端发送指定命令
	 * @param obj
	 */
	public void sendServerCommandToNewPos(JSONObject obj) {
		Message message = new Message(MQSetting.TOPIC_RESTO_SHOP,MQSetting.TAG_SERVER_COMMAND,obj.toJSONString().getBytes());
		sendMessageASync(message);
	}



}
