package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.SmsLog;
import com.resto.shop.web.service.SmsLogService;

@Controller
@RequestMapping("smslog")
public class SmsLogController extends GenericController{

	@Resource
	SmsLogService smslogService;
	
	@RequestMapping("/list")
        public void list(){
        }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<SmsLog> listData(){
		return smslogService.selectListByShopId(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		SmsLog smslog = smslogService.selectById(id);
		return getSuccessResult(smslog);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid SmsLog brand){
		smslogService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid SmsLog brand){
		smslogService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		smslogService.delete(id);
		return Result.getSuccess();
	}


}
