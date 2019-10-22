package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutOrderRefundRejectRequest extends CipCaterStringPairRequest {
    private Long orderId;
    private String reason;

    public CipCaterTakeoutOrderRefundRejectRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/rejectRefund";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("orderId", orderId.toString());
            put("reason", reason);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return orderId == null || reason == null || reason.trim().isEmpty();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
