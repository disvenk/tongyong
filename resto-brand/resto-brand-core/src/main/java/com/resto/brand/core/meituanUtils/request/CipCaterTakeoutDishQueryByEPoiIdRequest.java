package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutDishQueryByEPoiIdRequest extends CipCaterStringPairRequest {
    private String ePoiId;
    private Integer offset;
    private Integer limit;

    public CipCaterTakeoutDishQueryByEPoiIdRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/queryListByEPoiId";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("ePoiId", ePoiId);
            put("offset", offset.toString());
            put("limit", limit.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return ePoiId == null || ePoiId.trim().isEmpty() || offset == null || limit == null;
    }

    public String getePoiId() {
        return ePoiId;
    }

    public void setePoiId(String ePoiId) {
        this.ePoiId = ePoiId;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
