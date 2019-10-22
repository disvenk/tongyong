package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ShowPhotoMapper;
import com.resto.shop.web.model.ShowPhoto;
import com.resto.shop.web.service.ShowPhotoService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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

	@Override
	public List<ShowPhoto> selectListByShopId(String currentShopId) {
		return showphotoMapper.selectListByShopId(currentShopId);
	}

    @Override
    public void updatePhotoSquare(Integer id, String photoSquare) {
        showphotoMapper.updatePhotoSquare(id, photoSquare);
    }

}
