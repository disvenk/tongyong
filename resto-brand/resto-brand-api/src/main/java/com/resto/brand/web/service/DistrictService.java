package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.District;

import java.util.List;

public interface DistrictService extends GenericService<District, Integer> {

    List<District> selectByCityId(Integer cityId);

    
}
