package com.resto.brand.web.controller.common;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.dto.AssignJsTreeDto;
import com.resto.brand.web.model.Permission;
import com.resto.brand.web.model.Role;
import com.resto.brand.web.model.UserGroup;
import com.resto.brand.web.service.PermissionService;
import com.resto.brand.web.service.RoleService;
import com.resto.brand.web.service.UserGroupService;

@Controller
@RequestMapping("role")
public class RoleController extends GenericController{
	
	@Resource
	RoleService roleService;
	@Resource
	PermissionService permissionService;
	@Resource
	UserGroupService userGroupService;
	
	@RequestMapping("list")
	void list(HttpServletRequest request){
		List<UserGroup> userGroup = userGroupService.selectList();
		request.setAttribute("usergroup", userGroup);
	}
	
	@RequestMapping("listData")
	@ResponseBody
	public List<Role> listData(Long userGroupId){
		return roleService.selectList(userGroupId);
	}
	
	@RequestMapping("add")
	public String add(HttpServletRequest request){
		List<UserGroup> userGroup = userGroupService.selectList();
		request.setAttribute("usergroup", userGroup);
		return "role/save";
	}
	
	@RequestMapping("edit")
	public String edit(HttpServletRequest request,Long id){
		Role role = roleService.selectById(id);
		List<UserGroup> userGroup = userGroupService.selectList();
		request.setAttribute("usergroup", userGroup);
		request.setAttribute("m", role);
        System.out.println("..");
        return "role/save";
	}
	
	@RequestMapping("addData")
	@ResponseBody
	public Result addData(@Valid Role model,BindingResult validResult){
		roleService.insert(model);
		return new Result(true);
	}
	
	@RequestMapping("editData")
	@ResponseBody
	public Result editData(@Valid Role model,BindingResult validResult){
		roleService.update(model);
		return new Result(true);
	}
	
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		roleService.delete(id);
		return new Result(true);
	}
	
	@RequestMapping("assign")
	public ModelAndView assignPermissions(Long roleId){
		ModelAndView mv = new ModelAndView("role/assign");
		mv.addObject("roleId", roleId); 
		return mv;
	}
	
	@RequestMapping("assignData")
	@ResponseBody
	public AssignJsTreeDto  assignData(Long roleId) throws ReflectiveOperationException{
		Role role = roleService.selectById(roleId);
		Long userGroupId = role.getUserGroupId();
		List<Permission> hasPermission = permissionService.selectPermissionsByRoleIdWithOutParent(roleId);
		List<Permission> parentPermission = permissionService.selectAllParents(userGroupId);
		List<Permission> allPermission = permissionService.selectList(userGroupId);
		AssignJsTreeDto dto = AssignJsTreeDto.createPermissionTree(hasPermission,parentPermission,allPermission);
		return dto;
	}
	
	@RequestMapping("assign_form")
	@ResponseBody
	public Result assignForm(Long roleId,Long[] pids) throws ReflectiveOperationException{
		roleService.assignRolePermissions(roleId,pids);
		return new Result(true);
	}
		
}
