package com.resto.pos.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.ChargeSetting;
import com.resto.shop.web.service.ChargeSettingService;

@Controller
@RequestMapping("chargesetting")
public class ChargeSettingController extends GenericController{

	@Resource
	private ChargeSettingService chargeSettingService;
	
	@RequestMapping("/list_all")
	@ResponseBody
	public Result selectAll(){
		List<ChargeSetting> chargeSettings = new ArrayList<ChargeSetting>();
		try{
			chargeSettings = chargeSettingService.selectListByShopId();
			for(int i = 0; i < chargeSettings.size() ; i++){
				if(chargeSettings.get(i).getState() == 0){
					chargeSettings.remove(i);
					i--;
				}
			}
		}catch (Exception e) {
			log.debug("查询充值赠送活动出错!");
			return new Result("查询充值赠送活动出错!", false);
		}
		return getSuccessResult(chargeSettings);
	}
}
