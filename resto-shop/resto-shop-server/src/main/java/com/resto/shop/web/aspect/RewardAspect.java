package com.resto.shop.web.aspect;

import javax.annotation.Resource;

import com.resto.api.appraise.entity.Appraise;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.resto.brand.web.model.RewardSetting;
import com.resto.brand.web.service.RewardSettingService;
import com.resto.shop.web.datasource.DataSourceContextHolder;

@Component
@Aspect
public class RewardAspect {
	
	Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	RewardSettingService rewardSettingService;
	
	@Pointcut("execution(* com.resto.shop.web.service.AppraiseService.saveAppraise(..))")
	public void saveAppraise(){};
	
	@AfterReturning(value="saveAppraise()",returning="appraise")
	public void reward(Appraise  appraise){
		log.info("检测打赏配置 brandId:"+DataSourceContextHolder.getDataSourceName());
		if(appraise!=null){
			RewardSetting setting = rewardSettingService.selectValidSettingByBrandId(DataSourceContextHolder.getDataSourceName());
			if(setting!=null&&isCanReward(appraise,setting)){
				log.info("打赏配置已激活，可以打赏:"+setting.getId()+"  appraiseId:"+appraise.getId());
				appraise.setCanReward(true);
			}
		}
	}

	private boolean isCanReward(Appraise appraise, RewardSetting setting) {
		if(setting.getMinLevel()<=appraise.getLevel()&&setting.getMinLength()<=appraise.getContent().length()){
			return true;
		}
		return false;
	}
}
