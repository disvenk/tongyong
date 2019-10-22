package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterQuickrabateOrderConfirmRequest extends CipCaterStringPairRequest {
    private String orderId;
    private Long eOrderId;

    public CipCaterQuickrabateOrderConfirmRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/shanhui/order/confirm";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", orderId);
        if (eOrderId != null) {
            params.put("eOrderId", eOrderId.toString());
        }
        return params;
    }

    @Override
    public boolean paramsAbsent() {
        return orderId == null || orderId.trim().isEmpty();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long geteOrderId() {
        return eOrderId;
    }

    public void seteOrderId(Long eOrderId) {
        this.eOrderId = eOrderId;
    }
}
