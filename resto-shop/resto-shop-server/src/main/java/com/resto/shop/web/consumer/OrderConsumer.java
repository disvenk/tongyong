package com.resto.shop.web.consumer;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.resto.brand.core.util.MQSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Properties;

@Component
public class OrderConsumer{
	Logger log = LoggerFactory.getLogger(getClass());
	Consumer consumer =  null;
	
	@Resource
	OrderMessageListener orderMessageListener;
	
	@PostConstruct
	public void startConsumer(){
		Properties pro= MQSetting.getPropertiesWithAccessSecret();
		pro.setProperty(PropertyKeyConst.ConsumerId, MQSetting.CID_SHOP);
		log.info("正在启动消费者");
		consumer = ONSFactory.createConsumer(pro);
		consumer.subscribe(MQSetting.TOPIC_RESTO_SHOP, MQSetting.TAG_CANCEL_ORDER + "||"+MQSetting.TAG_AUTO_CONFIRM_ORDER
				+"||"+MQSetting.TAG_NOT_PRINT_ORDER+"||"+MQSetting.TAG_NOT_ALLOW_CONTINUE+"||"+MQSetting.TAG_SHOW_ORDER+
				"||"+MQSetting.TAG_AUTO_REFUND_ORDER+"||"+MQSetting.TAG_NOTICE_SHARE_CUSTOMER+"||"+MQSetting.SEND_CALL_MESSAGE
				+"||"+MQSetting.TAG_REMIND_MSG+"||"+MQSetting.TAG_AUTO_SEND_REMMEND+"||"+MQSetting.TAG_BOSS_ORDER+"||"+MQSetting.TAG_BRAND_ACCOUNT_SEND
				+"||"+MQSetting.TAG_REMOVE_TABLE_GROUP+"||"+MQSetting.TAG_SHARE_GIVE_MONEY, orderMessageListener);
//		consumer.subscribe(MQSetting.TOPIC_RESTO_SHOP, MQSetting.TAG_ALL, orderMessageListener);
		consumer.start();
		log.info("消费者启动成功！TOPIC:"+MQSetting.TOPIC_RESTO_SHOP+"  CID:"+MQSetting.CID_SHOP);
	}

	@PreDestroy
	public void stopConsumer(){
		consumer.shutdown();
		log.info("消费者关闭！");
	}
}
