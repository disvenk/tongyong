 package com.resto.shop.web.controller.business;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.resto.shop.web.util.OssUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Advert;
import com.resto.shop.web.service.AdvertService;

@Controller
@RequestMapping("advert")
public class AdvertController extends GenericController{

	@Resource
	AdvertService advertService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Advert> listData(){
		return advertService.selectListByShopId(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		Advert advert = advertService.selectById(id);
		return getSuccessResult(advert);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Advert advert,HttpServletRequest request){
		advert.setShopDetailId(request.getSession().getAttribute(SessionKey.CURRENT_SHOP_ID).toString());
		advert.setState((byte)1);
		advertService.insert(advert);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Advert brand){
		advertService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		advertService.delete(id);
		return Result.getSuccess();
	}
}
