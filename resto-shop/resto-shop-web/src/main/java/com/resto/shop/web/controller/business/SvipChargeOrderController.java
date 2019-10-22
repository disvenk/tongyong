 package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.SvipChargeOrder;
import com.resto.shop.web.service.SvipChargeOrderService;

@Controller
@RequestMapping("svipchargeorder")
public class SvipChargeOrderController extends GenericController{

	@Resource
	SvipChargeOrderService svipchargeorderService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<SvipChargeOrder> listData(){
		return svipchargeorderService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		SvipChargeOrder svipchargeorder = svipchargeorderService.selectById(id);
		return getSuccessResult(svipchargeorder);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid SvipChargeOrder svipchargeorder){
		svipchargeorderService.insert(svipchargeorder);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid SvipChargeOrder svipchargeorder){
		svipchargeorderService.update(svipchargeorder);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		svipchargeorderService.delete(id);
		return Result.getSuccess();
	}
}
