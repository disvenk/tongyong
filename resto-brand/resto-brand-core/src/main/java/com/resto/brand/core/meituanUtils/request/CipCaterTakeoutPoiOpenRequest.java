package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 门店置营业
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutPoiOpenRequest extends CipCaterStringPairRequest {


    public CipCaterTakeoutPoiOpenRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/poi/open";
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