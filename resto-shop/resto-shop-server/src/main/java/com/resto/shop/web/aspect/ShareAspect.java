package com.resto.shop.web.aspect;

import javax.annotation.Resource;
import com.resto.api.appraise.entity.Appraise;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.resto.brand.web.model.ShareSetting;
import com.resto.brand.web.service.ShareSettingService;
import com.resto.shop.web.datasource.DataSourceContextHolder;
import com.resto.shop.web.producer.MQMessageProducer;

/**
 * 分享功能切面
 * @author Diamond
 * @date 2016年6月3日
 */
@Component
@Aspect
public class ShareAspect {

	@Resource
	ShareSettingService shareSettingService;

	@Autowired
	MQMessageProducer mqMessageProducer;

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Pointcut("execution(* com.resto.shop.web.service.AppraiseService.saveAppraise(..))")
	public void saveAppraise(){}
	
	@AfterReturning(value="saveAppraise()",returning="appraise")
	public void saveAppraiseSuccess(Appraise  appraise) throws InterruptedException {
		log.info("保存评论成功,触发分享判定:"+appraise.getId());
		if(appraise!=null){
			ShareSetting setting = shareSettingService.selectValidSettingByBrandId(DataSourceContextHolder.getDataSourceName());
			if(setting!=null){
				boolean isCanShare = isCanShare(setting,appraise);			
				log.info("拥有分享好评设置,ID:"+setting.getId());
				if(isCanShare){
					//发送分享通知!
					Long delayTime = setting.getDelayTime() == null ? 120000 : setting.getDelayTime() * 1000L;
					log.info("可以分享 "+delayTime+"ms 后发送通知");
					appraise.setBrandId(setting.getBrandId());
					mqMessageProducer.sendShareMsg(appraise,delayTime);
				}
			}
		}
		
	}

	private boolean isCanShare(ShareSetting setting, Appraise appraise) {
		log.info("Setting,minLevel:"+setting.getMinLevel());
		log.info("appraise,minLevel:"+appraise.getLevel());
		log.info("Setting,getMinLength:"+setting.getMinLength());
		log.info("appraise,getContent:"+appraise.getContent().length());
		if(setting.getMinLevel()<=appraise.getLevel()&&setting.getMinLength()<=appraise.getContent().length()){
			log.info("开始分享:");
			return true;
		}
		log.info("不可分享:");
		return false;
	}

}
