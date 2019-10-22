package com.resto.shop.web.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.SmsLogType;
import com.resto.shop.web.dao.CouponMapper;
import com.resto.shop.web.dao.CouponShopArticlesMapper;
import com.resto.shop.web.dao.CustomerMapper;
import com.resto.shop.web.dao.NewCustomCouponMapper;
import com.resto.shop.web.form.ShopUnionArticle;
import com.resto.shop.web.form.UnionArticleForm;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.CouponShopArticlesService;
import com.resto.shop.web.service.SmsLogService;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Component
@Service
public class CouponShopArticlesServiceImpl extends GenericServiceImpl<CouponShopArticles, Integer> implements CouponShopArticlesService {

    @Resource
    private CouponShopArticlesMapper couponshoparticlesMapper;

    @Resource
    NewCustomCouponMapper newCustomCouponMapper;

    @Autowired
    private ShopDetailService shopDetailService;

    @Autowired
    CustomerMapper CustomerMapper;

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    BrandService brandService;

    @Autowired
    BrandSettingService brandSettingService;

    @Autowired
    WechatConfigService wechatConfigService;

    @Autowired
    WeChatService weChatService;

    @Autowired
    SmsLogService smsLogService;

    @Override
    public GenericDao<CouponShopArticles, Integer> getDao() {
        return couponshoparticlesMapper;
    }

    @Override
    public JSONArray selectUnionArticle(Long couponId,String brandId) {
        NewCustomCoupon newCustomCoupon = newCustomCouponMapper.selectByPrimaryKey(couponId);
        JSONArray jsonArray = new JSONArray();
        //店铺用
        if(newCustomCoupon.getIsBrand()==0){
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(newCustomCoupon.getShopDetailId());
            JSONObject shop = new JSONObject();
            shop.put("id",shopDetail.getId());
            shop.put("pId","0");
            shop.put("name",shopDetail.getName());
            shop.put("page",null);
            List<ArticleFamily> articleFamilies = couponshoparticlesMapper.selectArticleFamily(shopDetail.getId());
            articleFamilies.forEach(m->{
                JSONObject articleFamily = new JSONObject();
                articleFamily.put("id",m.getId());
                articleFamily.put("pId",shopDetail.getId().toString());
                articleFamily.put("name",m.getName());
                articleFamily.put("page",null);
                List<Article> articles = couponshoparticlesMapper.selectArticle(m.getId());
                articles.forEach(o->{

                    JSONObject article = new JSONObject();
                    article.put("id",o.getId());
                    article.put("pId",m.getId().toString());
                    article.put("name",o.getName());
                    article.put("page",shopDetail.getId());
                    jsonArray.add(article);

                });
                if(!articles.isEmpty()){
                    jsonArray.add(articleFamily);
                }
            });
            if(!articleFamilies.isEmpty()){
                jsonArray.add(shop);
            }

        }else {//品牌用
            List<ShopDetail> shopDetails = shopDetailService.selectByBrandId(brandId);
            shopDetails.forEach(n->{
                JSONObject shop = new JSONObject();
                shop.put("id",n.getId());
                shop.put("pId","0");
                shop.put("name",n.getName());
                shop.put("page",null);

                List<ArticleFamily> articleFamilies = couponshoparticlesMapper.selectArticleFamily(n.getId());
                articleFamilies.forEach(m->{
                    JSONObject articleFamily = new JSONObject();
                    articleFamily.put("id",m.getId());
                    articleFamily.put("pId",n.getId().toString());
                    articleFamily.put("name",m.getName());
                    articleFamily.put("page",null);
                    List<Article> articles = couponshoparticlesMapper.selectArticle(m.getId());
                    articles.forEach(o->{
                        JSONObject article = new JSONObject();
                        article.put("id",o.getId());
                        article.put("pId",m.getId().toString());
                        article.put("name",o.getName());
                        article.put("page",n.getId());
                        jsonArray.add(article);
                    });
                    if(!articles.isEmpty()){
                        jsonArray.add(articleFamily);
                    }
                });
                    jsonArray.add(shop);
            });
        }

        return jsonArray;
    }

