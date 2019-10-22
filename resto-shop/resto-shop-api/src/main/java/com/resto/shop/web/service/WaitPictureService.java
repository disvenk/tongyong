package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WaitPicture;

import java.util.List;

public interface WaitPictureService extends GenericService<WaitPicture, Integer> {

    int updateStateById(WaitPicture record);

    List<WaitPicture> getWaitPictureList(String shopId);
}
