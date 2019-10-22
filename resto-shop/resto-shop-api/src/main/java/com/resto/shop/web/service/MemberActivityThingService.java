package com.resto.shop.web.service;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.MemberActivityThing;

import java.util.List;

public interface MemberActivityThingService extends GenericService<MemberActivityThing, Integer> {

    MemberActivityThing selectByTelephone(String telephone);

    List<JSONObject> selectCustomerInfo(Integer memberActivityId);
}
