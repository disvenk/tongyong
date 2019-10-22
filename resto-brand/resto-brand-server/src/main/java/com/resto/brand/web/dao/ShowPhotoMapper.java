package com.resto.brand.web.dao;

import com.resto.brand.web.model.ShowPhoto;
import com.resto.brand.core.generic.GenericDao;

public interface ShowPhotoMapper  extends GenericDao<ShowPhoto,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ShowPhoto record);

    int insertSelective(ShowPhoto record);

    ShowPhoto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShowPhoto record);

    int updateByPrimaryKey(ShowPhoto record);
}
