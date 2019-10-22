package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterTakeoutOrderQueryByDaySeqRequest extends CipCaterStringPairRequest {
    private String ePoiId;
    private Integer daySeq;
    private String date;

    public CipCaterTakeoutOrderQueryByDaySeqRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/order/queryByDaySeq";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ePoiId", ePoiId);
        params.put("daySeq", daySeq.toString());
        if (date != null && !date.trim().isEmpty()) {
            params.put("date", date);
        }
        return params;
    }

    @Override
    public boolean paramsAbsent() {
        return ePoiId == null || ePoiId.trim().isEmpty() || daySeq == null;
    }

    public String getePoiId() {
        return ePoiId;
    }

    public void setePoiId(String ePoiId) {
        this.ePoiId = ePoiId;
    }

    public Integer getDaySeq() {
        return daySeq;
    }

    public void setDaySeq(Integer daySeq) {
        this.daySeq = daySeq;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
