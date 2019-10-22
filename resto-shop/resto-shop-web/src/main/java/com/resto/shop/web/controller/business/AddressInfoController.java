 package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.AddressInfo;
import com.resto.brand.web.service.AddressInfoService;
import com.resto.shop.web.controller.GenericController;

@Controller
@RequestMapping("addressinfo")
public class AddressInfoController extends GenericController{

	@Resource
	AddressInfoService addressInfoService;

	@RequestMapping("/list_all")
	@ResponseBody
	public Result listData(){
		List<AddressInfo> addressInfos = addressInfoService.selectByBrandId(getCurrentBrandId());
		return getSuccessResult(addressInfos);
	}
	
	
	@RequestMapping("/create")
	@ResponseBody
	public Result create(AddressInfo addressInfo){
		addressInfo.setBrandId(getCurrentBrandId());
		addressInfo.setId(ApplicationUtils.randomUUID());
		int row = addressInfoService.insert(addressInfo);
		return new Result(row>0?true:false);
	}
	
	@RequestMapping("/modify")
	@ResponseBody
	public Result modify(AddressInfo addressInfo){
		int row = addressInfoService.update(addressInfo);
		return new Result(row>0?true:false);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public boolean delete(String id){
		int row = addressInfoService.delete(id);
		return row>0?true:false;
	}
}
