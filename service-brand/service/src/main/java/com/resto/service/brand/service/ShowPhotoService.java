package com.resto.service.brand.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.ShowPhoto;
import com.resto.service.brand.mapper.ShowPhotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class ShowPhotoService extends BaseService<ShowPhoto, Integer> {

    @Autowired
    private ShowPhotoMapper showphotoMapper;

    public BaseDao<ShowPhoto, Integer> getDao() {
        return showphotoMapper;
    } 

}
