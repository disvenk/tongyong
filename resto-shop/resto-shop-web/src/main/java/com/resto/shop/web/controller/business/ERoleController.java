package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.web.model.Permission;
import com.resto.brand.web.service.PermissionService;
import com.resto.shop.web.dto.AssignShopJsTreeDto;
import com.resto.shop.web.dto.BrandPermissionDto;
import com.resto.shop.web.model.ERole;
import com.resto.shop.web.service.RolePermissionService;
import com.resto.shop.web.service.ShopBrandUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.service.ERoleService;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("erole")
public class ERoleController extends GenericController{

	@Resource
	ERoleService eroleService;

	@Resource
	PermissionService bPermissionService;

	@Resource
    RolePermissionService rolePermissionService;

	@Resource
	ShopBrandUserService shopBrandUserService;

	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ERole> listData(){
		return eroleService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		ERole erole = eroleService.selectById(id);
		return getSuccessResult(erole);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid ERole erole){
		eroleService.insert(erole);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid ERole erole){
		if(erole.getActionScope() == 1){
			//如果把角色的作用域改成了品牌， 则修改所有改角色的shopId为""
			shopBrandUserService.updateUserByRoleId(erole.getId());
		}
		eroleService.update(erole);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		eroleService.delete(id);
		return Result.getSuccess();
	}

	@RequestMapping("assign")
	public ModelAndView assignPermissions(Long roleId){
		ModelAndView mv = new ModelAndView("erole/assign");
		mv.addObject("roleId", roleId);
		return mv;
	}

	@RequestMapping("assignData")
	@ResponseBody
	public AssignShopJsTreeDto assignData(Long roleId) throws ReflectiveOperationException{
//		ERole role = eroleService.selectById(roleId);
		Long userGroupId = Long.valueOf(2);
		List<BrandPermissionDto> hasPermission = rolePermissionService.selectPermissionsByRoleId(roleId);
		List<Permission> parentPermission = bPermissionService.selectAllParents(userGroupId);
		List<Permission> allPermission = bPermissionService.selectList(userGroupId);
        AssignShopJsTreeDto dto = AssignShopJsTreeDto.createPermissionTree(hasPermission,parentPermission,allPermission);
		return dto;
	}

	@RequestMapping("assign_form")
	@ResponseBody
	public Result assignForm(Long roleId,Long[] pids) throws ReflectiveOperationException{
		rolePermissionService.assignRolePermissions(roleId,pids);
		return new Result(true);
	}
}
