package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 新增/更新菜品分类
 * <p>
 * Created by FreeMarker
 */
public class CipCaterTakeoutDishCatUpdateRequest extends CipCaterStringPairRequest {

    private String catName;
    private Integer sequence;
    private String oldCatName;

    public CipCaterTakeoutDishCatUpdateRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/dish/updateCat";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("catName", catName.toString());
            if (sequence != null) {
                put("sequence", sequence.toString());
            }
            if (oldCatName != null) {
                put("oldCatName", oldCatName.toString());
            }
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

    public Integer getSequence(){
        return sequence;
    }

    public void setSequence(Integer sequence){
        this.sequence = sequence;
    }
    public String getOldCatName(){
        return oldCatName;
    }

    public void setOldCatName(String oldCatName){
        this.oldCatName = oldCatName;
    }
}