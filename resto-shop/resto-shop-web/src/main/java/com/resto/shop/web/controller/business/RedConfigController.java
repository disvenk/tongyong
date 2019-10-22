package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.RedConfig;
import com.resto.shop.web.service.RedConfigService;

@Controller
@RequestMapping("redconfig")
public class RedConfigController extends GenericController{

	@Resource
	RedConfigService redconfigService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<RedConfig> listData(){		
		return redconfigService.selectListByShopId(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		RedConfig redconfig = redconfigService.selectById(id);
		return getSuccessResult(redconfig);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid RedConfig redConfig,HttpServletRequest request){
		redConfig.setShopDetailId(request.getSession().getAttribute(SessionKey.CURRENT_SHOP_ID).toString());
		redconfigService.insert(redConfig);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid RedConfig brand){
		redconfigService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		redconfigService.delete(id);
		return Result.getSuccess();
	}
}
