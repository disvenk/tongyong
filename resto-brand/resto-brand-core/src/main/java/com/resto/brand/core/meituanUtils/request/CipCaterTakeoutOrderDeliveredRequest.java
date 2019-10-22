package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutOrderDeliveredRequest extends CipCaterStringPairRequest {
    private Long orderId;

    public CipCaterTakeoutOrderDeliveredRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/delivered";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("orderId", orderId.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return orderId == null;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
