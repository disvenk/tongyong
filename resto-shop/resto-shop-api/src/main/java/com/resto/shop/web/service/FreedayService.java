package com.resto.shop.web.service;

import java.util.Date;
import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.FreeDay;

public interface FreedayService extends GenericService<FreeDay, String> {

    List<FreeDay> list(FreeDay day);

    int delete(FreeDay day);

    void setMonthWeekend(FreeDay day) throws Exception;

    void setYearWeekend(FreeDay day) throws Exception;

	boolean selectExists(Date date,String shopid);


    
    
}
