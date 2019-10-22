package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterCouponConsumptionPrepareRequest extends CipCaterStringPairRequest {
    private String couponCode;

    public CipCaterCouponConsumptionPrepareRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/tuangou/coupon/prepare";
        this.requestMethod = RequestMethod.POST;
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
