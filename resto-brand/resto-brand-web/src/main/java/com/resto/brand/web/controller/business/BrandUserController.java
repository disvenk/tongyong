package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.service.BrandUserService;

@Controller
@RequestMapping("branduser")
public class BrandUserController extends GenericController{

	@Resource
	BrandUserService branduserService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<BrandUser> listData(){
		return branduserService.selectList();
	}
	
	@RequestMapping("/list_one")
	@ResponseBody
	public Result list_one(String id){
		BrandUser branduser = branduserService.selectById(id);
		return getSuccessResult(branduser);
	}
	
	@RequestMapping("/create")
	@ResponseBody
	public Result create(@Valid BrandUser brandUser){
		Result result = branduserService.creatBrandUser(brandUser);
		return result;
	}
	
	@RequestMapping("/modify")
	@ResponseBody
	public Result modify(@Valid BrandUser brandUser){
		//判断是否修改了密码
		if("".equals(brandUser.getPassword().trim()) || brandUser.getPassword().trim() == null){
			brandUser.setPassword(null);
		}else{
			brandUser.setPassword(ApplicationUtils.pwd(brandUser.getPassword()));
		}
		Result result = branduserService.modifyBrandUser(brandUser);
		return result;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(String id){
		branduserService.delete(id);
		return Result.getSuccess();
	}
}
