 package com.resto.shop.web.controller.business;

 import com.alibaba.fastjson.JSONArray;
 import com.resto.brand.core.entity.JSONResult;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.BrandSetting;
 import com.resto.brand.web.model.ShopDetail;
 import com.resto.brand.web.service.BrandSettingService;
 import com.resto.brand.web.service.ShopDetailService;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.form.UnionArticleForm;
 import com.resto.shop.web.model.CouponShopArticles;
 import com.resto.shop.web.service.CouponShopArticlesService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.validation.Valid;
 import java.util.List;

 @Controller
 @RequestMapping("couponshoparticles")
 public class CouponShopArticlesController extends GenericController{

     @Resource
     CouponShopArticlesService couponshoparticlesService;

     @Autowired
     BrandSettingService brandSettingService;

     @Autowired
     ShopDetailService shopDetailService;


     @RequestMapping("/goToGrant")
     public String goToGrant(){
         return "couponshoparticles/grantCoupon";
     }

     @RequestMapping("/list")
     public String list(){
         BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
         if(brandSetting.getOpenProductCoupon() == 0){
             return "couponshoparticles/noJurisdiction";
         }else{
             return "couponshoparticles/list";
         }
     }

     @RequestMapping("/list_all")
     @ResponseBody
     public List<CouponShopArticles> listData(){
         return couponshoparticlesService.selectList();
     }

     @RequestMapping("list_one")
     @ResponseBody
     public Result list_one(Integer id){
         CouponShopArticles couponshoparticles = couponshoparticlesService.selectById(id);
         return getSuccessResult(couponshoparticles);
     }

     @RequestMapping("create")
     @ResponseBody
     public Result create(@Valid CouponShopArticles couponshoparticles){
         couponshoparticlesService.insert(couponshoparticles);
         return Result.getSuccess();
     }

     @RequestMapping("modify")
     @ResponseBody
     public Result modify(@Valid CouponShopArticles couponshoparticles){
         couponshoparticlesService.update(couponshoparticles);
         return Result.getSuccess();
     }

     @RequestMapping("delete")
     @ResponseBody
     public Result delete(Integer id){
         couponshoparticlesService.delete(id);
         return Result.getSuccess();
     }

     @RequestMapping("selectUnionArticle")
     @ResponseBody
     public Result selectUnionArticle(Long couponId){
         JSONArray jsonArray = couponshoparticlesService.selectUnionArticle(couponId,getCurrentBrandId());
         JSONResult jsonResult = new JSONResult();
         jsonResult.setData(jsonArray);
         jsonResult.setSuccess(true);
         return jsonResult;
     }

     @RequestMapping("selectShop")
     @ResponseBody
     public Result selectShop(Long couponId){
         JSONArray shopDetails = couponshoparticlesService.selectShop(couponId, getCurrentBrandId(), getCurrentShopId());
         JSONResult jsonResult = new JSONResult();
         jsonResult.setData(shopDetails);
         jsonResult.setSuccess(true);
         return jsonResult;
     }

     @RequestMapping("selectUnionedArticle")
     @ResponseBody
     public Result selectUnionedArticle(Long couponId){
        JSONArray shopDetails = couponshoparticlesService.selectUnionedArticle(couponId, getCurrentBrandId(), getCurrentShopId());
         JSONResult jsonResult = new JSONResult();
         jsonResult.setData(shopDetails);
         jsonResult.setSuccess(true);
         return jsonResult;
     }

     @RequestMapping("UnionArticle")
     @ResponseBody
     public Result UnionArticle(@RequestBody UnionArticleForm unionArticleForm){
        couponshoparticlesService.UnionArticle(unionArticleForm,getCurrentShopId(),getCurrentBrandId());
         return Result.getSuccess();
     }

     @RequestMapping("grantCoupon")
     @ResponseBody
     public Result grantCoupon(String phone,Long couponId){
         if(phone==null || "".equals(phone)){
             return new Result("请输入手机号",false);
         }
         return couponshoparticlesService.grantCouponId(phone,couponId );
     }

     @RequestMapping("selectShopList")
     @ResponseBody
     public List<ShopDetail> getShopList(){
         return shopDetailService.selectByBrandId(getCurrentBrandId());
     }

 }
