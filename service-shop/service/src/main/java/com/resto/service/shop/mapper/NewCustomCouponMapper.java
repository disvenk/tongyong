package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.NewCustomCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface NewCustomCouponMapper extends BaseDao<NewCustomCoupon,Long> {

    int insert(NewCustomCoupon record);

    int updateByPrimaryKey(NewCustomCoupon record);

    List<NewCustomCoupon> selectListByBrandId(@Param("brandId") String brandId, @Param("shopId") String shopId);

    List<NewCustomCoupon> selectListByCouponType(@Param("brandId") String brandId, @Param("couponType") Integer couponType);

    List<NewCustomCoupon> selectListByBrandIdAndIsActive(@Param("brandId") String brandId, @Param("couponType") Integer couponType);

    List<NewCustomCoupon> selectListByShopId(@Param("shopId") String shopId);

    List<NewCustomCoupon> selectListByShopIdAndIsActive(@Param("shopId") String shopId, @Param("couponType") Integer couponType);

    List<NewCustomCoupon> selectListByCouponTypeAndShopId(@Param("shopId") String shopId, @Param("couponType") Integer couponType);

    List<NewCustomCoupon> selectListByCouponTypeAndBrandId(@Param("brandId") String brandId, @Param("couponType") Integer couponType);

    List<NewCustomCoupon> selectBirthCoupon();

    List<NewCustomCoupon> selectRealTimeCoupon(Map<String, Object> selectMap);
}
