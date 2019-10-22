package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 删除菜品SKU
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutDishSkuDeleteRequest extends CipCaterStringPairRequest {

    private String eDishCode;
    private String eDishSkuCode;

    public CipCaterTakeoutDishSkuDeleteRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/deleteSku";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("eDishCode", eDishCode.toString());
            put("eDishSkuCode", eDishSkuCode.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= eDishCode == null || eDishCode.trim().isEmpty();
        absent |= eDishSkuCode == null || eDishSkuCode.trim().isEmpty();
        return absent;
    }

    public String getEDishCode(){
        return eDishCode;
    }

    public void setEDishCode(String eDishCode){
        this.eDishCode = eDishCode;
    }
    public String getEDishSkuCode(){
        return eDishSkuCode;
    }

    public void setEDishSkuCode(String eDishSkuCode){
        this.eDishSkuCode = eDishSkuCode;
    }

}