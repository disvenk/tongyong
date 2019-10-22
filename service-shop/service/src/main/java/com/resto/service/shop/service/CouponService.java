package com.resto.service.shop.service;

import com.resto.api.brand.dto.CouponDto;
import com.resto.conf.util.ApplicationUtils;
import com.resto.conf.util.DateUtil;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.CouponSource;
import com.resto.service.shop.constant.PayMode;
import com.resto.service.shop.constant.TimeCons;
import com.resto.service.shop.entity.*;
import com.resto.service.shop.exception.AppException;
import com.resto.service.shop.mapper.CouponMapper;
import com.resto.service.shop.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CouponService extends BaseService<Coupon, String>{

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderPaymentItemService orderPaymentItemService;

    @Override
    public BaseDao<Coupon, String> getDao() {
        return couponMapper;
    }

    public List<Coupon> listCoupon(Coupon coupon, String brandId, String shopId) {
        List<Coupon> list = new ArrayList<>();
        //查询出品牌的
        coupon.setBrandId(brandId);
        coupon.setShopDetailId(shopId);

        List<Coupon> brandList = couponMapper.listCouponByBrandId(coupon);
        //查询出店铺的专属优惠券
        List<Coupon> shopList = couponMapper.listCouponByShopId(coupon);
        list.addAll(brandList);
        list.addAll(shopList);
        return list;
    }

    public List<Coupon> listCouponUsed(Coupon coupon) {
        List<Coupon> list=couponMapper.listCouponUsed(coupon);
        return list;
    }

    public void insertCoupon(Coupon coupon) {
        coupon.setId(ApplicationUtils.randomUUID());
        coupon.setUsingTime(null);
        coupon.setIsUsed(false);
        couponMapper.insertSelective(coupon);
    }

    public Coupon useCoupon(BigDecimal totalMoney, Order order) throws AppException {

        Coupon coupon = selectById(order.getUseCoupon());
        //判断优惠券是否已使用
        if(coupon.getIsUsed()){
            throw new AppException(AppException.COUPON_IS_USED);
        }
        //判断优惠券有效期
        Date beginDate = DateUtil.getDateBegin(coupon.getBeginDate());
        Date endDate = DateUtil.getDateEnd(coupon.getEndDate());
        Date now = new Date();
        if(beginDate.getTime()>now.getTime()||endDate.getTime()<now.getTime()){
            throw new AppException(AppException.COUPON_IS_EXPIRE);
        }
        //判断优惠券使用时间段
        int beginMin = DateUtil.getMinOfDay(coupon.getBeginTime());
        int endMin = DateUtil.getMinOfDay(coupon.getEndTime());
        int nowMin = DateUtil.getMinOfDay(now);
        if(beginMin>nowMin||endMin<nowMin){
            throw new AppException(AppException.COUPON_TIME_ERR);
        }

        //判断优惠券使用类型是否0 或者是否等于订单类型
        if(coupon.getDistributionModeId()!=0&&coupon.getDistributionModeId()!=order.getDistributionModeId()){
            if(coupon.getDistributionModeId()!=1&&order.getDistributionModeId()!=3){
                throw new AppException(AppException.COUPON_MODE_ERR);
            }
        }
        //判断优惠券订单金额是否大于优惠券可用金额
        if(totalMoney.compareTo(totalMoney)<0){
            throw new AppException(AppException.COUPON_MIN_AMOUNT_ERR);
        }
        //判断是否使用了余额 并且 当前优惠券可否使用余额
        if(order.isUseAccount()&&!coupon.getUseWithAccount()){
            throw new AppException(AppException.COUPON_NOT_USEACCOUNT);
        }

        //判断是否是店铺专属的优惠券  优惠券有店铺id并且店铺id和订单中的店铺id不一样就会抛异常
        if(coupon.getShopDetailId()!=null &&!order.getShopDetailId().equals(coupon.getShopDetailId())){
            throw new AppException(AppException.COUPON_IS_SHOP);
        }
        coupon.setIsUsed(true);
        coupon.setUsingTime(new Date());
        this.update(coupon);
        return coupon;
    }

    public void refundCoupon(String id) {
        Coupon coupon = selectById(id);
        coupon.setIsUsed(false);
        coupon.setRemark("退还优惠券");
        update(coupon);
    }

    public List<Coupon> listCouponByStatus(String status, String customerId,String brandId,String shopId) {
        String IS_EXPIRE = null;
        String NOT_EXPIRE = null;
        if("2".equals(status)){
            IS_EXPIRE = "IS_EXPIRE";
            status = null;
        }
        if("0".equals(status)){
            NOT_EXPIRE = "NOT_EXPIRE";
        }
        List<Coupon> list = new ArrayList<>();
        //查询品牌优惠券
        List<Coupon> brandList = couponMapper.listCouponByStatusAndBrandId(status, IS_EXPIRE, NOT_EXPIRE, customerId,brandId);
        //当前店铺专属的优惠券
        List<Coupon> shopList = couponMapper.listCouponByStatusAndShopId(status, IS_EXPIRE, NOT_EXPIRE, customerId,shopId);
        list.addAll(brandList);
        list.addAll(shopList);
        return list;
    }

    public void useCouponById(String orderId, String id) {
        Coupon coupon = selectById(id);
        coupon.setIsUsed(true);
        coupon.setRemark("后付款消费优惠券");
        coupon.setUsingTime(new Date());
        update(coupon);


        Order order = orderMapper.selectByPrimaryKey(orderId);
//        order.setPaymentAmount(order.getPaymentAmount().subtract(coupon.getValue()));
//        orderService.update(order);

        OrderPaymentItem item = new OrderPaymentItem();
        item.setId(ApplicationUtils.randomUUID());
        item.setOrderId(orderId);
        item.setPaymentModeId(PayMode.COUPON_PAY);
        item.setPayTime(new Date());
        item.setPayValue(coupon.getValue());
        item.setRemark("优惠券支付:" + item.getPayValue());
        item.setResultData(coupon.getId());
        orderPaymentItemService.insert(item);
    }

    public List<Coupon> getListByCustomerId(String customerId) {
        return couponMapper.getListByCustomerId(customerId);
    }

    public List<CouponDto> selectCouponDto(Map<String, Object> selectMap) {
        return couponMapper.selectCouponDto(selectMap);
    }

    public List<Coupon> usedCouponBeforeByOrderId(String orderId) {
        return couponMapper.usedCouponBeforeByOrderId(orderId);
    }

    public List<Coupon> addRealTimeCoupon(List<NewCustomCoupon> newCustomCoupons, Customer customer) {
        List<Coupon> coupons = new ArrayList<>();
        try{
            String realTimeCouponIds = "";
            for (NewCustomCoupon customCoupon : newCustomCoupons){
                Coupon coupon = new Coupon();
                Date beginDate = new Date();

                //判断优惠券有效日期类型
                if (customCoupon.getTimeConsType().equals(TimeCons.MODELA)){ //按天
                    coupon.setBeginDate(beginDate);
                    coupon.setEndDate(DateUtil.getAfterDayDate(beginDate,customCoupon.getCouponValiday()));
                }else if (customCoupon.getTimeConsType()==TimeCons.MODELB){ //按日期
                    coupon.setBeginDate(customCoupon.getBeginDateTime());
                    coupon.setEndDate(customCoupon.getEndDateTime());
                }

                //判断是店铺优惠券还是品牌优惠券
                if(customCoupon.getIsBrand() == 1 && customCoupon.getBrandId() != null){
                    coupon.setBrandId(customCoupon.getBrandId());
                }else{
                    coupon.setShopDetailId(customCoupon.getShopDetailId());
                }
                //如果没有设置优惠券推送时间，那么，默认为3天
                if(customCoupon.getPushDay() != null){
                    coupon.setPushDay(customCoupon.getPushDay());
                }else{
                    coupon.setPushDay(3);
                }
                coupon.setName(customCoupon.getCouponName());
                coupon.setValue(customCoupon.getCouponValue());
                coupon.setMinAmount(customCoupon.getCouponMinMoney());
                coupon.setCouponType(customCoupon.getCouponType());
                coupon.setBeginTime(customCoupon.getBeginTime());
                coupon.setEndTime(customCoupon.getEndTime());
                coupon.setUseWithAccount(customCoupon.getUseWithAccount());
                coupon.setDistributionModeId(customCoupon.getDistributionModeId());
                coupon.setCouponSource(CouponSource.getCouponSourceByType(coupon.getCouponType()));
                coupon.setCustomerId(customer.getId());
                coupon.setRecommendDelayTime(0);
                coupon.setNewCustomCouponId(customCoupon.getId());
                for(int i = 0; i < customCoupon.getCouponNumber(); i++){
                    insertCoupon(coupon);
                    coupons.add(coupon);
                }
                if (coupon.getCouponSource().equalsIgnoreCase(CouponSource.REAL_TIME_COUPON)) {
                    realTimeCouponIds = realTimeCouponIds.concat(customCoupon.getId().toString()).concat(",");
                }
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(realTimeCouponIds)){
                Customer newCustomer = new Customer();
                newCustomer.setId(customer.getId());
                //得到用户领取过的实时优惠券Id
                realTimeCouponIds = realTimeCouponIds.substring(0,realTimeCouponIds.length() - 1);
                if (customer.getRealTimeCouponIds() != null){
                    realTimeCouponIds = customer.getRealTimeCouponIds().concat(",").concat(realTimeCouponIds);
                }
                newCustomer.setRealTimeCouponIds(realTimeCouponIds);
                customerService.update(newCustomer);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("发放实时优惠券出错！");
            return new ArrayList<>();
        }
        return coupons;
    }

    public Coupon selectPosPayOrderCanUseCoupon(Map<String, Object> selectMap) {
        return couponMapper.selectPosPayOrderCanUseCoupon(selectMap);
    }

}
