package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutDishMapRequest extends CipCaterStringPairRequest {
    private String ePoiId;
    private String dishMappings;

    public CipCaterTakeoutDishMapRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/mapping";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("ePoiId", ePoiId);
            put("dishMappings", dishMappings);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return ePoiId == null || ePoiId.trim().isEmpty() || dishMappings == null || dishMappings.trim().isEmpty();
    }

    public String getePoiId() {
        return ePoiId;
    }

    public void setePoiId(String ePoiId) {
        this.ePoiId = ePoiId;
    }

    public String getDishMappings() {
        return dishMappings;
    }

    public void setDishMappings(String dishMappings) {
        this.dishMappings = dishMappings;
    }
}
