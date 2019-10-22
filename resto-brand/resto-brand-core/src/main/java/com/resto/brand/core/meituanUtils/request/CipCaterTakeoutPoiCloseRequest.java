package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 门店置休息
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutPoiCloseRequest extends CipCaterStringPairRequest {


    public CipCaterTakeoutPoiCloseRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/poi/close";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        return absent;
    }


}