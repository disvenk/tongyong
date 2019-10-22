package com.resto.geekpos.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.metadata.Table;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.geekpos.web.config.SessionKey;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.model.TableCode;
import com.resto.shop.web.service.TableCodeService;

/**
 * 电视端 排号页面的主要方法
 * @author lmx
 * @version 创建时间：2016年12月13日 下午7:04:27
 */
@RequestMapping("/tv")
@Controller
public class TvController extends GenericController {
	
	@Resource
	private ShopDetailService shopDetailService;
	@Resource
	private TableCodeService tableCodeService; 
	
	
	@RequestMapping("/info")
	@ResponseBody
	public void login(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandId = queryParams.get("brandId").toString();
        String shopId = queryParams.get("shopId").toString();
        
        //定位数据库
        DataSourceTarget.setDataSourceName(brandId);
        
        ShopDetail shop = shopDetailService.selectQueueInfo(shopId);
        
        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", new JSONObject(shop));
        
        ApiUtils.setBackInfo(response, json); // 返回信息设置
	}
	

	@RequestMapping("/list")
	@ResponseBody
	public void list(HttpServletRequest request, HttpServletResponse response) {
		MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        String brandId = queryParams.get("brandId").toString();
        String shopId = queryParams.get("shopId").toString();
        
        //定位数据库
        DataSourceTarget.setDataSourceName(brandId);
        
        List<TableCode> tableCodeInfo = tableCodeService.selectTableAndGetNumbers(shopId);
        
        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", tableCodeInfo);
        
        ApiUtils.setBackInfo(response, json); // 返回信息设置
	}
	
	/**
	 * Tv端用于检测，tomcat是否正常运行
	 * @author lmx
	 * @version 创建时间：2016年12月12日 下午3:07:25
	 * @return
	 */
	@RequestMapping("/ping")
	@ResponseBody
	public void ping(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
		BrandUser user =  (BrandUser) session.getAttribute(SessionKey.USER_INFO);
		
		JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        json.put("data", user!=null?user.getShopDetailId():null);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
	}
}
