package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据券码查询详细信息
 * <p>
 * Created by cuibaosen on 16/8/16.
 */
public class CipCaterCouponQueryRequest extends CipCaterStringPairRequest {
    private String couponCode;

    public CipCaterCouponQueryRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/tuangou/coupon/queryById";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("couponCode", couponCode);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return couponCode == null || couponCode.trim().isEmpty();
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
