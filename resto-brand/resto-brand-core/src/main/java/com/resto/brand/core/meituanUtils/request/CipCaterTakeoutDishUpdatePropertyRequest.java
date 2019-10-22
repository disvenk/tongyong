package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.*;
import com.alibaba.fastjson.JSONObject;
import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 创建/更新菜品属性
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutDishUpdatePropertyRequest extends CipCaterStringPairRequest {

    @NotNull
    private List<WaiMaiDishPropertyVO> dishProperty;

    public CipCaterTakeoutDishUpdatePropertyRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/updateProperty";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            if (dishProperty != null) {
                put("dishProperty", JSONObject.toJSONString(dishProperty));
            }
        }};
    }

    public List<WaiMaiDishPropertyVO> getDishProperty(){
        return dishProperty;
    }

    public void setDishProperty(List<WaiMaiDishPropertyVO> dishProperty){
        this.dishProperty = dishProperty;
    }
}