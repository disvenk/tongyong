package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询门店是否延迟发配送
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutPoiDelayDispatchQueryRequest extends CipCaterStringPairRequest {


    public CipCaterTakeoutPoiDelayDispatchQueryRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/poi/queryDelayDispatch";
        this.requestMethod = RequestMethod.GET;
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