package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 删除菜品分类
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutDishCatDeleteRequest extends CipCaterStringPairRequest {

    private String catName;

    public CipCaterTakeoutDishCatDeleteRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/deleteCat";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("catName", catName.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        boolean absent = false;
        absent |= catName == null || catName.trim().isEmpty();
        return absent;
    }

    public String getCatName(){
        return catName;
    }

    public void setCatName(String catName){
        this.catName = catName;
    }

}