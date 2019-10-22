package com.resto.geekpos.web.controller;

import com.resto.api.article.service.NewArticleFamilyService;
import com.resto.api.article.service.NewArticleService;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.geekpos.web.constant.DictConstants;
import com.resto.geekpos.web.model.CheckResult;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.model.*;
import com.resto.api.article.entity.ArticleFamily;
import com.resto.api.article.entity.Article;
import com.resto.shop.web.service.ArticleService;
import com.resto.shop.web.service.OrderService;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by carl on 2016/9/6.
 */
@Controller
@RequestMapping("article")
public class ArticleController extends GenericController {

    @Resource
    private NewArticleFamilyService newArticleFamilyService;

    @Resource
    private NewArticleService newArticleService;

    @Resource
    private OrderService orderService;

    @Resource
    private BrandSettingService brandSettingService;

    @RequestMapping(value="/getArticleInfo",method = RequestMethod.POST)
    public void  getArticleInfo(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getArticleInfo");
        if (!result.isSuccess()) {
            return;
        }
        String brandId = queryParams.get("brandId").toString();
        String articleId = queryParams.get("articleId").toString();
        Article article = newArticleService.selectFullById(brandId,articleId,"wechat");
        JSONObject json = new JSONObject();
        json.put("msg", "请求成功");
        json.put("code", DictConstants.SUCCESS);
        json.put("data", new JSONObject(article));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value="/getFamily",method = RequestMethod.POST)
    public void getFamily(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getFamily");
        if (!result.isSuccess()) {
            return;
        }
        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);

        String shopId = queryParams.get("shopId").toString();
        Integer distributionModeId = Integer.parseInt(queryParams.get("distributionModeId").toString());
        List<ArticleFamily> falist = newArticleFamilyService.selectListByDistributionModeId(brandId,shopId,distributionModeId);

        JSONObject json = new JSONObject();
        json.put("msg", "请求成功");
        json.put("code", DictConstants.SUCCESS);
        json.put("data", falist);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value="/getArticleByFamilyId",method = RequestMethod.POST)
    public void getArticleByFamilyId(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getArticleByFamilyId");
        if (!result.isSuccess()) {
            return;
        }
        String brandId = queryParams.get("brandId").toString();
        String shopId = queryParams.get("shopId").toString();
        Integer distributionModeId = Integer.parseInt(queryParams.get("distributionModeId").toString());
        List<Article> article = newArticleService.selectListFull(brandId,shopId,distributionModeId,"wechat");
//        JSONArray articleList = new JSONArray();
//
//        //循环店铺下所有菜品进行封装
//        for(Article a : article){
//            JSONObject articleInfo = new JSONObject();
//            articleInfo.put("articleId", a.getId());
//            articleInfo.put("articleName", a.getName());
//            articleInfo.put("articlePrice", brand.getGeekPosPrice() == 0 ? a.getPrice() : a.getFansPrice());
//            articleInfo.put("unit", a.getUnit());
//            articleInfo.put("articleType", a.getArticleType());
//
//            //处理套餐
//            if(a.getArticleType().equals(2)){
//                JSONArray attrList = new JSONArray();
//                List<MealAttr> mealAttrs = a.getMealAttrs();
//                for(MealAttr ma : mealAttrs){
//                    List<MealItem> mealItems = ma.getMealItems();
//                    JSONObject childList  = new JSONObject();
//                    childList.put("attrId", ma.getId());
//                    childList.put("attrName", ma.getName());
//                    JSONArray child = new JSONArray();
//                    for(MealItem mi : mealItems){
//                        JSONObject smallChild  = new JSONObject();
//                        smallChild.put("articleId", mi.getId());
//                        smallChild.put("articleName", mi.getArticleName());
//                        child.put(smallChild);
//                    }
//                    childList.put("childList", child);
//                    attrList.put(childList);
//                }
//                articleInfo.put("attrList",attrList);
//            }
//            //处理有规格单品
//            if(a.getArticleType().equals(1) && a.getArticlePrices().size() != 0){
//                List<ArticlePrice> articlePrices = a.getArticlePrices();
//                JSONArray unitList = new JSONArray();
//                for(ArticlePrice ap : articlePrices){
//                    JSONObject unit = new JSONObject();
//                    unit.put("articleId", ap.getId());
//                    unit.put("articleName", ap.getName());
//                    unit.put("articlePrice", brand.getGeekPosPrice() == 0 ? ap.getPrice() : ap.getFansPrice());
//                    unitList.put(unit);
//                }
//                articleInfo.put("unitList",unitList);
//            }
//            articleList.put(articleInfo);
//        }

        JSONObject json = new JSONObject();
        json.put("msg", "请求成功");
        json.put("code", DictConstants.SUCCESS);
        json.put("data", article);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getList");
        if (!result.isSuccess()) {
            return;
        }
        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);

        String shopId = queryParams.get("shopId").toString();
        List<Order> orders = orderService.getTableNumberAll(shopId);

        JSONObject json = new JSONObject();
        json.put("msg", "请求成功");
        json.put("code", DictConstants.SUCCESS);
        json.put("data", orders);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value="/getDetail",method = RequestMethod.POST)
    public void getDetail(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getDetail");
        if (!result.isSuccess()) {
            return;
        }
        String brandid = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandid);
        String orderId = queryParams.get("orderId").toString();
        Order order = orderService.getOrderDetail(orderId);
        JSONObject json = new JSONObject();
        json.put("msg", "请求成功");
        json.put("code", DictConstants.SUCCESS);
        json.put("data", new JSONObject(order));
        ApiUtils.setBackInfo(response, json); // 返回信息设置

    }
}
