package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.OrderPaymentItem;
import com.resto.shop.web.service.OrderPaymentItemService;

@Controller
@RequestMapping("orderpaymentitem")
public class OrderPaymentItemController extends GenericController{

	@Resource
	OrderPaymentItemService orderpaymentitemService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<OrderPaymentItem> listData(){
		return orderpaymentitemService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		OrderPaymentItem orderpaymentitem = orderpaymentitemService.selectById(id);
		return getSuccessResult(orderpaymentitem);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid OrderPaymentItem brand){
		orderpaymentitemService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid OrderPaymentItem brand){
		orderpaymentitemService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		orderpaymentitemService.delete(id);
		return Result.getSuccess();
	}
}
