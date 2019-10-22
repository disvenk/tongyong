package com.resto.brand.web.controller.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.UserGroup;
import com.resto.brand.web.service.RoleService;
import com.resto.brand.web.service.UserGroupService;

@RequestMapping("usergroup")
@Controller
public class UserGroupController extends GenericController{

	@Resource
	UserGroupService userGroupService;
	@Resource
	RoleService roleService;
	
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(UserGroup userGroup){
		userGroupService.insert(userGroup);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(UserGroup userGroup){
		userGroupService.update(userGroup);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		userGroupService.delete(id);
		return Result.getSuccess();
	}
}
