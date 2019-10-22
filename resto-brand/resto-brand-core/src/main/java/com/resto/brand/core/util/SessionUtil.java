package com.resto.brand.core.util;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class SessionUtil {

	private static Map <String, HttpSession> SESSIONMAP=new HashMap<String, HttpSession>();
	private static Map <String,String> sessionLink=new HashMap<String, String>();
	public static HttpSession getSession(String localSessionId){
		return SESSIONMAP.get(localSessionId);
	}
	
	public static void setSession(String localSessionId,HttpSession localSession){
		 SESSIONMAP.put(localSessionId, localSession);
	}
	
	public static void remove(String localSessionId){
		SESSIONMAP.remove(localSessionId);
	}
	
	public static String getLocalSessionId(String allSessionId){
		return sessionLink.get(allSessionId);
	}
	public static void setLink(String allSessionId,String localSessionId){
		sessionLink.put(allSessionId, localSessionId);
	}
	public static void removeL(String allSessionId,String localSessionId){
		sessionLink.remove(allSessionId);
	}
}
