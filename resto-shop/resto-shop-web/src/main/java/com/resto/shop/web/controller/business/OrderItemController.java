package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.OrderItem;
import com.resto.shop.web.service.OrderItemService;

@Controller
@RequestMapping("orderitem")
public class OrderItemController extends GenericController{

	@Resource
	OrderItemService orderitemService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<OrderItem> listData(){
		return orderitemService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		OrderItem orderitem = orderitemService.selectById(id);
		return getSuccessResult(orderitem);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid OrderItem brand){
		orderitemService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid OrderItem brand){
		orderitemService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		orderitemService.delete(id);
		return Result.getSuccess();
	}
}
