package com.resto.wechat.web.controller;

import com.resto.api.article.service.*;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.PinyinUtil;
import com.resto.api.article.entity.*;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.util.ArticleLibraryUtil;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("article")
@RestController
public class ArticleController extends GenericController{
	
	@Resource
	private NewArticleFamilyService newArticleFamilyService;
	@Resource
	private NewArticleAttrService newArticleAttrService;
	@Resource
	private NewArticleService newArticleService;
	@Resource
	private NewRecommendCategoryService newRecommendCategoryService;
	@Resource
	private NewWeightPackageService newWeightPackageService;

	@RequestMapping("articleFamilyList")
	public Result articleFamilyList(Integer distributionModeId){
		return getSuccessResult(newArticleFamilyService.selectListByDistributionModeId(getCurrentBrandId(),getCurrentShopId(), distributionModeId));
	}

	@RequestMapping("/new/articleFamilyList")
	public void articleFamilyListNew(Integer distributionModeId,HttpServletRequest request, HttpServletResponse response){
		AppUtils.unpack(request, response);
        //判断当前品牌是否开启了菜品库
        String shopId = Common.YES.equals(getCurrentBrand().getBrandSetting().getOpenArticleLibrary()) ? ArticleLibraryUtil.SHOPID : getCurrentShopId();
		List<ArticleFamily> articleFamilies =newArticleFamilyService.selectListByDistributionModeId(getCurrentBrandId(),shopId, distributionModeId);
		JSONObject json = new JSONObject();
		json.put("statusCode", "0");
		json.put("success", true);
		json.put("message", "请求成功");
		json.put("data", new JSONArray(articleFamilies));
		ApiUtils.setBackInfo(response, json); // 返回信息设置
	}

	@RequestMapping("articleAttrList")
	public Result articleAttrList(){
		return getSuccessResult(newArticleAttrService.selectListByShopId(getCurrentBrandId(),getCurrentShopId()));
	}

	@RequestMapping("/new/articleAttrList")
	public void articleAttrListNew(HttpServletRequest request, HttpServletResponse response){
		AppUtils.unpack(request, response);
		JSONObject json = new JSONObject();
		json.put("statusCode", "0");
		json.put("message", "请求成功");
		json.put("success", true);
		List<ArticleAttr> articleAttrs = newArticleAttrService.selectListByShopId(getCurrentBrandId(),getCurrentShopId());
		json.put("data", new JSONArray());
		ApiUtils.setBackInfo(response, json); // 返回信息设置
	}

	@RequestMapping("articleList")
	public Result articleList(Integer distributionModeId){
		return getSuccessResult(newArticleService.selectListFull(getCurrentBrandId(),getCurrentShopId(),distributionModeId,"wechat"));
	}

	@RequestMapping("/new/articleList")
	public void articleListNew(Integer distributionModeId,HttpServletRequest request, HttpServletResponse response){
		AppUtils.unpack(request, response);
		JSONObject json = new JSONObject();
		json.put("statusCode", "0");
		json.put("success", true);
        List<Article> result;
        List<Article> articles =newArticleService.selectListFull(getCurrentBrandId(),getCurrentShopId(),distributionModeId,"wechat");
        //判断当前品牌是否开启了菜品库
        if (Common.YES.equals(getCurrentBrand().getBrandSetting().getOpenArticleLibrary())) {
            result = articles.stream().filter(article -> article.getOpenArticleLibrary()).collect(Collectors.toList());
        } else {
            result = articles;
        }
        json.put("message", "请求成功");
        json.put("data", new JSONArray(result));
		ApiUtils.setBackInfo(response, json); // 返回信息设置
	}

	/**
	 * 给品牌下的所有菜品添加英文首字母的字段值
	 * @param brandId
	 * @return
     */
	@RequestMapping("/arttest")
	public String arttest(String brandId){
		List<Article> articles = newArticleService.selectArticleList(brandId);
		for(Article article : articles){
			newArticleService.updateInitialsById(brandId,PinyinUtil.getPinYinHeadChar(article.getName()), article.getId());
		}
		return "yse";
	}

	/**
	 * 菜品推荐,根据店铺id,查询菜品推荐类型
	 * @param request
	 * @param response
	 */
	@RequestMapping("recommendCategoryList")
	public void recommendCategoryList(HttpServletRequest request, HttpServletResponse response,String shopId){
		AppUtils.unpack(request, response);
		List<RecommendCategory> recommendCategorys = newRecommendCategoryService.selectListSortShopId(getCurrentBrandId(),shopId);
		JSONObject json = new JSONObject();
		json.put("statusCode", "0");
		json.put("success", true);
		json.put("message", "请求成功");
		json.put("data", new JSONArray(recommendCategorys));
		ApiUtils.setBackInfo(response, json); // 返回信息设置
	}

	/**
	 * 菜品推荐,根据推荐类型id,查询菜品
	 * @param request
	 * @param response
	 */
	@RequestMapping("/articleRecommendCategoryList")
	public void articleRecommendCategoryList(String recommendCcategoryId,HttpServletRequest request, HttpServletResponse response){
		AppUtils.unpack(request, response);
		JSONObject json = new JSONObject();
		json.put("statusCode", "0");
		json.put("success", true);
		List<Article> articles = newArticleService.selectListByShopIdRecommendCategory(getCurrentBrandId(),getCurrentShopId(),recommendCcategoryId,"wechat");
		json.put("message", "请求成功");
		json.put("data", new JSONArray(articles));
		ApiUtils.setBackInfo(response, json); // 返回信息设置
	}

	@RequestMapping("/articleWeightPackage")
	public void articleWeightPackage(Long weightPackageId, HttpServletRequest request, HttpServletResponse response){
		AppUtils.unpack(request, response);
		WeightPackage weightPackage = newWeightPackageService.getWeightPackageById(getCurrentBrandId(),weightPackageId);
		JSONObject json = new JSONObject();
		json.put("statusCode", "0");
		json.put("success", true);
		json.put("message", "请求成功");
		json.put("data", new JSONObject(weightPackage));
		ApiUtils.setBackInfo(response, json); // 返回信息设置
	}

	@RequestMapping("/weightPackageList")
	public void weightPackageList(HttpServletRequest request, HttpServletResponse response){
		AppUtils.unpack(request, response);
        //判断当前品牌是否开启了菜品库
        String shopId = Common.YES.equals(getCurrentBrand().getBrandSetting().getOpenArticleLibrary()) ? ArticleLibraryUtil.SHOPID : getCurrentShopId();
		List<WeightPackage> list = newWeightPackageService.getAllWeightPackages(getCurrentBrandId(),shopId);
		JSONObject json = new JSONObject();
		json.put("statusCode", "0");
		json.put("success", true);
		json.put("message", "请求成功");
		json.put("data", new JSONArray(list));
		ApiUtils.setBackInfo(response, json); // 返回信息设置
	}
}
