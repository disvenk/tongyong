package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.BrandTemplateEdit;

import java.util.List;

public interface BrandTemplateEditService extends GenericService<BrandTemplateEdit, Integer> {

    List<BrandTemplateEdit> selectByBrandId(String brandId);

    BrandTemplateEdit selectOneByManyTerm(String templateNum,String appId,Integer templateSign);

    int reset(Integer id,Boolean bigOpen);

    int startOrOpenById(Integer id,Integer bigOpen);

    int distributionOrDelete(String BrandId,String appId,Integer flag);
    
}
