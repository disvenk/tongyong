package com.resto.brand.web.listeners;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.resto.brand.core.enums.UserGroupSign;
import com.resto.brand.web.model.Permission;
import com.resto.brand.web.service.PermissionService;

@Component
public class StartupListener implements ApplicationContextAware,ServletContextListener{	
	
	public static ApplicationContext application;
	public static ServletContext servletContext;
	Logger log = Logger.getLogger(getClass()); 
	@Override
	public void setApplicationContext(ApplicationContext context)throws BeansException {
		application = context;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
		PermissionService permissionService = application.getBean(PermissionService.class);
		initAllMenu(servletContext, permissionService);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("context was destoryed");
	}

	public static void initAllMenu(ServletContext servletContext, PermissionService permissionService) {
		List<Permission> allParents = permissionService.selectFullStructMenu(UserGroupSign.SYSTEM_GROUP); 
		servletContext.setAttribute("allMenu", allParents);
	}

}
