package com.resto.shop.web.dao;

import java.util.List;
import java.util.Map;

import com.resto.shop.web.model.CouponShopArticles;
import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.Coupon;

public interface CouponMapper  extends GenericDao<Coupon,String> {
    int deleteByPrimaryKey(String id);

    int insert(Coupon record);

    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);

    List<Coupon> listCoupon(Coupon coupon);
    
    /**
     * 根据状态查询用户的品牌优惠券
     * @param status
     * @param IS_EXPIRE
     * @param NOT_EXPIRE
     * @param customerId
     * @return
     */
    List<Coupon> listCouponByStatusAndBrandId(@Param("status") String status,@Param("isexpire") String IS_EXPIRE,@Param("notexpire") String NOT_EXPIRE,@Param("customerId") String customerId,@Param("brandId")String brandId);


    /**
     * 查询该店铺的专属优惠券
     * @param status
     * @param IS_EXPIRE
     * @param NOT_EXPIRE
     * @param customerId
     * @return
     */
    List<Coupon> listCouponByStatusAndShopId(@Param("status") String status,@Param("isexpire") String IS_EXPIRE,@Param("notexpire") String NOT_EXPIRE,@Param("customerId") String customerId,@Param("shopId")String shopId);

    /**
     * 查询品牌优惠券
     * @param coupon
     * @return
     */
    List<Coupon> listCouponByBrandId(Coupon coupon);


    /**
     * 查询店铺优惠券
     * @param coupon
     * @return
     */
    List<Coupon> listProductByShopId(Coupon coupon);

    /**
     * 查询店铺优惠券
     * @param coupon
     * @return
     */
    List<Coupon> listCouponByShopId(Coupon coupon);

    /**
     * 查询用户已使用的优惠券
     * @param coupon
     * @return
     */
    List<Coupon> listCouponUsed(Coupon coupon);


    /**
     * 根据用户得到用户的优惠券
     * @param customerId
     * @return
     */
    List<Coupon> getListByCustomerId(String customerId);

    List<Coupon> usedCouponBeforeByOrderId(String orderId);

    Coupon selectPosPayOrderCanUseCoupon(Map<String, Object> selectMap);

    List<Coupon> getCouponByShopId(@Param("shopId") String shopId,@Param("time") Integer day,@Param("type") Integer type);

    void insertCouponBatch(@Param("list") List<Coupon> list);

    Coupon selectLastTimeRebate(@Param("customerId") String customerId);

    List<CouponShopArticles> selectUnionArticleById(@Param("id") Long id,@Param("shopId")String shopId);
}
