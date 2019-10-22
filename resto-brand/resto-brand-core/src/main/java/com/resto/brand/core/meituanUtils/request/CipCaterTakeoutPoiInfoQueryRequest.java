package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询门店信息
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutPoiInfoQueryRequest extends CipCaterStringPairRequest {

    private String ePoiIds;

    public CipCaterTakeoutPoiInfoQueryRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/poi/queryPoiInfo";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("ePoiIds", ePoiIds.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= ePoiIds == null || ePoiIds.trim().isEmpty();
        return absent;
    }

    public String getEPoiIds(){
        return ePoiIds;
    }

    public void setEPoiIds(String ePoiIds){
        this.ePoiIds = ePoiIds;
    }

}