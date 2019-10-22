package com.resto.shop.web.service;

import java.util.List;
import java.util.Map;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.WechatConfig;
import com.resto.shop.web.model.Coupon;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.model.NewCustomCoupon;

public interface NewCustomCouponService extends GenericService<NewCustomCoupon, Long> {

    int insertNewCustomCoupon(NewCustomCoupon newCustomCoupon);

    List<NewCustomCoupon> selectListByBrandId(String currentBrandId,String shopId);

    List<Coupon> giftCoupon(com.resto.api.customer.entity.Customer cus, Integer couponType, String shopId);

    List<NewCustomCoupon> selectListByCouponType(String currentBrandId,Integer couponType,String shopId);

    List<NewCustomCoupon>  selectListByCouponTypeAndShopId(String shopId,Integer couponType);

    public List<NewCustomCoupon> selectProductCouponListByBrandId(String currentBrandId, String shopId);

    /**
     * 查询所有属于该店铺的
     * 优惠券设置
     * @param currentShopId
     * @return
     */
    List<NewCustomCoupon> selectListShopId(String currentShopId);
    
    
//    void timedPush(long BeginDate,long EndDate,String customerId,String name,BigDecimal price,ShopDetail shopDetail,Map<String,String> logMap);

    /**
     * 查询生日优惠券
     * @return
     */
    List<NewCustomCoupon> selectBirthCoupon();

    void insertBirthCoupon(NewCustomCoupon customCoupon, Customer customer, Brand brand, WechatConfig config, BrandSetting setting);

    List<NewCustomCoupon> selectRealTimeCoupon(Map<String, Object> selectMap);

    /**
     * 查询消费返利优惠券
     * @return
     */
    List<NewCustomCoupon> selectConsumptionRebateCoupon(String shopId);
}
