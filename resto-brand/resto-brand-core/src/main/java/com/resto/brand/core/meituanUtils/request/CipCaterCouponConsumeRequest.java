package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterCouponConsumeRequest extends CipCaterStringPairRequest {
    private String couponCode;
    private Long eId;
    private String eName;
    private Long eOrderId;
    private Integer count;

    public CipCaterCouponConsumeRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/tuangou/coupon/consume";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("couponCode", couponCode);
        params.put("eId", eId.toString());
        params.put("eName", eName);
        params.put("count", count.toString());
        if (eOrderId != null) {
            params.put("eOrderId", eOrderId.toString());
        }
        return params;
    }

    @Override
    public boolean paramsAbsent() {
        return couponCode == null || couponCode.trim().isEmpty() || eId == null || eName == null || eName.trim().isEmpty() || count == null;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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

    public Long geteOrderId() {
        return eOrderId;
    }

    public void seteOrderId(Long eOrderId) {
        this.eOrderId = eOrderId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
