package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.City;

import java.util.List;

public interface CityService extends GenericService<City, Integer> {
    List<City> selectByProvinceId(Integer provinceId);
}
