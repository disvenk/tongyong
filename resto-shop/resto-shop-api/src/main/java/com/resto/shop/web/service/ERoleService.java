package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ERole;

import java.util.List;

public interface ERoleService extends GenericService<ERole, Long> {


    List<ERole> selectByStauts();

}
