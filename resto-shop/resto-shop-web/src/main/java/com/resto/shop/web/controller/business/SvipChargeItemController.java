 package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.SvipChargeItem;
import com.resto.shop.web.service.SvipChargeItemService;

@Controller
@RequestMapping("svipchargeitem")
public class SvipChargeItemController extends GenericController{

	@Resource
	SvipChargeItemService svipchargeitemService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<SvipChargeItem> listData(){
		return svipchargeitemService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		SvipChargeItem svipchargeitem = svipchargeitemService.selectById(id);
		return getSuccessResult(svipchargeitem);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid SvipChargeItem svipchargeitem){
		svipchargeitemService.insert(svipchargeitem);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid SvipChargeItem svipchargeitem){
		svipchargeitemService.update(svipchargeitem);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		svipchargeitemService.delete(id);
		return Result.getSuccess();
	}
}
