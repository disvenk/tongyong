package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 众包配送预下单
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutOrderZbDispatchPrepareRequest extends CipCaterStringPairRequest {

    private Long orderId;
    private Double shippingFee;
    private Double tipAmount;

    public CipCaterTakeoutOrderZbDispatchPrepareRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/prepareZbDispatch";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("orderId", orderId.toString());
            put("shippingFee", shippingFee.toString());
            put("tipAmount", tipAmount.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= orderId == null;
        absent |= shippingFee == null;
        absent |= tipAmount == null;
        return absent;
    }

    public Long getOrderId(){
        return orderId;
    }

    public void setOrderId(Long orderId){
        this.orderId = orderId;
    }
    public Double getShippingFee(){
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee){
        this.shippingFee = shippingFee;
    }
    public Double getTipAmount(){
        return tipAmount;
    }

    public void setTipAmount(Double tipAmount){
        this.tipAmount = tipAmount;
    }

}