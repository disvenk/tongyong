package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.OffLineOrder;
import com.resto.shop.web.service.OffLineOrderService;

@Controller
@RequestMapping("offlineorder")
public class OffLineOrderController extends GenericController{

	@Resource
	OffLineOrderService offlineorderService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<OffLineOrder> listData(){
		return offlineorderService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		OffLineOrder offlineorder = offlineorderService.selectById(id);
		return getSuccessResult(offlineorder);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid OffLineOrder offlineorder){
		offlineorderService.insert(offlineorder);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid OffLineOrder offlineorder){
		offlineorderService.update(offlineorder);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		offlineorderService.delete(id);
		return Result.getSuccess();
	}
}
