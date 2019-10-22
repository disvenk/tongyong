package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据门店拉取外卖订单
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutOrderPullByEpoiIdsRequest extends CipCaterStringPairRequest {

    private String epoiIds;

    public CipCaterTakeoutOrderPullByEpoiIdsRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/queryByEpoids";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("epoiIds", epoiIds.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= epoiIds == null || epoiIds.trim().isEmpty();
        return absent;
    }

    public String getEpoiIds(){
        return epoiIds;
    }

    public void setEpoiIds(String epoiIds){
        this.epoiIds = epoiIds;
    }

}
