package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 众包配送单加小费
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutOrderZbDispatchTipUpdateRequest extends CipCaterStringPairRequest {

    private Long orderId;
    private Double tipAmount;

    public CipCaterTakeoutOrderZbDispatchTipUpdateRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/updateZbDispatchTip";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("orderId", orderId.toString());
            put("tipAmount", tipAmount.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= orderId == null;
        absent |= tipAmount == null;
        return absent;
    }

    public Long getOrderId(){
        return orderId;
    }

    public void setOrderId(Long orderId){
        this.orderId = orderId;
    }
    public Double getTipAmount(){
        return tipAmount;
    }

    public void setTipAmount(Double tipAmount){
        this.tipAmount = tipAmount;
    }

}