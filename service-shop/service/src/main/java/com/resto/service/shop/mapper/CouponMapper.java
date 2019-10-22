package com.resto.service.shop.mapper;

import com.resto.api.brand.dto.CouponDto;
import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.Coupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CouponMapper extends BaseDao<Coupon,String> {
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
    List<Coupon> listCouponByStatusAndBrandId(@Param("status") String status, @Param("IS_EXPIRE") String IS_EXPIRE, @Param("NOT_EXPIRE") String NOT_EXPIRE, @Param("customerId") String customerId, @Param("brandId") String brandId);


    /**
     * 查询该店铺的专属优惠券
     * @param status
     * @param IS_EXPIRE
     * @param NOT_EXPIRE
     * @param customerId
     * @return
     */
    List<Coupon> listCouponByStatusAndShopId(@Param("status") String status, @Param("IS_EXPIRE") String IS_EXPIRE, @Param("NOT_EXPIRE") String NOT_EXPIRE, @Param("customerId") String customerId, @Param("shopId") String shopId);

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

    List<CouponDto> selectCouponDto(Map<String, Object> selectMap);

    List<Coupon> usedCouponBeforeByOrderId(String orderId);

    Coupon selectPosPayOrderCanUseCoupon(Map<String, Object> selectMap);

    List<Coupon> getCouponByShopId(@Param("shopId") String shopId, @Param("time") Integer day, @Param("type") Integer type);

    void insertCouponBatch(@Param("list") List<Coupon> list);
}
