package com.resto.wechat.web.task;

import com.resto.wechat.web.util.WeChatCardUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Task {
	protected Logger log = LoggerFactory.getLogger(getClass());
	public void ApiTicket(){
		log.info("进入刷新api_ticket");
		if(!WeChatCardUtils.appidCard.equals("")&&!WeChatCardUtils.appsecretCard.equals("")){
			//WeChatCardUtils.api_ticket_map.clear();
			String appid= WeChatCardUtils.appidCard;
			String secret=WeChatCardUtils.appsecretCard;
			WeChatCardUtils.api_ticket_map.remove(appid);
			WeChatCardUtils.getApiTicket(appid, secret);
			log.info("定时刷新api_ticket:"+WeChatCardUtils.getApiTicket(appid, secret));
		}
	}
}

