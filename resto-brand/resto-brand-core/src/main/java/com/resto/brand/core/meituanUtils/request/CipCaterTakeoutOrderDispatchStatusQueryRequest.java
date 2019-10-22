package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 美团配送状态查询（包含美团专送和众包配送）
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutOrderDispatchStatusQueryRequest extends CipCaterStringPairRequest {

    private Long orderId;

    public CipCaterTakeoutOrderDispatchStatusQueryRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/queryDispatchStatus";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("orderId", orderId.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= orderId == null;
        return absent;
    }

    public Long getOrderId(){
        return orderId;
    }

    public void setOrderId(Long orderId){
        this.orderId = orderId;
    }

}