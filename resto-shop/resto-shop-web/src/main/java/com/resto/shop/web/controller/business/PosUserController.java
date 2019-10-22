package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.PosUser;
import com.resto.shop.web.service.PosUserService;

@Controller
@RequestMapping("posuser")
public class PosUserController extends GenericController{

	@Resource
	PosUserService posuserService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<PosUser> listData(){
		return posuserService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		PosUser posuser = posuserService.selectById(id);
		return getSuccessResult(posuser);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid PosUser posuser){
		posuserService.insert(posuser);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid PosUser posuser){
		posuserService.update(posuser);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		posuserService.delete(id);
		return Result.getSuccess();
	}
}
