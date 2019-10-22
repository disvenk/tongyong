package com.resto.service.shop.constant;

/**
 * @name CouponSource
 * @description 优惠券来源常量类
 * @author Diamond
 * @date 2015年10月8日 
 */
public class CouponSource {
        public final static String WECHAT_PROMOTION = "1";   //微信被推广获得红包
        public final static String WECHAT_PROMOTION_REBATE = "2"; //微信推广 被推广人使用后，推广人获得红包
        public static final String NEW_CUSTOMER_COUPON = "3"; //新用户绑定手机赠送红包
        public static final String BIRTHDAY_COUPON = "4"; //用户生日赠送优惠券
        public static final String SHARE_COUPON = "5"; //分享赠送优惠券
        public static final String REAL_TIME_COUPON = "6"; //实时优惠券
        public static final String DRAIN_AROUSAL_COUPON = "7"; //流失唤醒优惠券

        public static final String getCouponSourceByType(Integer couponType){
                switch (couponType) {
                        case 0:
                                return NEW_CUSTOMER_COUPON;
                        case 1:
                                return WECHAT_PROMOTION_REBATE;
                        case 2:
                                return BIRTHDAY_COUPON;
                        case 3:
                                return SHARE_COUPON;
                        case 4:
                                return REAL_TIME_COUPON;
                        case 5:
                                return DRAIN_AROUSAL_COUPON;
                        default:
                                return "-1";
                }
        }
}