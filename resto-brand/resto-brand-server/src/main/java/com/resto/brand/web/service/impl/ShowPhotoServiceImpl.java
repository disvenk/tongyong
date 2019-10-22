package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ShowPhotoMapper;
import com.resto.brand.web.model.ShowPhoto;
import com.resto.brand.web.service.ShowPhotoService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *
 */
@Component
@Service
public class ShowPhotoServiceImpl extends GenericServiceImpl<ShowPhoto, Integer> implements ShowPhotoService {

    @Resource
    private ShowPhotoMapper showphotoMapper;

    @Override
    public GenericDao<ShowPhoto, Integer> getDao() {
        return showphotoMapper;
    } 

}
