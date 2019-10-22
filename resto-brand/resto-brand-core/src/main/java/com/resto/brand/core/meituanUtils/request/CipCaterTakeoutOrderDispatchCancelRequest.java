package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 取消美团配送订单
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutOrderDispatchCancelRequest extends CipCaterStringPairRequest {

    private Long orderId;

    public CipCaterTakeoutOrderDispatchCancelRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/cancelDispatch";
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