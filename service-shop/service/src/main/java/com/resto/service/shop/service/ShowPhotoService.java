package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ShowPhoto;
import com.resto.service.shop.mapper.ShowPhotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowPhotoService extends BaseService<ShowPhoto, Integer> {

    @Autowired
    private ShowPhotoMapper showphotoMapper;

    @Override
    public BaseDao<ShowPhoto, Integer> getDao() {
        return showphotoMapper;
    }

	public List<ShowPhoto> selectListByShopId(String currentShopId) {
		return showphotoMapper.selectListByShopId(currentShopId);
	}

    public void updatePhotoSquare(Integer id, String photoSquare) {
        showphotoMapper.updatePhotoSquare(id, photoSquare);
    }

}
