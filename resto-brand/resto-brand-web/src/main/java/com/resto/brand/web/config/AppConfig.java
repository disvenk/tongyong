package com.resto.brand.web.config;

import java.io.IOException;
import java.util.Properties;

public class AppConfig {
	public static final String SUPER_ADMIN;
	public static final String SUPER_PWD;
	static{
		Properties pro = new Properties();
		try {
			pro.load(AppConfig.class.getResourceAsStream("/application.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SUPER_ADMIN = pro.getProperty("SUPER_ADMIN");
		SUPER_PWD = pro.getProperty("SUPER_PWD");
	}
	public static boolean isSuperAdmin(String username, String password) {
		return SUPER_ADMIN.equals(username)&&SUPER_PWD.equals(password);
	}
	
}
