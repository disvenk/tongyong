package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ShowPhoto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShowPhotoMapper extends BaseDao<ShowPhoto,Integer> {

    int deleteByPrimaryKey(Integer id);

    int insert(ShowPhoto record);

    int insertSelective(ShowPhoto record);

    ShowPhoto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShowPhoto record);

    int updateByPrimaryKey(ShowPhoto record);

	List<ShowPhoto> selectListByShopId(String currentShopId);

    void updatePhotoSquare(@Param("id") Integer id, @Param("photoSquare") String photoSquare);
}
