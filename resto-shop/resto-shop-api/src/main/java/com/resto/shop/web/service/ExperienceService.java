package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.Experience;

import java.util.List;

public interface ExperienceService extends GenericService<Experience, Integer> {

    int deleteByTitle(String title, String shopId);

    List<Experience> selectListByShopId(String shopId);
    
}
