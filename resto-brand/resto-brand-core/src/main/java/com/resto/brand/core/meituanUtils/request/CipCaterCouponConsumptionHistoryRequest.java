package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterCouponConsumptionHistoryRequest extends CipCaterStringPairRequest {
    private String date;
    private Integer offset;
    private Integer limit;

    public CipCaterCouponConsumptionHistoryRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/tuangou/coupon/queryListByDate";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("date", date);
            put("offset", offset.toString());
            put("limit", limit.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return date == null || date.trim().isEmpty() || offset == null || limit == null;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
