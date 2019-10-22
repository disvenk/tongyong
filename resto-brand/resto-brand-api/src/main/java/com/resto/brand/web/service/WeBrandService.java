package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.WeBrand;

import java.util.List;

public interface WeBrandService extends GenericService<WeBrand, Integer> {

    List<WeBrand> selectWeBrandList(String createTime);
}
