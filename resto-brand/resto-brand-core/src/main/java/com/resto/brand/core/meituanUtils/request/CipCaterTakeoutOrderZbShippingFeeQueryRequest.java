package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 批量查询众包配送费
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutOrderZbShippingFeeQueryRequest extends CipCaterStringPairRequest {

    private String orderIds;

    public CipCaterTakeoutOrderZbShippingFeeQueryRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/queryZbShippingFee";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("orderIds", orderIds.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= orderIds == null || orderIds.trim().isEmpty();
        return absent;
    }

    public String getOrderIds(){
        return orderIds;
    }

    public void setOrderIds(String orderIds){
        this.orderIds = orderIds;
    }

}