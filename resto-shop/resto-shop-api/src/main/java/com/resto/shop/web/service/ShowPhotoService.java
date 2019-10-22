package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ShowPhoto;

import java.util.List;

public interface ShowPhotoService extends GenericService<ShowPhoto, Integer> {

	List<ShowPhoto> selectListByShopId(String currentShopId);

	void updatePhotoSquare(Integer id, String photoSquare);
    
}
