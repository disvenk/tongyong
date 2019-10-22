package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutOrderDeliveringRequest extends CipCaterStringPairRequest {
    private Long orderId;
    private String courierName;
    private String courierPhone;

    public CipCaterTakeoutOrderDeliveringRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/delivering";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("orderId", orderId.toString());
            put("courierName", courierName);
            put("courierPhone", courierPhone);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return orderId == null || courierName == null || courierName.trim().isEmpty() || courierPhone == null || courierPhone.trim().isEmpty();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }
}
