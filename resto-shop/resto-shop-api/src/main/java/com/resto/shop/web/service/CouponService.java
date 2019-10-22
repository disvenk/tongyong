package com.resto.shop.web.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.CouponDto;
import com.resto.brand.web.dto.CouponInfoDto;
import com.resto.brand.web.dto.UseCouponOrderDto;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.Coupon;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.model.NewCustomCoupon;
import com.resto.shop.web.model.Order;
import org.json.JSONArray;

public interface CouponService extends GenericService<Coupon, String> {

	List<Coupon> listCoupon(Coupon coupon,String brandId,String shopId);

	void insertCoupon(Coupon coupon);

	Coupon useCoupon(BigDecimal totalMoney, Order order) throws AppException;

	void refundCoupon(String id);

    void refundArticleCoupon(String id);

	/**
	 * 根据 状态 查询 优惠劵列表
	 * @param status
	 * @return
	 */
	List<Coupon> listCouponByStatus(String status, String customerId, String brandId, String shopId);

	void useCouponById(String orderId,String id);

	List<Coupon> getListByCustomerId(String customerId);

	List<CouponDto> selectCouponDto(Map<String, Object> selectMap);

	List<Coupon> usedCouponBeforeByOrderId(String orderId);

	List<Coupon> addRealTimeCoupon(List<NewCustomCoupon> newCustomCoupons, com.resto.api.customer.entity.Customer  customer);

	Coupon selectPosPayOrderCanUseCoupon(Map<String, Object> selectMap);

	List<Coupon> getCouponByShopId(String shopId,Integer day,Integer type);

	List<Coupon> listCouponUsed(Coupon coupon);

	void addCoupon(NewCustomCoupon newCustomCoupon, Customer customer);

	void addCouponBatch(List<Customer> customerList,NewCustomCoupon newCustomCoupon,String brandId) throws SQLException;

	/**
	 * 查询到用户上一次领取到的消费返利优惠券
	 * @param customerId
	 * @return
	 */
	Coupon selectLastTimeRebate(String customerId);

	List<CouponInfoDto> selectCouponInfoList(String beginDate, String endDate, String newCustomCouponId);

	List<UseCouponOrderDto> selectUseCouponOrders(List<String> couponIds, Integer isNewCustomId, String useBeginDate, String useEndDate);

	void addConsumptionRebateCoupon(Order order);
}
