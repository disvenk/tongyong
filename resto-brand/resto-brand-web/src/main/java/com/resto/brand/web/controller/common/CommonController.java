package com.resto.brand.web.controller.common;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.enums.MenuType;
import com.resto.brand.core.enums.UserGroupSign;
import com.resto.brand.web.model.Permission;
import com.resto.brand.web.service.PermissionService;

/**
 * 公共视图控制器
 * 
 * @author Diamond
 * @since 2016年6月2日
 **/
@Controller
public class CommonController {
    /**
     * 首页
     * 
     * @param request
     * @return
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        return "index";
    }
    @Resource
    PermissionService permissionService;
    
    
    @RequestMapping("/installedPermission")
    @ResponseBody
    public String[] installed(String classPath,String classPackage,String className) throws ClassNotFoundException{
    	
    	String [] classPrimay =new String[]{className}; 
    	
    	String [] permissionList = new String[]{"add","delete","modify"};
    	long groupid = UserGroupSign.SYSTEM_GROUP;
		Permission r = new Permission();
		r.setUserGroupId(groupid);
		r.setIsMenu(true);
		r.setMenuType(MenuType.AJAX);
		r.setPermissionName("Auto Genic Manager");
		r.setPermissionSign("auto genic"+System.currentTimeMillis());
		long baseId=3005;
		r.setId(baseId++);
		permissionService.insert(r);
		Long rootId = r.getId();
		for (String cp: classPrimay) {
			
			String modelName = cp;
			String modelNameLower = modelName.toLowerCase();
			Permission p = new Permission();
			p.setId(baseId++);
			p.setIsMenu(true);
			p.setMenuType(MenuType.AJAX);
			p.setPermissionName(modelName+" Manager");
			p.setPermissionSign(modelNameLower+"/list");
			p.setParentId(rootId);
			p.setUserGroupId(groupid);
			permissionService.insert(p);
			
			Long pid = p.getId();
			for(String childPermission:permissionList){
				Permission child = new Permission();
				child.setId(baseId++);
				child.setUserGroupId(groupid);
				child.setIsMenu(false);
				child.setMenuType(MenuType.BUTTON);
				child.setPermissionName(modelNameLower+":"+childPermission);
				child.setPermissionSign(modelNameLower+"/"+childPermission);
				child.setParentId(pid);
				permissionService.insert(child);
			}
		}
    	
		return classPrimay;
    }

}