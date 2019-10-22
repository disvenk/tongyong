package com.resto.brand.core.meituanUtils.request;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * B扫C:商家提交第三方订单
 * <p>
 * Created by FreeMarker
 */
public class CipCaterQuickrabateCreateOrderRequest extends CipCaterStringPairRequest {

    @NotNull
    private String eOrderId;
    @NotNull
    private String authCode;
    @NotNull
    private BigDecimal originalAmount;
    @NotNull
    private BigDecimal noDiscountAmount;

    public CipCaterQuickrabateCreateOrderRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/shanhui/order/create";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            if (eOrderId != null) {
                put("eOrderId", JSONObject.toJSONString(eOrderId));
            }
            if (authCode != null) {
                put("authCode", JSONObject.toJSONString(authCode));
            }
            if (originalAmount != null) {
                put("originalAmount", JSONObject.toJSONString(originalAmount));
            }
            if (noDiscountAmount != null) {
                put("noDiscountAmount", JSONObject.toJSONString(noDiscountAmount));
            }
        }};
    }

    public String getEOrderId(){
        return eOrderId;
    }

    public void setEOrderId(String eOrderId){
        this.eOrderId = eOrderId;
    }
    public String getAuthCode(){
        return authCode;
    }

    public void setAuthCode(String authCode){
        this.authCode = authCode;
    }
    public BigDecimal getOriginalAmount(){
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount){
        this.originalAmount = originalAmount;
    }
    public BigDecimal getNoDiscountAmount(){
        return noDiscountAmount;
    }

    public void setNoDiscountAmount(BigDecimal noDiscountAmount){
        this.noDiscountAmount = noDiscountAmount;
    }
}
