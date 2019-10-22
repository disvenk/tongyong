package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.PictureSlider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PictureSliderMapper extends BaseDao<PictureSlider,Integer> {

    int insert(PictureSlider record);

    int updateByPrimaryKey(PictureSlider record);
    
    /**
     * 根据店铺ID查询信息
     * @param currentShopId
     * @return
     */
    List<PictureSlider> selectListByShopId(@Param(value = "shopId") String currentShopId);
}
