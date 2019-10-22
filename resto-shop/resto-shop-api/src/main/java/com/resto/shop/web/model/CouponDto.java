package com.resto.shop.web.model;

import com.resto.brand.web.model.ShopDetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KONATA on 2016/12/21.
 */
public class CouponDto implements Serializable {
    List<ShopDetail> shopDetails;

    List<Coupon> coupons;

    public List<ShopDetail> getShopDetails() {
        return shopDetails;
    }

    public void setShopDetails(List<ShopDetail> shopDetails) {
        this.shopDetails = shopDetails;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }
}
