package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.ShopMode;
import com.resto.brand.web.service.ShopModeService;

@Controller
@RequestMapping("shopmode")
public class ShopModeController extends GenericController{

	@Resource
	ShopModeService shopmodeService;
	
	@RequestMapping("/list")
	public void list(){
	}

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ShopMode> listData(){
		return shopmodeService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		ShopMode shopmode = shopmodeService.selectById(id);
		return getSuccessResult(shopmode);
	}
	@RequestMapping("one")
	@ResponseBody
	public ShopMode listOne(Integer id){
	    ShopMode shopmode = shopmodeService.selectById(id);
	    return shopmode;
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid ShopMode brand){
		shopmodeService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid ShopMode brand){
		shopmodeService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		shopmodeService.delete(id);
		return Result.getSuccess();
	}
}
