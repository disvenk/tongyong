package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.PictureSlider;
import com.resto.service.shop.mapper.PictureSliderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureSliderService extends BaseService<PictureSlider, Integer> {

    @Autowired
    private PictureSliderMapper picturesliderMapper;

    @Override
    public BaseDao<PictureSlider, Integer> getDao() {
        return picturesliderMapper;
    }

	public List<PictureSlider> selectListByShopId(String shopId) {
		return picturesliderMapper.selectListByShopId(shopId);
	} 

}
