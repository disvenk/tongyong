 package com.resto.shop.web.controller.business;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.web.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.ChargeSetting;
import com.resto.shop.web.service.ChargeSettingService;

@Controller
@RequestMapping("chargesetting")
public class ChargeSettingController extends GenericController{

	@Resource
	ChargeSettingService chargesettingService;
	
	@Autowired
	RedisService redisService;
	
	@RequestMapping("/list")
        public void list(){
        }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ChargeSetting> listData(){
		return chargesettingService.selectListByShopIdAll();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		ChargeSetting chargesetting = chargesettingService.selectById(id);
		return getSuccessResult(chargesetting);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid ChargeSetting brand){
	    brand.setShopDetailId(getCurrentShopId());
	    brand.setBrandId(getCurrentBrandId());
	    brand.setLabelText("充" + brand.getChargeMoney() + "送" + brand.getRewardMoney());
	    brand.setCreateTime(new Date());
	    brand.setId(UUID.randomUUID().toString());
	    chargesettingService.insert(brand);
	    if(redisService.get(getCurrentBrandId()+"chargeList") != null){
			redisService.remove(getCurrentBrandId()+"chargeList");
		}

		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid ChargeSetting brand){
		brand.setLabelText("充" + brand.getChargeMoney() + "送" + brand.getRewardMoney());
		chargesettingService.update(brand);
		if(redisService.get(getCurrentBrandId()+"chargeList") != null){
			redisService.remove(getCurrentBrandId()+"chargeList");
		}
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		chargesettingService.delete(id);
		if(redisService.get(getCurrentBrandId()+"chargeList") != null){
			redisService.remove(getCurrentBrandId()+"chargeList");
		}
		return Result.getSuccess();
	}
}
