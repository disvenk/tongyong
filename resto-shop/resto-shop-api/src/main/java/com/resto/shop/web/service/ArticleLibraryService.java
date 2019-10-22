package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ArticleLibrary;

public interface ArticleLibraryService extends GenericService<ArticleLibrary, String> {

    void addArticleLikes(String id);
    
}
