 package com.resto.pos.web.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.service.SmsLogService;


 @RestController
@RequestMapping("smsLog")
public class SmsControler extends GenericController{

	@Resource
	SmsLogService smsLogService;
	
	@RequestMapping("smsLogList")
	public Result smsLogList(){
		
		return getSuccessResult(smsLogService.selectListByShopIdAndDate(getCurrentShopId()));
		//return getSuccessResult(smsLogService.selectListByShopId(getCurrentShopId()));
	}

}
