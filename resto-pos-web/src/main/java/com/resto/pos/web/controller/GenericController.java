package com.resto.pos.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.feature.orm.mybatis.Page;
import com.resto.brand.web.model.AccountSetting;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.model.ShopDetail;
import com.resto.pos.web.config.SessionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public abstract class GenericController {

    protected Logger log = LoggerFactory.getLogger(getClass());

    public Result getSuccessResult() {
        return getSuccessResult(null);
    }


    public Result getSuccessResult(Object data) {
        if (data == null) {
            return new Result(true);
        }
        JSONResult<Object> result = new JSONResult<Object>(data);
        return result;
    }


    @ExceptionHandler(value = {Exception.class})
    public void throwExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ex.printStackTrace();
    }


    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public HttpSession getSession() {
        return getRequest().getSession();
    }

    public Map<String, String> getParams() {
        HttpServletRequest request = getRequest();
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    @SuppressWarnings("rawtypes")
    public Page<?> getPage() {
        Map<String, String> params = getParams();
        if (params.containsKey("pageSize") && params.containsKey("pageNo")) {
            Page<?> page = new Page(getInt("pageSize"), getInt("pageNo"));
            return page;
        } else {
            return null;
        }
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return getObject(key) == null ? defaultValue : Integer.parseInt(getObject(key).toString());
    }

    public Long getLong(String key, long defaultValue) {
        return getObject(key) == null ? defaultValue : Long.valueOf(getObject(key).toString());
    }

    public Long getLong(String key) {
        return getLong(key, 0);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return getObject(key) == null ? defaultValue : String.valueOf(getObject(key));
    }

    public Object getObject(String key) {
        return getParams().get(key);
    }

    public JSONPObject getJSONPObject(Object data) {
        return new JSONPObject(getRequest().getParameter("callback"), data);
    }

    public Object getSessionAttr(String key) {
        return getRequest().getSession().getAttribute(key);
    }

    public Brand getCurrentBrand() {
        return (Brand) getSessionAttr(SessionKey.CURRENT_BRAND);
    }

    public String getCurrentBrandId() {
        if (getCurrentBrand() != null) {
            return getCurrentBrand().getId();
        } else {
            return null;
        }

    }


    public BrandUser getCurrentBrandUser() {
        return (BrandUser) getRequest().getSession().getAttribute(SessionKey.USER_INFO);
    }

    public ShopDetail getCurrentShop() {
        return (ShopDetail) getSessionAttr(SessionKey.CURRENT_SHOP);
    }

    public String getCurrentShopId() {
        if (getCurrentShop() != null) {
            return getCurrentShop().getId();
        } else {
            return null;
        }

    }

    public String getBaseUrl() {
        HttpServletRequest request = getRequest();
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        return basePath;
    }

    /**
     * 获取本机IP地址
     * 在服务器中，获取的为服务器公网IP
     * 在开发环境中，获取的为127.0.0.1
     *
     * @return
     */
    public static String getLocalIP() {
        String SERVER_IP = "";
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (SERVER_IP != null) {
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return SERVER_IP;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

	/**
	 * yz 2017/07/29
	 * @return
	 */
	//是否开启品牌账户
	public Boolean openBrandAccount(){

		return (Boolean)getSessionAttr(SessionKey.OPENBRANDACCOUNT);
	}

	//品牌账户设置
	public AccountSetting getAccountSetting(){
		return (AccountSetting)getSessionAttr(SessionKey.CURRENT_ACCOUNT_SETTING);
	}


}
