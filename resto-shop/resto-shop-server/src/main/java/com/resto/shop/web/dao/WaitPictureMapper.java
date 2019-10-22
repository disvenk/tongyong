package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.WaitPicture;

import java.util.List;

public interface WaitPictureMapper  extends GenericDao<WaitPicture,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(WaitPicture record);

    int insertSelective(WaitPicture record);

    WaitPicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WaitPicture record);

    int updateByPrimaryKey(WaitPicture record);

    int updateStateById(WaitPicture record);

    List<WaitPicture> getWaitPictureList(String shopId);
}
