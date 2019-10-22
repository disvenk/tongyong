package com.resto.brand.core.meituanUtils.request;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询菜品属性
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutDishQueryPropertyRequest extends CipCaterStringPairRequest {

    @NotNull
    private String eDishCode;

    public CipCaterTakeoutDishQueryPropertyRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/queryPropertyList";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            if (eDishCode != null) {
                put("eDishCode", JSONObject.toJSONString(eDishCode));
            }
        }};
    }

    public String getEDishCode(){
        return eDishCode;
    }

    public void setEDishCode(String eDishCode){
        this.eDishCode = eDishCode;
    }
}