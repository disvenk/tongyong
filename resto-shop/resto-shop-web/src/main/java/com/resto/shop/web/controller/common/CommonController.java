package com.resto.shop.web.controller.common;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.enums.MenuType;
import com.resto.brand.core.enums.UserGroupSign;
import com.resto.brand.web.model.Permission;


/**
 * 公共视图控制器
 **/
@Controller
@SuppressWarnings("SpringJavaAutowiringInspection")
public class CommonController {
	
	@Resource
	com.resto.brand.web.service.PermissionService brandPermissionService;
	
    /**
     * 首页
     * @param request
     * @return
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request) {
    	List<Permission> allMenu = brandPermissionService.selectFullStructMenu(UserGroupSign.BRAND_GROUP);
    	request.setAttribute("allMenu", allMenu);
        return "index";
    }
    
    
    /**
     * 
     * @param classPath  例如 D:/work/RestoPlus/gitRepository/resto-shop/resto-shop-api/src/main/java/com/resto/shop/web/model
     * @param classPackage 例如 com.resto.shop.web.model
     * @param className 例如 ArticleAttr
     * @return
     * @throws ClassNotFoundException
     */
    @RequestMapping("/installedPermission")
    @ResponseBody
    public String[] installed(String classPath,String classPackage,String className) throws ClassNotFoundException{
    	
    	String [] classPrimay =new String[]{className}; 
    	if(StringUtils.isEmpty(className)){
    		classPrimay = getModelClass(classPath,classPackage);
    	}
    	
    	String [] permissionList = new String[]{"add","delete","edit"};
    	long groupid = UserGroupSign.BRAND_GROUP;
		Permission r = new Permission();
		r.setUserGroupId(groupid);
		r.setIsMenu(true);
		r.setMenuType(MenuType.AJAX);
		r.setPermissionName("Auto Genic Manager");
		r.setPermissionSign("auto genic"+System.currentTimeMillis());
		r.setId(500L);
        brandPermissionService.insert(r);
		Long rootId = r.getId();
		long base = 1;
		for (String cp: classPrimay) {
			
			String modelName = cp;
			String modelNameLower = modelName.toLowerCase();
			Permission p = new Permission();
			p.setId(rootId*10+base++);
			p.setIsMenu(true);
			p.setMenuType(MenuType.AJAX);
			p.setPermissionName(modelName+" Manager");
			p.setPermissionSign(modelNameLower+"/list");
			p.setParentId(rootId);
			p.setUserGroupId(groupid);
            brandPermissionService.insert(p);
			
			Long pid = p.getId();
			long childbase = 1;
			for(String childPermission:permissionList){
				Permission child = new Permission();
				child.setId(p.getId()*10+childbase++);
				child.setUserGroupId(groupid);
				child.setIsMenu(false);
				child.setMenuType(MenuType.BUTTON);
				child.setPermissionName(modelNameLower+":"+childPermission);
				child.setPermissionSign(modelNameLower+"/"+childPermission);
				child.setParentId(pid);
                brandPermissionService.insert(child);
			}
		}
    	
		return classPrimay;
    }
    
    private static String[] getModelClass(String classPath, String classPackage) throws ClassNotFoundException {
		File file = new File(classPath);
		File[] fileList = file.listFiles();
		String []  classPrimay = new String [fileList.length];
		for(int i=0;i<fileList.length;i++){
			File f = fileList[i];
			String className = classPackage+"."+f.getName().substring(0,f.getName().length()-5);
			Class<?> clazz = Class.forName(className);
			String classSimpleName = clazz.getSimpleName();
			classPrimay[i] = classSimpleName;
		}
		return classPrimay;
	}

}