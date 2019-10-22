package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 更改门店营业时间
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutPoiOpenTimeUpdateRequest extends CipCaterStringPairRequest {

    private String openTime;

    public CipCaterTakeoutPoiOpenTimeUpdateRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/poi/updateOpenTime";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("openTime", openTime.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= openTime == null || openTime.trim().isEmpty();
        return absent;
    }

    public String getOpenTime(){
        return openTime;
    }

    public void setOpenTime(String openTime){
        this.openTime = openTime;
    }

}