package com.resto.shop.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.NewCustomCoupon;

public interface NewCustomCouponMapper  extends GenericDao<NewCustomCoupon,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(NewCustomCoupon record);

    int insertSelective(NewCustomCoupon record);

    NewCustomCoupon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewCustomCoupon record);

    int updateByPrimaryKey(NewCustomCoupon record);

    List<NewCustomCoupon> selectListByBrandId(@Param("brandId")String brandId,@Param("shopId") String shopId);

    List<NewCustomCoupon> selectProductCouponListByBrandId(@Param("brandId")String brandId,@Param("shopId") String shopId);

    List<NewCustomCoupon> selectListByCouponType(@Param("brandId")String brandId,@Param("couponType")Integer couponType);

    List<NewCustomCoupon> selectListByBrandIdAndIsActive(@Param("brandId")String brandId,@Param("couponType")Integer couponType);

    List<NewCustomCoupon> selectListByShopId(@Param("shopId")String shopId);

    List<NewCustomCoupon> selectListByShopIdAndIsActive(@Param("shopId") String shopId, @Param("couponType") Integer couponType);

    List<NewCustomCoupon> selectListByCouponTypeAndShopId(@Param("shopId") String shopId,@Param("couponType") Integer couponType);

    List<NewCustomCoupon> selectListByCouponTypeAndBrandId(@Param("brandId") String brandId,@Param("couponType") Integer couponType);

    List<NewCustomCoupon> selectBirthCoupon();

    List<NewCustomCoupon> selectRealTimeCoupon(Map<String, Object> selectMap);

    List<NewCustomCoupon> selectConsumptionRebateCoupon(@Param("shopId") String shopId);
}
