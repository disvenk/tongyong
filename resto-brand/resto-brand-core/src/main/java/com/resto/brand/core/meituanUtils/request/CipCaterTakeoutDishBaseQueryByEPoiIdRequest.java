package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutDishBaseQueryByEPoiIdRequest extends CipCaterStringPairRequest {
    private String ePoiId;

    public CipCaterTakeoutDishBaseQueryByEPoiIdRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/queryBaseListByEPoiId";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("ePoiId", ePoiId);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return ePoiId == null || ePoiId.trim().isEmpty();
    }

    public String getePoiId() {
        return ePoiId;
    }

    public void setePoiId(String ePoiId) {
        this.ePoiId = ePoiId;
    }
}
