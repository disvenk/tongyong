package com.resto.brand.web.controller.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.RoleSign;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.dto.PermissionJsTreeDto;
import com.resto.brand.web.model.Permission;
import com.resto.brand.web.model.UserGroup;
import com.resto.brand.web.service.PermissionService;
import com.resto.brand.web.service.UserGroupService;

@RequestMapping("menu")
@Controller
public class MenuController extends GenericController{

	
	@Resource
	PermissionService permissionService;
	@Resource
	UserGroupService userGroupService;
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("allParents")
	@ResponseBody
	public List<Permission> allParents(Long userGroupId){
		return permissionService.selectAllParents(userGroupId);
	}
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("list")
	public void toList(HttpServletRequest request){
		List<UserGroup> userGroup = userGroupService.selectList();
		request.setAttribute("usergroup", userGroup);
	}
	
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("allAssignJsTreeData")
	@ResponseBody
	public List<PermissionJsTreeDto> allMenus(Long userGroupId) throws ReflectiveOperationException{
		List<Permission> allMenu = permissionService.selectAllMenu(userGroupId);
		List<Permission> parentMenus = permissionService.selectAllParents(userGroupId);
		List<PermissionJsTreeDto> permissionDtoList = new ArrayList<PermissionJsTreeDto>();
		for (Permission permission : parentMenus) {
			PermissionJsTreeDto pdto = new PermissionJsTreeDto(permission);
			pdto.addAllChildren(allMenu);
			permissionDtoList.add(pdto);
		}
		return permissionDtoList;
	}
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("allUrls")
	@ResponseBody
	public List<String> allUrls(){
		return permissionService.selectAllUrls();
	}
	 
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("findById")
	@ResponseBody
	public Permission findById(Long id){
		Permission permission = permissionService.selectById(id);
		return permission;
	}
	
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("edit")
	public void toEdit(HttpServletRequest request,Long id){
		Permission p = permissionService.selectById(id);
		request.setAttribute("m", p);
		request.setAttribute("parentMenus",permissionService.selectAllParents(p.getUserGroupId()));
	}
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("add")
	public void toAdd(HttpServletRequest reqeust,Long userGroupId){
		reqeust.setAttribute("allUrls",permissionService.selectAllUrls());
		reqeust.setAttribute("parentMenus",permissionService.selectAllParents(userGroupId));
		reqeust.setAttribute("userGroupId", userGroupId);
	}
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("save")
	@ResponseBody
	public Result edit_edit(@Valid Permission model,BindingResult validResult){
		Result result = new Result();
		model.setIsMenu(true);
		if(model.getId()==null){
			permissionService.insert(model);
		}else{
			permissionService.update(model);
		}
		result.setSuccess(true);
		return result;
	}
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("delete")
	@ResponseBody 
	public Result edit_delete(Long id){
		Result result =new Result();
		permissionService.delete(id);
		result.setSuccess(true);
		return result;
	}
	
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("listData")
	@ResponseBody
	public List<Permission> list() {
		return permissionService.selectListByParentId(getLong("parentId"));
	}
	
	
	/**
	 * 添加子权限
	 * @param parentId
	 * @return
	 */
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("addSub")
	public ModelAndView edit_addSub(Long parentId){
		ModelAndView mv = new ModelAndView("menu/addSub");
		mv.addObject("parentId", parentId);
		return mv;
	}
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("addSubData")
	@ResponseBody
	public Result edit_addSubData(@Valid Permission model,BindingResult validResult){
		Result result = new Result();
		permissionService.addSubPermission(model);
		result.setSuccess(true);
		return result;
	}
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("editSub")
	public ModelAndView edit_editSub(Long id){
		ModelAndView mv = new ModelAndView("menu/addSub");
		mv.addObject("m", permissionService.selectById(id));
		return mv;
	}
	
	@RequiresRoles({RoleSign.SUPER_ADMIN})
	@RequestMapping("editSubData")
	@ResponseBody
	public Result edit_editSubData(@Valid Permission model,BindingResult validResult){
		Result result = new Result();
		permissionService.updateSubPermission(model);
		result.setSuccess(true);
		return result;
	}
	
}
