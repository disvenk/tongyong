package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterQuickrabateOrderRefundRequest extends CipCaterStringPairRequest {
    private String orderId;

    public CipCaterQuickrabateOrderRefundRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/shanhui/order/refund";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("orderId", orderId);
        }};
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
}
