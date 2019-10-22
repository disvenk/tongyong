package com.resto.geekpos.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.resto.brand.core.entity.DataVailedException;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.feature.orm.mybatis.Page;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.model.SysLoginLog;
import com.resto.brand.web.service.TokenService;
import com.resto.geekpos.web.config.SessionKey;
import com.resto.geekpos.web.model.CheckResult;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.model.Customer;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class GenericController {
	
	protected Logger log = LoggerFactory.getLogger(getClass());

	@Value("#{configProperties['dev']}")
	private String keyValue;

	@Value("#{configProperties['token.skip']}")
	private String apiInterfaceNames;

	@Autowired
	private TokenService tokenService;

	private static String RANDOM_STRINGS = "abcdefghijklmnopqrstuvwxyz0123456789";

	public Result getSuccessResult() {
		return getSuccessResult(null);
	}
	
	public Result getSuccessResult(Object data){
		if(data==null){
			return new Result(true);
		}
		JSONResult<Object> result = new JSONResult<Object>(data); 
		return result;
	}
	
	
	@ExceptionHandler
	@ResponseBody
	public void otherExceptionHandler(HttpServletRequest request,HttpServletResponse response,Exception ex) throws IOException{
		log.info("Error Begin-------------------------------------");
		log.error(ex.toString());
		ex.printStackTrace();
		log.info("Error End-------------------------------------");
		request.setAttribute("errorType", ex.getClass().getName());
		request.setAttribute("error", ex.toString());
		response.sendError(500, ex.getClass().getSimpleName());
	}
	
	@ExceptionHandler(BindException.class)
	@ResponseBody
	public Result bindException(BindException bindEx){
		List<ObjectError> oe = bindEx.getAllErrors();
		Result result = new Result();
		result.setSuccess(false);
		for (ObjectError objectError : oe) {
			String message = objectError.getDefaultMessage();
			result.setMessage("数据校验异常："+message);
			return result;
		}
		result.setMessage("数据校验异常:"+bindEx.getMessage());
		return result;
	}
	@ExceptionHandler(DataVailedException.class)
	@ResponseBody
	public Result dataVailedException(Exception ex){
		Result result = new Result(false);
		result.setMessage(ex.getMessage());
		return result;
	}
	
	public HttpServletRequest getRequest(){
		return  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
	}
	
	public HttpSession getSession(){
		return getRequest().getSession();
	}
	
	public Map<String,String> getParams(){
		HttpServletRequest request = getRequest();
		Map<String,String> params = new HashMap<String,String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = iter.next();
			String[] values =  requestParams.get(name);
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
	public Page<?> getPage(){
		Map<String,String> params = getParams();
		if(params.containsKey("pageSize")&&params.containsKey("pageNo")){
			Page<?> page = new Page(getInt("pageSize"),getInt("pageNo"));
			return page;
		}else{
			return null;
		}
	}
	
	public int getInt(String key){
		return getInt(key, 0);
	}
	
	public int getInt(String key,int defaultValue){
		return getObject(key)==null?defaultValue:Integer.parseInt(getObject(key).toString());
	}
	
	public Long getLong(String key,long defaultValue){
		return getObject(key)==null?defaultValue:Long.valueOf(getObject(key).toString());
	}
	
	public Long getLong(String key){
		return getLong(key, 0);
	}
	public String getString(String key){
		return getString(key, "");
	}
	
	public String getString(String key,String defaultValue){
		return getObject(key)==null?defaultValue:String.valueOf(getObject(key));
	}
	
	public Object getObject(String key){
		return getParams().get(key);
	}

	public Object getSessionAttr(String key){
		return getRequest().getSession().getAttribute(key);
	}

	public JSONPObject getJSONPObject(Object data){
		return new JSONPObject(getRequest().getParameter("callback"), data);
	}
	
	public String getCurrentShopId(){
		return getCurrentBrandUser().getShopDetailId();
	}

	public Brand getCurrentBrand(){
		return (Brand) getSessionAttr(SessionKey.CURRENT_BRAND);
	}

	public String getCurrentBrandId(){
		if(getCurrentBrand() != null){
			return getCurrentBrand().getId();
		}else{
			return null;
		}
	}

	public Customer getCurrentCustomer(){
		return (Customer) getSessionAttr(SessionKey.CURRENT_CUSTOMER);
	}

	public String getCurrentCustomerId(){
		return getCurrentCustomer().getId();
	}

	public BrandUser getCurrentBrandUser(){
		return (BrandUser) getRequest().getSession().getAttribute(SessionKey.USER_INFO);
	}
	
	public String getCurrentUserId(){
		return getCurrentBrandUser().getId();
	}

	public String getBrandName(){
		
		return getCurrentBrandUser().getBrandName();
	}
	
	public String getBaseUrl(){
		HttpServletRequest request = getRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		return basePath;
	}

	/**
	 * 获取当前请求的 品牌标识
	 * @return
	 */
	public String getBrandSign(){
		HttpServletRequest request = getRequest();
		String requestURL = request.getRequestURL().toString();
		return requestURL.substring("http://".length(), requestURL.indexOf("."));
	}

	public CheckResult check(HttpServletRequest request, HttpServletResponse response, String apiName) {
		MobilePackageBean mobile = AppUtils.unpack(request, response);
		Map queryParams = (Map) mobile.getContent();
		CheckResult result = new CheckResult();
		Map local = new HashMap();
		local.put("api", apiName);
		local.put("keyValue", keyValue);
		local.put("apiNames", apiInterfaceNames);


		String[] interfaceName = local.get("apiNames").toString().split(","); //遍历越过token的接口名称

		Boolean loginFlag = false;
		for (String name : interfaceName) {
			if (name.equals(apiName)) {
				loginFlag = true;
				break;
			}
		}

		if (loginFlag) {
			local.put("token", "");
		} else {
			SysLoginLog log = tokenService.queryToken(queryParams.get("header[userId]").toString());
			if (log != null) { //根据userid 拿到对应的token
				local.put("token", log.getToken());
			} else {
				local.put("token", "");
			}
		}



		if (!AppUtils.validatePackage(mobile, response, local)) {
			result.setSuccess(false);
			return result;
		}

		result.setSuccess(true);
		return result;
	}

	/**
	 * 生成随机字符串
	 *
	 * @param count 默认32位
	 * @return
	 */
	public static String random(int... count) {
		if (null != count && count.length > 0) {
			return RandomStringUtils.random(count[0], RANDOM_STRINGS);
		}
		return RandomStringUtils.random(32, RANDOM_STRINGS);
	}
}
