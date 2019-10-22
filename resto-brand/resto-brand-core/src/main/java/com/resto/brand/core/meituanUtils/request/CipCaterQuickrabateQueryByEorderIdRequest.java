package com.resto.brand.core.meituanUtils.request;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * B扫C:凭第三方订单号查询
 * <p>
 * Created by FreeMarker
 */
public class CipCaterQuickrabateQueryByEorderIdRequest extends CipCaterStringPairRequest {

    @NotNull
    private String eOrderId;

    public CipCaterQuickrabateQueryByEorderIdRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/shanhui/order/queryByEorderId";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            if (eOrderId != null) {
                put("eOrderId", JSONObject.toJSONString(eOrderId));
            }
        }};
    }

    public String getEOrderId(){
        return eOrderId;
    }

    public void setEOrderId(String eOrderId){
        this.eOrderId = eOrderId;
    }
}