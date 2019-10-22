package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.ShowPhoto;

public interface ShowPhotoMapper  extends BaseDao<ShowPhoto,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ShowPhoto record);

    int insertSelective(ShowPhoto record);

    ShowPhoto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShowPhoto record);

    int updateByPrimaryKey(ShowPhoto record);
}
