package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutDishStockUpdateRequest extends CipCaterStringPairRequest {
    private String ePoiId;
    private String dishSkuStocks;

    public CipCaterTakeoutDishStockUpdateRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/updateStock";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("ePoiId", ePoiId);
            put("dishSkuStocks", dishSkuStocks);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return ePoiId == null || ePoiId.trim().isEmpty() || dishSkuStocks == null || dishSkuStocks.trim().isEmpty();
    }

    public String getePoiId() {
        return ePoiId;
    }

    public void setePoiId(String ePoiId) {
        this.ePoiId = ePoiId;
    }

    public String getDishSkuStocks() {
        return dishSkuStocks;
    }

    public void setDishSkuStocks(String dishSkuStocks) {
        this.dishSkuStocks = dishSkuStocks;
    }
}
