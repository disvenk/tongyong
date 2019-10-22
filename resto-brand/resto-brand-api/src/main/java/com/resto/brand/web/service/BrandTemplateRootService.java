package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.BrandTemplateEdit;
import com.resto.brand.web.model.BrandTemplateRoot;

public interface BrandTemplateRootService extends GenericService<BrandTemplateRoot, Integer> {

    int addToDoUpateRoot(BrandTemplateRoot brandTemplateRoot);

    int deleteToDoUpdataRoot(Integer id);

    int updateToDoUpdataRoot(BrandTemplateRoot brandTemplateRoot);
    
}
