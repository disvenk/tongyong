package com.resto.shop.web.controller.common;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.UserGroupSign;
import com.resto.brand.web.model.Role;
import com.resto.brand.web.model.UserGroup;
import com.resto.brand.web.service.RoleService;
import com.resto.brand.web.service.UserGroupService;
import com.resto.shop.web.controller.GenericController;

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
	
	
	@RequestMapping("list_all")
	@ResponseBody
	public Result listRow(Long id){
		
		List<Role> lists = roleService.selectList(UserGroupSign.BRAND_GROUP);
		
		return getSuccessResult(lists);
	}
}
