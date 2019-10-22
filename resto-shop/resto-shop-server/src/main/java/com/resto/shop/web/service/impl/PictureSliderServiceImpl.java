package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.PictureSliderMapper;
import com.resto.shop.web.model.PictureSlider;
import com.resto.shop.web.service.PictureSliderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class PictureSliderServiceImpl extends GenericServiceImpl<PictureSlider, Integer> implements PictureSliderService {

    @Resource
    private PictureSliderMapper picturesliderMapper;

    @Override
    public GenericDao<PictureSlider, Integer> getDao() {
        return picturesliderMapper;
    }

	@Override
	public List<PictureSlider> selectListByShopId(String shopId) {
		return picturesliderMapper.selectListByShopId(shopId);
	} 

}
