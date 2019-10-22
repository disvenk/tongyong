package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutDishDeleteRequest extends CipCaterStringPairRequest {
    private String ePoiId;
    private String eDishCode;

    public CipCaterTakeoutDishDeleteRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/delete";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("ePoiId", ePoiId);
            put("eDishCode", eDishCode);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return ePoiId == null || ePoiId.trim().isEmpty() || eDishCode == null || eDishCode.trim().isEmpty();
    }

    public String getePoiId() {
        return ePoiId;
    }

    public void setePoiId(String ePoiId) {
        this.ePoiId = ePoiId;
    }

    public String geteDishCode() {
        return eDishCode;
    }

    public void seteDishCode(String eDishCode) {
        this.eDishCode = eDishCode;
    }
}
