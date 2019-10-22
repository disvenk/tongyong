package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Kitchen;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.KitchenService;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("kitchen")
public class KitchenController extends GenericController{

	@Resource
	KitchenService kitchenService;

	@Autowired
	private PosService posService;

	@Autowired
	private ShopDetailService shopDetailService;

	@RequestMapping("/list")
    public String list(){
		ShopDetail shopDetail = shopDetailService.selectShopByStatus(getCurrentBrandId(),getCurrentShopId());
		if (shopDetail.getEnableDuoDongXian()!=1){
			return "kitchen/list";
		}
		return "kitchen/oldList";
    }
	@RequestMapping("/grouplist")
	public String kitchenGroupList(){
		ShopDetail shopDetail = shopDetailService.selectShopByStatus(getCurrentBrandId(),getCurrentShopId());
		if (shopDetail.getEnableDuoDongXian()!=1){
			return "kitchenGroup/list";
		}
		return "";
	}

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Kitchen> listData(){
		List<Kitchen> kitchens = kitchenService.selectListByShopIdByStatusAll(getCurrentBrandId(),getCurrentShopId());
		return kitchens;
	}

	@RequestMapping("/list_allStatus")
	@ResponseBody
	public List<Kitchen> listDataStatus(){
		List<Kitchen> kitchens = kitchenService.selectListByShopIdByStatus(getCurrentBrandId(),getCurrentShopId());
		for(Kitchen kitchen:kitchens){
			kitchen.setEnableTime(kitchen.getBeginTime()+"至"+kitchen.getEndTime());
			if (kitchen.getStatus()!=1){
				kitchen.setStatusName("开启");
			}else{
				kitchen.setStatusName("未开启");
			}
		}
		return kitchens;
	}


	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		Kitchen kitchen = kitchenService.selectById(id);
		return getSuccessResult(kitchen);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid@RequestBody Kitchen kitchen){
		kitchen.setShopDetailId(getCurrentShopId());
		kitchen.setBrandId(getCurrentBrandId());
		kitchenService.insertSelective(kitchen);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid@RequestBody Kitchen kitchen){
		kitchen.setShopDetailId(getCurrentShopId());
		kitchen.setBrandId(getCurrentBrandId());
		kitchenService.update(kitchen);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(getCurrentBrandId());
		shopMsgChangeDto.setShopId(getCurrentShopId());
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBKITCHEN);
		shopMsgChangeDto.setType("modify");
		shopMsgChangeDto.setId(kitchen.getId().toString());
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		kitchenService.deleteAndBrandId(id,getCurrentBrandId(),getCurrentShopId());
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(getCurrentBrandId());
		shopMsgChangeDto.setShopId(getCurrentShopId());
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBKITCHEN);
		shopMsgChangeDto.setType("delete");
		shopMsgChangeDto.setId(id.toString());
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		return Result.getSuccess();
	}
	@RequestMapping("/selectShopStatus")
	@ResponseBody
	public Integer selectShopStatus(){
		Integer status=kitchenService.selectShopStatus(getCurrentBrandId(),getCurrentShopId());
		return status;
	}
}
