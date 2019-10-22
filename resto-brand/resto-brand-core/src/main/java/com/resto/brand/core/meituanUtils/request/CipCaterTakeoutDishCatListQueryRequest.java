package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询菜品分类
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutDishCatListQueryRequest extends CipCaterStringPairRequest {


    public CipCaterTakeoutDishCatListQueryRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/queryCatList";
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