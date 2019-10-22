package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置门店延迟发配送时间
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutPoiDelayDispatchUpdateRequest extends CipCaterStringPairRequest {

    private Integer delaySeconds;

    public CipCaterTakeoutPoiDelayDispatchUpdateRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/poi/updateDelayDispatch";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("delaySeconds", delaySeconds.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= delaySeconds == null;
        return absent;
    }

    public Integer getDelaySeconds(){
        return delaySeconds;
    }

    public void setDelaySeconds(Integer delaySeconds){
        this.delaySeconds = delaySeconds;
    }

}