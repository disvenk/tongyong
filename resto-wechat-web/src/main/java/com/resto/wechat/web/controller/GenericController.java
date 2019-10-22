package com.resto.wechat.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.feature.orm.mybatis.Page;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.SysErrorService;
import com.resto.shop.web.model.Customer;
import com.resto.wechat.web.config.SessionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

@SuppressWarnings("unchecked")
@Component
public abstract class GenericController{
	
	protected Logger log = LoggerFactory.getLogger(getClass());

	private static final String NUMBER = "0123456789";

	@Autowired
	SysErrorService sysErrorService;

	@Resource
	ShopDetailService shopDetailService;
	
	public Result getSuccessResult(){
		return getSuccessResult(null);
	}
	
	public Result getSuccessResult(Object data){
		if(data==null){
			return new Result(true);
		}
		JSONResult<Object> result = new JSONResult<>(data);
		return result;
	}


//	@ExceptionHandler(value = {Exception.class})
//	public void throwExceptionHandler(HttpServletRequest request,HttpServletResponse response,Exception ex){
//		ex.printStackTrace();
//	}



	public static String getErrorInfoFromException(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "\r\n" + sw.toString() + "\r\n";
		} catch (Exception e2) {
			return "bad getErrorInfoFromException";
		}
	}


	public HttpServletRequest getRequest(){
		return  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
	}
	
	public HttpSession getSession(){
		return getRequest().getSession();
	}
	
	public Map<String,String> getParams(){
		HttpServletRequest request = getRequest();
		Map<String,String> params = new HashMap<>();
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
	public JSONPObject getJSONPObject(Object data){
		return new JSONPObject(getRequest().getParameter("callback"), data);
	}
	
	public Object getSessionAttr(String key){
		return getRequest().getSession().getAttribute(key);
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
	
	public ShopDetail getCurrentShop(){
		ShopDetail shopDetail = (ShopDetail) getSessionAttr(SessionKey.CURRENT_SHOP);
		if(shopDetail == null && getCurrentCustomer() != null){
			shopDetail = shopDetailService.selectByPrimaryKey(getCurrentCustomer().getLastOrderShop());
			getSession().setAttribute(SessionKey.CURRENT_SHOP, shopDetail);
		}
		return shopDetail;
	}
	public void setCurrentShop(String sid){
		getSession().setAttribute(SessionKey.CURRENT_SHOP,getShopMap().get(sid));
	}
	
	public String getCurrentShopId(){
		if(getCurrentShop() != null){
			return getCurrentShop().getId();
		}else{
			return null;
		}
	}
	

	public List<ShopDetail> getShopList(){
		return (List<ShopDetail>) getSessionAttr(SessionKey.SHOP_LIST);
	}
	
	public Map<String,ShopDetail> getShopMap(){
		return (Map<String, ShopDetail>) getSessionAttr(SessionKey.SHOP_MAP);
	}
	
	public com.resto.api.customer.entity.Customer getCurrentCustomer(){
		return (com.resto.api.customer.entity.Customer) getSessionAttr(SessionKey.CURRENT_CUSTOMER);
	}
	
	public String getCurrentCustomerId(){
		return getCurrentCustomer().getId();
	}
	
	
	public ShopDetail getShopDetail(String id){
		return getShopMap().get(id);
	}
	
	public String getBaseUrl(){
		HttpServletRequest request = getRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		return basePath;
	}

	public static String generateString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
		}
		return sb.toString();
	}

	/**
	 * yz 2017/07/29
	 * @return
	 * 最终不打算把品牌账户的相关内容存起来 因为不是每个品牌都会开启该功能 部分功能由于参数太多可能有问题
	 *
	 */
//	//是否开启品牌账户
//	public Boolean openBrandAccount(){
//
//		return (Boolean)getSessionAttr(SessionKey.OPENBRANDACCOUNT);
//	}
//
//	//品牌账户设置
//	public AccountSetting getAccountSetting(){
//		return (AccountSetting)getSessionAttr(SessionKey.CURRENT_ACCOUNT_SETTING);
//	}



}
