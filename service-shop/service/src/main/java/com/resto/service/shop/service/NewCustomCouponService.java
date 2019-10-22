package com.resto.service.shop.service;

import com.resto.api.brand.define.api.BrandApiShopDetail;
import com.resto.api.brand.dto.ShopDetailDto;
import com.resto.conf.util.DateUtil;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.CouponSource;
import com.resto.service.shop.constant.TimeCons;
import com.resto.service.shop.entity.Coupon;
import com.resto.service.shop.entity.Customer;
import com.resto.service.shop.entity.NewCustomCoupon;
import com.resto.service.shop.mapper.NewCustomCouponMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NewCustomCouponService extends BaseService<NewCustomCoupon, Long> {

    @Autowired
    private NewCustomCouponMapper newcustomcouponMapper;
    @Autowired
    private BrandApiShopDetail shopDetailService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CouponService couponService;

//    @Value("#{propertyConfigurer['orderMsg']}")
    public static String orderMsg;

    @Override
    public BaseDao<NewCustomCoupon, Long> getDao() {
        return newcustomcouponMapper;
    }

    public List<NewCustomCoupon> selectListByBrandId(String currentBrandId, String shopId) {
        List<NewCustomCoupon> list = newcustomcouponMapper.selectListByBrandId(currentBrandId, shopId);
        //查询品牌下所有的店铺
        List<ShopDetailDto> shopDetailList = shopDetailService.selectByBrandId(currentBrandId);

        if (!list.isEmpty()) {
            for (NewCustomCoupon newcustomcoupon : list) {
                for (ShopDetailDto shopDetail : shopDetailList) {
                    if (shopDetail.getId().equals(newcustomcoupon.getShopDetailId())) {
                        newcustomcoupon.setShopName(shopDetail.getName());
                    }
                }
            }
        }

        return list;
    }

    public List<Coupon> giftCoupon(Customer cus, Integer couponType, String shopId) {
        logger.info("brandId:"+cus.getBrandId()+"couponType:"+couponType+"shopId:"+shopId);
        List<Coupon> coupons = new ArrayList<>();
        try {
            String shareCoupinIds = "";
            //根据 店铺id 查询该店铺的优惠券配置 查询已经启用的优惠券
            List<NewCustomCoupon> couponConfigs = newcustomcouponMapper.selectListByBrandIdAndIsActive(cus.getBrandId(), couponType);
            //如果没有找到 对应类型的优惠券，则显示通用的优惠券。用于兼容老版本红包没有设置 优惠券类型问题
            if (couponConfigs == null || couponConfigs.size() == 0) {
                couponType = -1;
                couponConfigs = newcustomcouponMapper.selectListByBrandIdAndIsActive(cus.getBrandId(), couponType);
            }
            ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(shopId);
            //根据优惠券配置，添加对应数量的优惠券
            Date beginDate = new Date();
            for (NewCustomCoupon cfg : couponConfigs) {
                if (couponType.equals(3)) {
                    boolean flg = true;
                    if (StringUtils.isNotBlank(cus.getShareCouponIds())) {
                        String[] ids = cus.getShareCouponIds().split(",");
                        for (String id : ids) {
                            if (cfg.getId().equals(Long.valueOf(id))) {
                                flg = false;
                                break;
                            }
                        }
                    }
                    if (!flg) {
                        continue;
                    }
                    addCoupon(cfg, shopId, couponType, cus, beginDate, shopDetail, coupons);
                    shareCoupinIds = shareCoupinIds.concat(cfg.getId().toString()).concat(",");
                } else {
                    addCoupon(cfg, shopId, couponType, cus, beginDate, shopDetail, coupons);
                }
            }
            if (StringUtils.isNotBlank(shareCoupinIds)) {
                Customer customer = new Customer();
                customer.setId(cus.getId());
                //得到领取到的分享优惠券Id
                shareCoupinIds = shareCoupinIds.substring(0, shareCoupinIds.length() - 1);
                if (StringUtils.isNotBlank(customer.getShareCouponIds())) {
                    shareCoupinIds = cus.getShareCouponIds().concat(",").concat(shareCoupinIds);
                }
                customer.setShareCouponIds(shareCoupinIds);
                customerService.update(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发放优惠券出错！"+e.getMessage());
            return new ArrayList<>();
        }
        return coupons;
    }

    public void addCoupon(NewCustomCoupon cfg, String shopId, Integer couponType, Customer cus, Date beginDate, ShopDetailDto shopDetail, List<Coupon> coupons) {
        //如果是品牌优惠券设置或者是当前店铺的优惠券设置
        try{
            if (cfg.getIsBrand() == 1 || shopId.equals(cfg.getShopDetailId())) {
                Coupon coupon = new Coupon();
                coupon.setName(cfg.getCouponName());
                coupon.setValue(cfg.getCouponValue());
                coupon.setMinAmount(cfg.getCouponMinMoney());
                coupon.setCouponType(couponType);
                coupon.setBeginTime(cfg.getBeginTime());
                coupon.setEndTime(cfg.getEndTime());
                coupon.setUseWithAccount(cfg.getUseWithAccount());
                coupon.setDistributionModeId(cfg.getDistributionModeId());
                coupon.setCouponSource(CouponSource.NEW_CUSTOMER_COUPON);
                if (couponType.equals(3)) {
                    coupon.setCouponSource(CouponSource.SHARE_COUPON);
                }
                coupon.setCustomerId(cus.getId());
                coupon.setPushDay(cfg.getPushDay());
                coupon.setRecommendDelayTime(cfg.getRecommendDelayTime() * 3600);
                coupon.setNewCustomCouponId(cfg.getId());
                //如果是店铺专有的优惠券设置 设置该优惠券的shopId表示只有这个店铺可以用
                if (cfg.getShopDetailId() != null && shopId.equals(cfg.getShopDetailId())) {
                    coupon.setShopDetailId(cfg.getShopDetailId());
                }
                //如果是品牌的专有优惠券
                if (cfg.getIsBrand() == 1 && cfg.getBrandId() != null) {
                    coupon.setBrandId(cfg.getBrandId());
                }
                //优惠券时间选择的类型分配时间
                if (cfg.getTimeConsType() == TimeCons.MODELA) {
                    coupon.setBeginDate(beginDate);
                    coupon.setEndDate(DateUtil.getAfterDayDate(beginDate, cfg.getCouponValiday()));
                } else if (cfg.getTimeConsType() == TimeCons.MODELB) {
                    coupon.setBeginDate(cfg.getBeginDateTime());
                    coupon.setEndDate(cfg.getEndDateTime());
                }
                //如果没有设置优惠券推送时间，那么，默认为3天
                if (cfg.getPushDay() == null) {
                    coupon.setPushDay(3);
                }
                for (int i = 0; i < cfg.getCouponNumber(); i++) {
                    couponService.insertCoupon(coupon);
                    coupons.add(coupon);
                }
                long begin = coupon.getBeginDate().getTime();
                long end = coupon.getEndDate().getTime();
//            timedPush(begin, end, coupon.getCustomerId(), coupon.getName(), coupon.getValue(), shopDetail, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public List<NewCustomCoupon> selectListByCouponType(String brandId, Integer couponType, String shopId) {
        List<NewCustomCoupon> list = new ArrayList<>();
        //查询品牌设置的优惠券
        List<NewCustomCoupon> brandList = newcustomcouponMapper.selectListByCouponTypeAndBrandId(brandId, couponType);
        //查询店铺设置的优惠券
        List<NewCustomCoupon> shopList = newcustomcouponMapper.selectListByCouponTypeAndShopId(shopId, couponType);
        list.addAll(brandList);
        list.addAll(shopList);
        //如果没有找到 对应类型的优惠券，则显示通用的优惠券。用于兼容老版本红包没有设置 优惠券类型问题
        if (list == null || list.size() == 0) {
            list = newcustomcouponMapper.selectListByCouponType(brandId, -1);
        }
        return list;
    }

    public List<NewCustomCoupon> selectBirthCoupon() {
        return newcustomcouponMapper.selectBirthCoupon();
    }

    public List<NewCustomCoupon> selectRealTimeCoupon(Map<String, Object> selectMap) {
        return newcustomcouponMapper.selectRealTimeCoupon(selectMap);
    }
}
