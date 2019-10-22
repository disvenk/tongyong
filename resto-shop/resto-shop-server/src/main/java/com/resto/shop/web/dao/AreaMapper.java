package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.Area;

import java.util.List;

/**
 * Created by KONATA on 2017/4/5.
 */
public interface AreaMapper extends GenericDao<Area,Long> {

    List<Area> getAreaList(String shopId);

    void  updateArea(String printId);

    void  deleteArea(String printId);
}
