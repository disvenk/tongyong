package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;


import com.resto.shop.web.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.Permission;


@Controller
@RequestMapping("permission")
@SuppressWarnings("SpringJavaAutowiringInspection")
public class PermissionController extends GenericController{

	@Resource
    PermissionService permissionService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Permission> listData(){
		return permissionService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		Permission permission = permissionService.selectById(id);
		return getSuccessResult(permission);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Permission permission){
		permissionService.insert(permission);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Permission permission){
		permissionService.update(permission);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		permissionService.delete(id);
		return Result.getSuccess();
	}
}
