package com.resto.shop.web.controller.business;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.SmsAcount;
import com.resto.brand.web.service.SmsAcountService;
import com.resto.shop.web.controller.GenericController;

/**
 * 短信账户 Controller
 * @author lmx
 *
 */
@Controller
@RequestMapping("smsacount")
public class SmsAcountController extends GenericController{
	
	@Resource
	SmsAcountService smsAcountService;
	
	@RequestMapping("/selectSmsAcount")
	@ResponseBody
	public Result selectSmsAcount(){
		SmsAcount smsAcount = smsAcountService.selectByBrandId(getCurrentBrandId());
		return getSuccessResult(smsAcount);
	}
	
}
