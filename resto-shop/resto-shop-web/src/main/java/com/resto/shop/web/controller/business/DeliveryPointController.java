package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.DeliveryPoint;
import com.resto.shop.web.service.DeliveryPointService;

@Controller
@RequestMapping("deliverypoint")
public class DeliveryPointController extends GenericController{

	@Resource
	DeliveryPointService deliverypointService;
	
	@RequestMapping("/list")
        public void list(){
        }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<DeliveryPoint> listData(){
		return deliverypointService.selectListById(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		DeliveryPoint deliverypoint = deliverypointService.selectById(id);
		return getSuccessResult(deliverypoint);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid DeliveryPoint brand){
	        String shopDetailId = getCurrentShopId();
	        brand.setShopDetailId(shopDetailId);
		deliverypointService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid DeliveryPoint brand){
		deliverypointService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		deliverypointService.delete(id);
		return Result.getSuccess();
	}
}