    @Override
    public JSONArray selectShop(Long couponId, String brandId, String shopId) {
        JSONArray jsonArray = new JSONArray();

        NewCustomCoupon newCustomCoupon = newCustomCouponMapper.selectByPrimaryKey(couponId);
        if(newCustomCoupon.getIsBrand()==0){
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(newCustomCoupon.getShopDetailId());

            JSONObject shop = new JSONObject();
            shop.put("shopName",shopDetail.getName() );
            shop.put("id",shopDetail.getId() );

            JSONArray couponArticles = new JSONArray();
            List<CouponShopArticles> couponShopArticles = couponshoparticlesMapper.selectByShopId(shopDetail.getId(),couponId);
            couponShopArticles.forEach(n->{
                JSONObject couPonArticle = new JSONObject();
                couPonArticle.put("id",n.getArticleId() );
                couPonArticle.put("name",n.getArticleName());
                couPonArticle.put("pId",n.getArticleFamilyId().toString());
                couPonArticle.put("page",n.getShopId());
                couponArticles.add(couPonArticle);
            });
            shop.put("articleList", couponArticles);

                jsonArray.add(shop);
        }else {
           List<ShopDetail> shopDetails = shopDetailService.selectByBrandId(brandId);
            shopDetails.forEach(n->{
                JSONObject shop = new JSONObject();
                shop.put("shopName",n.getName() );
                shop.put("id",n.getId() );
                JSONArray couponArticles = new JSONArray();
                List<CouponShopArticles> couponShopArticles = couponshoparticlesMapper.selectByShopId(n.getId(),couponId);
                couponShopArticles.forEach(m->{
                    JSONObject couPonArticle = new JSONObject();
                    couPonArticle.put("id",m.getArticleId() );
                    couPonArticle.put("name",m.getArticleName());
                    couPonArticle.put("pId",m.getArticleFamilyId().toString());
                    couPonArticle.put("page",m.getShopId());
                    couponArticles.add(couPonArticle);
                });
                shop.put("articleList", couponArticles);
                    jsonArray.add(shop);
            });
        }
        return jsonArray;
    }

    @Override
    public JSONArray selectUnionedArticle(Long couponId, String brandId, String shopId) {
        JSONArray jsonArray = new JSONArray();
        NewCustomCoupon newCustomCoupon = newCustomCouponMapper.selectByPrimaryKey(couponId);
        if(newCustomCoupon.getIsBrand()==0){
            List<CouponShopArticles> couponShopArticles = couponshoparticlesMapper.selectByCouponId(couponId);
            couponShopArticles.forEach(n->{
                JSONObject article = new JSONObject();
                article.put("id",n.getArticleId());
                article.put("pId",n.getArticleFamilyId().toString());
                article.put("name","");
                article.put("page",n.getId());
                jsonArray.add(article);
            });
        }else {
            List<CouponShopArticles>  couponShopArticles = couponshoparticlesMapper.selectByCouponId(couponId);
            couponShopArticles.forEach(n->{
                JSONObject article = new JSONObject();
                article.put("id",n.getArticleId());
                article.put("pId",n.getArticleFamilyId().toString());
                article.put("name","");
                article.put("page",n.getId());
                jsonArray.add(article);
            });
        }
        return jsonArray;
    }

    @Override
    public void UnionArticle(UnionArticleForm unionArticleForm,String shopId,String brandId){
        NewCustomCoupon newCustomCoupon = newCustomCouponMapper.selectByPrimaryKey(unionArticleForm.couponId);
        List<ShopUnionArticle> shopUnionArticle = unionArticleForm.shopUnionArticle;
            couponshoparticlesMapper.delectByCouponId(unionArticleForm.couponId);
        shopUnionArticle.forEach(n->{
            AtomicInteger sort = new AtomicInteger(1);
            n.articleList.forEach(m->{
                CouponShopArticles couponShopArticles = new CouponShopArticles();
                couponShopArticles.setShopId(n.id);
                couponShopArticles.setArticleId(m.id);
                couponShopArticles.setBrandId(brandId);
                couponShopArticles.setArticleFamilyId(m.pId);
                couponShopArticles.setArticleName(m.name);
                couponShopArticles.setCouponId(newCustomCoupon.getId());
                couponShopArticles.setCreateTime(new Date());
                couponShopArticles.setSort(sort.get());
                couponshoparticlesMapper.insert(couponShopArticles);
                sort.getAndIncrement();
            });
        });
    }

