package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutDishPriceUpdateRequest extends CipCaterStringPairRequest {
    private String ePoiId;
    private String dishSkuPrices;

    public CipCaterTakeoutDishPriceUpdateRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/updatePrice";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("ePoiId", ePoiId);
            put("dishSkuPrices", dishSkuPrices);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return ePoiId == null || ePoiId.trim().isEmpty() || dishSkuPrices == null || dishSkuPrices.trim().isEmpty();
    }

    public String getePoiId() {
        return ePoiId;
    }

    public void setePoiId(String ePoiId) {
        this.ePoiId = ePoiId;
    }

    public String getDishSkuPrices() {
        return dishSkuPrices;
    }

    public void setDishSkuPrices(String dishSkuPrices) {
        this.dishSkuPrices = dishSkuPrices;
    }
}
