package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.Area;

import java.util.List;

/**
 * Created by KONATA on 2017/4/5.
 */
public interface AreaService extends GenericService<Area, Long> {

    List<Area> getAreaList(String shopId);



    void deleteArea(String printId);
}
