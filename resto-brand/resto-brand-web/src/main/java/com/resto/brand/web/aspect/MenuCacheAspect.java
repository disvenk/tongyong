package com.resto.brand.web.aspect;


import com.resto.brand.web.listeners.StartupListener;
import com.resto.brand.web.service.PermissionService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

/**
 * 菜单缓存切面，每当菜单改变时，更新菜单缓存
 * @author Diamond
 * @date 2016年3月11日
 */
@Component
@Aspect
public class MenuCacheAspect {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	PermissionService permissionService;

	@Pointcut("execution(* com.resto.brand.web.controller.common.MenuController.edit_*(..))")
	public void updateMenuCache(){};
	
	@After(value = "updateMenuCache()")
	public void changeMenuCache(){
		ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
		StartupListener.initAllMenu(servletContext, permissionService);
	}
}