    public Result grantCouponId(String phone, Long couponId){
        NewCustomCoupon newCustomCoupon = newCustomCouponMapper.selectByPrimaryKey(couponId);
        Customer customer = CustomerMapper.selectByPhone(phone);
        if(customer==null){
            JSONResult jsonResult = new JSONResult();
            jsonResult.setSuccess(false);
            jsonResult.setMessage("用户不存在");
            return jsonResult;
        }

        Coupon coupon = new Coupon();
        coupon.setId(ApplicationUtils.randomUUID());
        coupon.setName(newCustomCoupon.getCouponName());
        coupon.setValue(newCustomCoupon.getCouponValue());
        coupon.setMinAmount(BigDecimal.ZERO);
        coupon.setDeductionType(newCustomCoupon.getDeductionType());

        //判断优惠券有效日期类型
        if (newCustomCoupon.getTimeConsType()==1){ //按天
            coupon.setBeginDate(new Date());
            coupon.setEndDate(getAfterDayDate(new Date(),newCustomCoupon.getCouponValiday()));
        }else if (newCustomCoupon.getTimeConsType()==2){ //按日期
            coupon.setBeginDate(newCustomCoupon.getBeginDateTime());
            coupon.setEndDate(newCustomCoupon.getEndDateTime());
        }

        if(newCustomCoupon.getShopDetailId() == null){
            coupon.setBrandId(newCustomCoupon.getBrandId());
        }else{
            coupon.setShopDetailId(newCustomCoupon.getShopDetailId());
        }
        coupon.setBeginTime(newCustomCoupon.getBeginTime());
        coupon.setEndTime(newCustomCoupon.getEndTime());
        coupon.setIsUsed(false);
        coupon.setCouponSource("9");
        coupon.setUseWithAccount(true);
        coupon.setDistributionModeId(1);
        coupon.setCustomerId(customer.getId());
        coupon.setAddTime(new Date());
        coupon.setCouponType(7);
        coupon.setNewCustomCouponId(newCustomCoupon.getId());

        //如果没有设置优惠券推送时间，那么，默认为3天
        if(newCustomCoupon.getPushDay() != null){
            coupon.setPushDay(newCustomCoupon.getPushDay());
        }else{
            coupon.setPushDay(3);
        }
        couponMapper.insertSelective(coupon);

        //发送微信推送  语音推送
        pushContent(customer, newCustomCoupon);

        JSONResult jsonResult = new JSONResult();
        jsonResult.setSuccess(true);
        jsonResult.setMessage("发放成功");
        return jsonResult;
    }


    public void pushContent(Customer customer, NewCustomCoupon newCustomCoupon){
        Brand brand = brandService.selectById(customer.getBrandId());
        BrandSetting brandSetting = brandSettingService.selectByBrandId(customer.getBrandId());
        WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
        ShopDetail shopDetail = new ShopDetail();
        if(newCustomCoupon.getShopDetailId() != null){
            shopDetail = shopDetailService.selectByPrimaryKey(newCustomCoupon.getShopDetailId());
        }
        //封装推送文案的信息
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("name", newCustomCoupon.getIsBrand() == 0 ? shopDetail.getName() : brand.getBrandName());
        valueMap.put("couponName", newCustomCoupon.getCouponName());
        valueMap.put("url", newCustomCoupon.getIsBrand() == 0 ? brandSetting.getWechatWelcomeUrl() + "?dialog=myCoupon&qiehuan=qiehuan&subpage=my&shopId=" + newCustomCoupon.getShopDetailId() + ""
                : brandSetting.getWechatWelcomeUrl() + "?dialog=myCoupon&qiehuan=qiehuan&subpage=my");
        String content = "您好，【${name}】送您一份【${couponName}】，请注意查收哦~<a href='${url}'>点击查看详情！~</a>";
        StrSubstitutor substitutor = new StrSubstitutor(valueMap);
        content = substitutor.replace(content);
        if (brandSetting.getWechatPushGiftCoupons().equals(Common.YES)) {
            weChatService.sendCustomerMsg(content, customer.getWechatId(), config.getAppid(), config.getAppsecret());
        }

        //有手机号则发送短信
        if (StringUtils.isNotBlank(customer.getTelephone()) && brandSetting.getSmsPushGiftCoupons().equals(Common.YES)) {
            JSONObject smsParam = new JSONObject();
            smsParam.put("shopName", valueMap.get("name").toString());
            smsParam.put("code", valueMap.get("couponName").toString());
            JSONObject jsonObject = smsLogService.sendMessage(customer.getBrandId(), customer.getLastOrderShop() == null ? "" : customer.getLastOrderShop(),
                    SmsLogType.WAKELOSS, SMSUtils.SIGN, SMSUtils.SMS_PRODUCTION, customer.getTelephone(), smsParam);
            log.info("短信发送结果：" + jsonObject.toJSONString());
        }
    }

    @Override
    public List<CouponShopArticles> selectByCouponIdShopId(Long couponId, String shopId) {
        return couponshoparticlesMapper.selectByCouponIdShopId(couponId, shopId);
    }

    public static Date getAfterDayDate(Date beginDate, int after) {
        Calendar c = Calendar.getInstance();
        c.setTime(beginDate);
        c.add(6, after);
        return c.getTime();
    }
}
