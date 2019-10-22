package com.resto.geekpos.web.controller;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.geekpos.web.constant.DictConstants;
import com.resto.geekpos.web.model.CheckResult;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.model.Article;
import com.resto.shop.web.model.ArticlePrice;
import com.resto.shop.web.model.MealItem;
import com.resto.shop.web.model.ShopCart;
import com.resto.shop.web.service.ArticleService;
import com.resto.shop.web.service.ShopCartService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by carl on 2016/9/7.
 */
@Controller
@RequestMapping("shopCar")
public class ShopCarController extends GenericController {

    @Resource
    ShopCartService shopCartService;

    @Resource
    private ArticleService articleService;

    @Resource
    private BrandSettingService brandSettingService;

    /**
     * 接受参数 articleId,distributionModeId,number 餐品id,模式id,数量
     先查询当前客户是否有该商品的 购物车的条目，如果有 则更新，如果没有则新建，如果number等于0 则删除
     *
     * @param shopCart
     * @return
     */
    @RequestMapping(value = "/addShopCar", method = RequestMethod.POST)
    public void addShopCar(ShopCart shopCart,HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "addShopCar");
        if (!result.isSuccess()) {
            return;
        }

        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);

        String number = queryParams.get("number").toString();
        String shopId = queryParams.get("shopId").toString();
        String userId = queryParams.get("userId").toString();

        shopCart.setShopDetailId(shopId);
        shopCart.setUserId(userId);
        shopCart.setNumber(Integer.parseInt(number));
        shopCart.setCustomerId("");
        shopCart.setShopType("1");
        shopCart.setPid(0);
        shopCartService.updateShopCart(shopCart);

        JSONObject json = new JSONObject();
        json.put("msg", "请求成功");
        json.put("code", DictConstants.SUCCESS);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value = "/addShopcartMeal", method = RequestMethod.POST)
    public void addShopcartMeal( HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "addShopcartMeal");
        if (!result.isSuccess()) {
            return;
        }

        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);

        String shopId = queryParams.get("shopId").toString();
        String articleId = queryParams.get("articleId").toString();
        String distributionModeId = queryParams.get("distributionModeId").toString();
        String number = queryParams.get("number").toString();
        String mealItemsss = queryParams.get("mealItems").toString();
        String[] mi = mealItemsss.split(",");
        String userId = queryParams.get("userId").toString();

        ShopCart shopCartInfo = new ShopCart();
        shopCartInfo.setNumber(Integer.parseInt(number));
        shopCartInfo.setArticleId(articleId);
        shopCartInfo.setDistributionModeId(Integer.parseInt(distributionModeId));
        shopCartInfo.setShopDetailId(shopId);
        shopCartInfo.setCustomerId("");
        shopCartInfo.setShopType("2");
        shopCartInfo.setUserId(userId);
        shopCartInfo.setPid(0);
        int farId = shopCartService.updateShopCart(shopCartInfo);
        log.info("套餐添加成功---------------------------");

            for (int a = 0; a < mi.length; a++){
                ShopCart shopCartChild = new ShopCart();
                shopCartChild.setNumber(Integer.parseInt(number));
                shopCartChild.setArticleId(mi[a]);
                shopCartChild.setDistributionModeId(Integer.parseInt(distributionModeId));
                shopCartChild.setShopDetailId(shopId);
                shopCartChild.setCustomerId("");
                shopCartChild.setShopType("3");
                shopCartChild.setUserId(userId);
                shopCartChild.setPid(farId);
                shopCartService.updateShopCart(shopCartChild);
                log.info("套餐子品"+a+"添加成功---------------------------");
            }
        JSONObject shopCarId = new JSONObject();
        shopCarId.put("shopCarId", farId);
        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        json.put("data", shopCarId);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value = "/getCarList", method = RequestMethod.POST)
    public void getCarList(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getCarList");
        if (!result.isSuccess()) {
            return;
        }

        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);
        //BrandSetting brand = brandSettingService.selectByBrandId(brandId);

        String shopId = queryParams.get("shopId").toString();
        String userId = queryParams.get("userId").toString();
        String distributionModeId = queryParams.get("distributionModeId").toString();
        List<ShopCart> shopCartList = shopCartService.listUserShopCart(userId, shopId, Integer.parseInt(distributionModeId));

        JSONArray articleList = new JSONArray();
        for (ShopCart sc : shopCartList){
            JSONObject shopcart = new JSONObject();
            //Article article = articleService.selectFullById(sc.getArticleId(),"wechat");
            shopcart.put("shopCarId", sc.getId());
            shopcart.put("articleId", sc.getArticleId());
            shopcart.put("pid", sc.getPid());
            //shopcart.put("name", article.getName());
            //shopcart.put("price", article.getPrice());
            shopcart.put("count", sc.getNumber());
            shopcart.put("shopType", sc.getShopType());
            articleList.put(shopcart);
        }

        JSONObject json = new JSONObject();
        json.put("msg", "请求成功");
        json.put("code", DictConstants.SUCCESS);
        json.put("data", articleList);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value = "/delMealArticle", method = RequestMethod.POST)
    public void delMealArticle(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "delMealArticle");
        if (!result.isSuccess()) {
            return;
        }
        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);
        String shopCarId = queryParams.get("shopCarId").toString();

        shopCartService.delMealArticle(shopCarId);

        JSONObject json = new JSONObject();
        json.put("msg", "请求成功");
        json.put("code", DictConstants.SUCCESS);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value = "/clearCarList", method = RequestMethod.POST)
    public void clearCarList(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "clearCarList");
        if (!result.isSuccess()) {
            return;
        }

        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);

        String shopId = queryParams.get("shopId").toString();
        String userId = queryParams.get("userId").toString();

        shopCartService.clearShopCartGeekPos(userId, shopId);
        JSONObject json = new JSONObject();
        json.put("msg", "请求成功");
        json.put("code", DictConstants.SUCCESS);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}
