package com.resto.shop.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.PictureSlider;

public interface PictureSliderMapper  extends GenericDao<PictureSlider,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureSlider record);

    int insertSelective(PictureSlider record);

    PictureSlider selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureSlider record);

    int updateByPrimaryKey(PictureSlider record);
    
    /**
     * 根据店铺ID查询信息
     * @param currentShopId
     * @return
     */
    List<PictureSlider> selectListByShopId(@Param(value = "shopId") String currentShopId);
}
