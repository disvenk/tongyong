 package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.ChargePayment;
import com.resto.shop.web.service.ChargePaymentService;

@Controller
@RequestMapping("chargepayment")
public class ChargePaymentController extends GenericController{

	@Resource
	ChargePaymentService chargepaymentService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ChargePayment> listData(){
		return chargepaymentService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		ChargePayment chargepayment = chargepaymentService.selectById(id);
		return getSuccessResult(chargepayment);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid ChargePayment brand){
		chargepaymentService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid ChargePayment brand){
		chargepaymentService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		chargepaymentService.delete(id);
		return Result.getSuccess();
	}
}
