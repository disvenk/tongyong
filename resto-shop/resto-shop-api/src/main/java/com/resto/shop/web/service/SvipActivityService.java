package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.SvipActivity;

public interface SvipActivityService extends GenericService<SvipActivity, String> {

    public Long getOpendAct();

    public SvipActivity getAct();

    public Long getMyself(String id);
    
}
