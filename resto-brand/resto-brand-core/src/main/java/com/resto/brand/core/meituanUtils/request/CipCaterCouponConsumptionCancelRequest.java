package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterCouponConsumptionCancelRequest extends CipCaterStringPairRequest {
    private String couponCode;
    private Integer type;
    private Long eId;
    private String eName;

    public CipCaterCouponConsumptionCancelRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/tuangou/coupon/cancel";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("couponCode", couponCode);
            put("type", type.toString());
            put("eId", eId.toString());
            put("eName", eName);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return couponCode == null || couponCode.trim().isEmpty() || type == null || eId == null || eName == null || eName.trim().isEmpty();
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long geteId() {
        return eId;
    }

    public void seteId(Long eId) {
        this.eId = eId;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }
}
