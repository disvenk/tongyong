package com.resto.shop.web.dao;

import java.util.List;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ShowPhoto;
import org.apache.ibatis.annotations.Param;

public interface ShowPhotoMapper  extends GenericDao<ShowPhoto,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ShowPhoto record);

    int insertSelective(ShowPhoto record);

    ShowPhoto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShowPhoto record);

    int updateByPrimaryKey(ShowPhoto record);

	List<ShowPhoto> selectListByShopId(String currentShopId);

    void updatePhotoSquare(@Param("id") Integer id, @Param("photoSquare") String photoSquare);
}
